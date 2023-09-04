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

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;

/**
 *
 * @author arsi
 */
public class MqttConnectStruct {

    private final int protocolNameLength;
    private final String protocolName;
    private final int protocolVersion;
    private final int flags;
    private final int keepAlive;
    private final int clientIdLength;
    private final String clientID;

    public MqttConnectStruct(ByteBuffer buffer) throws UnsupportedEncodingException {
        protocolNameLength = buffer.getShort();
        protocolName = readString(buffer, protocolNameLength);
        protocolVersion = buffer.get();
        flags = buffer.get();
        keepAlive = buffer.getShort();
        clientIdLength = buffer.getShort();
        clientID = readString(buffer, clientIdLength);
    }

    private String readString(ByteBuffer buffer, int length) throws UnsupportedEncodingException {
        byte[] buf = new byte[length];
        buffer.get(buf);
        return new String(buf, "UTF-8");
    }

    public int getProtocolNameLength() {
        return protocolNameLength;
    }

    public String getProtocolName() {
        return protocolName;
    }

    public int getProtocolVersion() {
        return protocolVersion;
    }

    public int getFlags() {
        return flags;
    }

    public int getKeepAlive() {
        return keepAlive;
    }

    public int getClientIdLength() {
        return clientIdLength;
    }

    public String getClientID() {
        return clientID;
    }

}
