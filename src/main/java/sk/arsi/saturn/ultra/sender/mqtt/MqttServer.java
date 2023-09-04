/*
 * Copyright 2023 ArSi (arsi_at_arsi_sk)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package sk.arsi.saturn.ultra.sender.mqtt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.Level;
import java.util.logging.Logger;
import sk.arsi.saturn.ultra.sender.Detail;
import sk.arsi.saturn.ultra.sender.pojo.Attributes.AttrDeserializer;
import sk.arsi.saturn.ultra.sender.pojo.Attributes.AttrRoot;
import sk.arsi.saturn.ultra.sender.pojo.Browse.BrowseRoot;
import sk.arsi.saturn.ultra.sender.pojo.Browse.BrowseRootDeserializer;
import sk.arsi.saturn.ultra.sender.pojo.Status.StatusDeserializer;
import sk.arsi.saturn.ultra.sender.pojo.Status.StatusRoot;
import sk.arsi.saturn.ultra.sender.pojo.load.LoadHandler;
import sk.arsi.saturn.ultra.sender.pojo.print.PrintHandler;

/**
 *
 * @author arsi
 */
public class MqttServer implements Runnable {

    private int localPort = 0;
    private ServerSocket serverSocket;
    private byte header[] = new byte[2];
    public static final ExecutorService POOL = Executors.newFixedThreadPool(1);
    private String clientID;
    private AtomicReference<AttrRoot> attributes = new AtomicReference<>();
    private AtomicReference<StatusRoot> status = new AtomicReference<>();
    private AtomicReference<LoadHandler> file = new AtomicReference<>();
    private AtomicReference<LoadHandler> print = new AtomicReference<>();
    private static final ObjectMapper MAPPER = new ObjectMapper();
    private ActionListener listener = null;
    private AtomicInteger couter = new AtomicInteger(1);
    private short msgId;

    public ActionListener getListener() {
        return listener;
    }

    public void setListener(ActionListener listener) {
        this.listener = listener;
    }

    public void setFileLoad(LoadHandler loadHandler) {
        file.set(loadHandler);
    }

    public void setPrint(LoadHandler handler) {
        print.set(handler);
    }

    public AttrRoot getAttributes() {
        return attributes.get();
    }

    public StatusRoot getStatus() {
        return status.get();
    }

    public MqttServer() {
        SimpleModule module = new SimpleModule();
        module.addDeserializer(StatusRoot.class, new StatusDeserializer());
        module.addDeserializer(BrowseRoot.class, new BrowseRootDeserializer());
        module.addDeserializer(AttrRoot.class, new AttrDeserializer());
        MAPPER.registerModule(module);
        try {
            serverSocket = new ServerSocket(0);
            serverSocket.setReuseAddress(true);
            serverSocket.setSoTimeout(3000);
            localPort = serverSocket.getLocalPort();
            POOL.execute(this);
        } catch (IOException ex) {
            Logger.getLogger(MqttServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public int getPort() {
        return localPort;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Socket clientSocket = serverSocket.accept();
                while (!clientSocket.isClosed() && clientSocket.isConnected()) {
                    InputStream inputStream = clientSocket.getInputStream();
                    OutputStream outputStream = clientSocket.getOutputStream();
                    int read = inputStream.read(header);
                    if (read == 2) {
                        ByteBuffer buffer = ByteBuffer.wrap(header);
                        MessageTypes messageType = MessageTypes.getMessageType((short) ((buffer.get() & 0xf0) >> 4));
                        int length = computeMessageLength(inputStream, buffer.get());
                        byte data[] = new byte[length];
                        read = inputStream.read(data);
                        if (read == length) {
                            switch (messageType) {
                                case Reserved0:
                                    break;
                                case CONNECT: {

                                    buffer = ByteBuffer.wrap(data);
                                    MqttConnectStruct connectStruct = new MqttConnectStruct(buffer);
                                    clientID = connectStruct.getClientID();
                                    System.out.println("Connected client: " + clientID);
                                    data = new byte[]{(byte) 0x20, (byte) 0x02, 0, 0};
                                    outputStream.write(data);
                                    outputStream.flush();
                                }
                                break;
                                case CONNACK:
                                    break;
                                case PUBLISH: {
                                    buffer = ByteBuffer.wrap(data);
                                    short topicLength = buffer.getShort();
                                    String topic = readString(buffer, topicLength);
                                    msgId = buffer.getShort();
                                    String msg = readString(buffer, (short) (((short) data.length) - (((short) 4) + topicLength)));
                                    decodeMessage(topic, msg);
                                    System.out.println("*******************************************");
                                    System.out.println(topic);
                                    System.out.println(msg);
                                    data = new byte[]{(byte) 0x40, (byte) 0x02, (byte) ((msgId >> 8) & 0xff), (byte) ((msgId) & 0xff)};
                                    outputStream.write(data);
                                    outputStream.flush();
                                    LoadHandler fileRoot = file.getAndSet(null);
                                    if (fileRoot != null) {
                                        makeLoadFile(outputStream, fileRoot, msgId);

                                    }
                                    LoadHandler lHandler = print.getAndSet(null);
                                    if (lHandler != null) {
                                        PrintHandler pHandler = new PrintHandler();
                                        pHandler.set(PrintHandler.Keys.Filename, lHandler.get(LoadHandler.Keys.Filename));
                                        pHandler.set(PrintHandler.Keys.MainboardID, lHandler.get(LoadHandler.Keys.MainboardID));
                                        pHandler.set(PrintHandler.Keys.RequestID, Detail.getRandomHexString(32));
                                        pHandler.set(PrintHandler.Keys.TimeStamp, "" + System.currentTimeMillis());
                                        makePrintFile(outputStream, pHandler, msgId);
                                    }
                                }
                                break;
                                case PUBACK: {


                                }
                                break;
                                case PUBREC:
                                    break;
                                case PUBREL:
                                    break;
                                case PUBCOMP:
                                    break;
                                case SUBSCRIBE:
                                    System.out.println("Subscribe: " + clientID);
                                    data = new byte[]{(byte) 0x90, (byte) 0x03, 0, 0x01, 0x01};
                                    outputStream.write(data);
                                    outputStream.flush();
                                    break;
                                case SUBACK:
                                    break;
                                case UNSUBSCRIBE:
                                    break;
                                case UNSUBACK:
                                    break;
                                case PINGREQ:
                                    break;
                                case PINGRESP:
                                    break;
                                case DISCONNECT:
                                    break;
                                case Reserved15:
                                    break;
                                case Unknown:
                                    break;
                                default:
                                    throw new AssertionError(messageType.name());

                            }
                        }
                    }
                }
            } catch (IOException ex) {
                Logger.getLogger(MqttServer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private String readString(ByteBuffer buffer, short length) throws UnsupportedEncodingException {
        byte[] buf = new byte[length];
        buffer.get(buf);
        return new String(buf, "UTF-8");
    }

    private int computeMessageLength(InputStream inputStream, short firstDigit) throws IOException {
        int length = 0;
        int multiplier = 1;
        byte digit = (byte) firstDigit;
        length += (digit & 0x7F) * multiplier;
        if ((digit & 0x80) == 0) {
            return length;
        }
        multiplier <<= 7;
        digit = (byte) inputStream.read();
        length += (digit & 0x7F) * multiplier;
        if ((digit & 0x80) == 0) {
            return length;
        }
        multiplier <<= 7;
        digit = (byte) inputStream.read();
        length += (digit & 0x7F) * multiplier;
        if ((digit & 0x80) == 0) {
            return length;
        }
        multiplier <<= 7;
        digit = (byte) inputStream.read();
        length += (digit & 0x7F) * multiplier;
        if ((digit & 0x80) == 0) {
            return length;
        }
        return length;

    }

    private void decodeMessage(String topic, String msg) {
        if (topic.startsWith("/sdcp/status/")) {
            try {
                StatusRoot readValue = MAPPER.readValue(msg, StatusRoot.class);
                status.set(readValue);
            } catch (IOException ex) {
                Logger.getLogger(MqttServer.class.getName()).log(Level.SEVERE, null, ex);
            }

        } else if (topic.startsWith("/sdcp/attributes/")) {
            try {
                AttrRoot readValue = MAPPER.readValue(msg, AttrRoot.class);
                attributes.set(readValue);
            } catch (IOException ex) {
                Logger.getLogger(MqttServer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        if (listener != null) {
            listener.actionPerformed(null);
        }
    }

    private void makeLoadFile(OutputStream outputStream, LoadHandler handler, short msgId) {
        try {
            //  try {
            // byte[] bytes = MAPPER.writeValueAsBytes(fileRoot);
            ByteBuffer tmp = ByteBuffer.allocate(1000);
            tmp.putShort((short) 30);
            String path = "/sdcp/request/" + handler.get(LoadHandler.Keys.MainboardID);
            tmp.put(path.getBytes());
            tmp.putShort((short) (msgId + 1));
            String json = handler.getJson();
            System.out.println(json);
            tmp.put(json.getBytes("UTF-8"));
            tmp.flip();
            int limit = tmp.limit();
            ByteBuffer out = ByteBuffer.allocate(1000);
            out.put((byte) 0x32);
            int cele = limit / 128;
            int zvysok = limit - (cele * 128);
            zvysok = zvysok | 128;
            out.put((byte) zvysok);
            out.put((byte) cele);
            out.put(tmp);
            out.flip();
            byte b[] = new byte[out.limit()];
            out.get(b);
            try {
                outputStream.write(b);
                outputStream.flush();

//        } catch (JsonProcessingException ex) {
//            Logger.getLogger(MqttServer.class.getName()).log(Level.SEVERE, null, ex);
//        }
            } catch (IOException ex) {
                Logger.getLogger(MqttServer.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(MqttServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void makePrintFile(OutputStream outputStream, PrintHandler handler, short msgId) {
        try {
            //  try {
            // byte[] bytes = MAPPER.writeValueAsBytes(fileRoot);
            ByteBuffer tmp = ByteBuffer.allocate(1000);
            tmp.putShort((short) 30);
            String path = "/sdcp/request/" + handler.get(PrintHandler.Keys.MainboardID);
            tmp.put(path.getBytes());
            tmp.putShort((short) (msgId + 2));
            String json = handler.getJson();
            System.out.println(json);
            tmp.put(json.getBytes("UTF-8"));
            tmp.flip();
            int limit = tmp.limit();
            ByteBuffer out = ByteBuffer.allocate(1000);
            out.put((byte) 0x32);
            int cele = limit / 128;
            int zvysok = limit - (cele * 128);
            zvysok = zvysok | 128;
            out.put((byte) zvysok);
            out.put((byte) cele);
            out.put(tmp);
            out.flip();
            byte b[] = new byte[out.limit()];
            out.get(b);
            try {
                outputStream.write(b);
                outputStream.flush();

//        } catch (JsonProcessingException ex) {
//            Logger.getLogger(MqttServer.class.getName()).log(Level.SEVERE, null, ex);
//        }
            } catch (IOException ex) {
                Logger.getLogger(MqttServer.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(MqttServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
