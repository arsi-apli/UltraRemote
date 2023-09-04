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
package sk.arsi.saturn.ultra.sender;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Properties;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.Timer;
import javax.swing.filechooser.FileNameExtensionFilter;
import sk.arsi.saturn.ultra.sender.httpserver.SimpleHttpServer;
import sk.arsi.saturn.ultra.sender.mqtt.MqttServer;
import sk.arsi.saturn.ultra.sender.pojo.Browse.BrowseRoot;
import sk.arsi.saturn.ultra.sender.pojo.Status.StatusRoot;
import sk.arsi.saturn.ultra.sender.pojo.load.LoadHandler;

/**
 *
 * @author arsi
 */
public class Detail extends javax.swing.JPanel implements ActionListener {

    private final BrowseRoot root;
    private final MqttServer server;
    private String xchecksum = "";
    private String xfilename = "";
    private long xfilesize = 0;
    private final Timer commTimer;
    private final Properties properties;
    private final SimpleHttpServer httpServer;
    private final AtomicReference<LoadHandler> print = new AtomicReference<>();
    private final AtomicReference<LoadHandler> printStage2 = new AtomicReference<>();
    private Timer printTimer;

    /**
     * Creates new form Detail
     *
     * @param root
     */
    public Detail(BrowseRoot root, String filename) {
        initComponents();
        properties = new Properties();
        try {
            properties.load(new FileReader("ultra.cfg"));
        } catch (Exception ex) {
        }
        send.setEnabled(false);
        wait.setVisible(false);
        try {
            if (filename != null) {
                byte[] data = Files.readAllBytes(Paths.get(filename));
                xfilesize = Files.size(Paths.get(filename));
                byte[] hash = MessageDigest.getInstance("MD5").digest(data);
                xchecksum = new BigInteger(1, hash).toString(16);
                this.xfilename = filename;
                send.setEnabled(true);
                System.out.println("*************TMP: " + SimpleHttpServer.TMP_DIR);
                Path from = Paths.get(this.xfilename); //convert from File to Path
                Path to = Paths.get(SimpleHttpServer.TMP_DIR + File.separator + new File(xfilename).getName()); //convert from String to Path
                Files.copy(from, to, StandardCopyOption.REPLACE_EXISTING);

            }

        } catch (IOException iOException) {
        } catch (NoSuchAlgorithmException noSuchAlgorithmException) {
        }

        this.root = root;
        name.setText(root.data.attributes.name);
        model.setText(root.data.attributes.machineName);
        ip.setText(root.data.attributes.mainboardIP);
        firmware.setText(root.data.attributes.firmwareVersion);
        resolution.setText(root.data.attributes.resolution);
        if (root.data.attributes.machineName.contains("Saturn")) {
            img.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sk/arsi/saturn/ultra/sender/Saturn3Ultra3_180x.png"))); // NOI18N
        } else if (root.data.attributes.machineName.contains("Mars")) {
            img.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sk/arsi/saturn/ultra/sender/Mars4Ultra_120x120.png"))); // NOI18N
        } else {
            img.setIcon(null);
        }
        server = new MqttServer();
        server.setListener(this);
        int port = server.getPort();
        try (DatagramSocket socket = new DatagramSocket()) {
            socket.setSoTimeout(2000);
            socket.setReuseAddress(true);
            String startMqtt = "M66666 " + port;

            DatagramPacket packet = new DatagramPacket(startMqtt.getBytes(), startMqtt.getBytes().length, InetAddress.getByName(root.data.attributes.mainboardIP), 3000);
            socket.send(packet);
        } catch (SocketException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnknownHostException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
        commTimer = new Timer(10000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                labCom.setText("  Offline  ");
                labCom.setBackground(Color.red);
            }
        });
        commTimer.setRepeats(true);
        commTimer.start();
        httpServer = new SimpleHttpServer();
        Thread thread = new Thread(httpServer);
        thread.start();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException ex) {
            Logger.getLogger(Detail.class.getName()).log(Level.SEVERE, null, ex);
        }
        httpServer.refresh();
        httpPort.setText("" + httpServer.getAddress().getPort());
        httpDir.setText(SimpleHttpServer.TMP_DIR);

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        status = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        name = new javax.swing.JLabel();
        model = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        ip = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        firmware = new javax.swing.JLabel();
        resolution = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        img = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        cFilename = new javax.swing.JLabel();
        currentLayer = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        totalLayers = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        curJob = new javax.swing.JLabel();
        progressLayers = new javax.swing.JProgressBar();
        totJob = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        error = new javax.swing.JLabel();
        progressTime = new javax.swing.JProgressBar();
        jPanel3 = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        filename = new javax.swing.JLabel();
        filesize = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        filemd5 = new javax.swing.JLabel();
        send = new javax.swing.JButton();
        select = new javax.swing.JButton();
        wait = new javax.swing.JLabel();
        jToolBar1 = new javax.swing.JToolBar();
        labCom = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jLabel16 = new javax.swing.JLabel();
        httpPort = new javax.swing.JLabel();
        httpDir = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Printer"));

        status.setText("......");

        jLabel5.setText("Status:");

        jLabel4.setText("Name:");

        name.setText("......");

        model.setText("......");

        jLabel1.setText("Model:");

        jLabel6.setText("IP:");

        ip.setText("......");

        jLabel7.setText("Firmware:");

        firmware.setText("......");

        resolution.setText("......");

        jLabel8.setText("Resolution:");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(img)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(35, 35, 35)
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(model))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel6, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel7, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel8, javax.swing.GroupLayout.Alignment.TRAILING))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(name)
                            .addComponent(status)
                            .addComponent(ip)
                            .addComponent(firmware)
                            .addComponent(resolution))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel1)
                            .addComponent(model))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel4)
                            .addComponent(name))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel6)
                            .addComponent(ip))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel7)
                            .addComponent(firmware))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel8)
                            .addComponent(resolution))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel5)
                            .addComponent(status)))
                    .addComponent(img))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Current job"));

        jLabel2.setText("File name:");

        cFilename.setText(".......");

        currentLayer.setText(".......");

        jLabel3.setText("Current layer:");

        totalLayers.setText(".......");

        jLabel9.setText("Total layers:");

        jLabel11.setText("Current job time:");

        curJob.setText(".......");

        totJob.setText(".......");

        jLabel12.setText("Total job time:");

        jLabel14.setText("Error number:");

        error.setText(".......");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel9, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel11, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel12, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel14, javax.swing.GroupLayout.Alignment.TRAILING))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(progressLayers, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(cFilename)
                            .addComponent(currentLayer)
                            .addComponent(totalLayers)
                            .addComponent(curJob)
                            .addComponent(totJob)
                            .addComponent(error))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(progressTime, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(cFilename))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(currentLayer))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(totalLayers))
                .addGap(9, 9, 9)
                .addComponent(progressLayers, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11)
                    .addComponent(curJob))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel12)
                    .addComponent(totJob))
                .addGap(9, 9, 9)
                .addComponent(progressTime, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel14)
                    .addComponent(error))
                .addContainerGap())
        );

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder("File"));

        jLabel10.setText("Name:");

        filename.setText(".......");

        filesize.setText(".......");

        jLabel13.setText("Size:");

        jLabel15.setText("MD5:");

        filemd5.setText(".......");

        send.setText("Send to printer");
        send.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sendActionPerformed(evt);
            }
        });

        select.setText("Select another file");
        select.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                selectActionPerformed(evt);
            }
        });

        wait.setBackground(new java.awt.Color(255, 255, 153));
        wait.setText("Waiting for the file transfer to complete");
        wait.setOpaque(true);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(34, 34, 34)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel13, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel10, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel15, javax.swing.GroupLayout.Alignment.TRAILING))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(filename)
                    .addComponent(filesize)
                    .addComponent(filemd5))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(173, 173, 173)
                .addComponent(send)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(select)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(wait))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(filename))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel13)
                    .addComponent(filesize))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel15)
                    .addComponent(filemd5))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(send)
                    .addComponent(select))
                .addContainerGap())
            .addComponent(wait, javax.swing.GroupLayout.Alignment.TRAILING)
        );

        jToolBar1.setFloatable(false);

        labCom.setBackground(java.awt.Color.red);
        labCom.setText("Offline");
        labCom.setOpaque(true);
        jToolBar1.add(labCom);

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder("Http server"));

        jLabel16.setText("Port:");

        httpPort.setText("..............");

        httpDir.setText("..............");

        jLabel17.setText("Web dir:");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(36, 36, 36)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel17, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel16, javax.swing.GroupLayout.Alignment.TRAILING))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(httpPort)
                    .addComponent(httpDir))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel16)
                    .addComponent(httpPort))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel17)
                    .addComponent(httpDir)))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jToolBar1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void selectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_selectActionPerformed
        // TODO add your handling code here:
        String dir = properties.getProperty("DIR");
        JFileChooser j = new JFileChooser();
        if (dir != null) {
            j.setCurrentDirectory(new File(dir));
        }
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Slicer files", "goo", "GOO", "ctb", "CTB");
        j.setFileFilter(filter);
        j.setFileSelectionMode(JFileChooser.FILES_ONLY);
        j.setMultiSelectionEnabled(false);
        int showOpenDialog = j.showOpenDialog(this);
        if (showOpenDialog == JFileChooser.APPROVE_OPTION) {
            File selectedFile = j.getSelectedFile();
            if (selectedFile.exists()) {
                try {
                    byte[] data = Files.readAllBytes(Paths.get(selectedFile.getAbsolutePath()));
                    xfilesize = Files.size(Paths.get(selectedFile.getAbsolutePath()));
                    byte[] hash = MessageDigest.getInstance("MD5").digest(data);
                    xchecksum = new BigInteger(1, hash).toString(16);
                    this.xfilename = selectedFile.getAbsolutePath();
                    send.setEnabled(true);
                    properties.setProperty("DIR", selectedFile.getParent());
                    properties.store(new FileWriter("ultra.cfg"), "");
                    actionPerformed(null);
                    if (xfilename != null) {
                        Path from = Paths.get(xfilename); //convert from File to Path
                        Path to = Paths.get(SimpleHttpServer.TMP_DIR + File.separator + new File(xfilename).getName()); //convert from String to Path
                        Files.copy(from, to, StandardCopyOption.REPLACE_EXISTING);
                        httpServer.refresh();
                    }
                } catch (IOException ex) {
                    Logger.getLogger(Detail.class.getName()).log(Level.SEVERE, null, ex);
                } catch (NoSuchAlgorithmException ex) {
                    Logger.getLogger(Detail.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }//GEN-LAST:event_selectActionPerformed

    private void sendActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sendActionPerformed
        // TODO add your handling code here:
        LoadHandler handler = new LoadHandler();
        handler.set(LoadHandler.Keys.MD5, filemd5.getText());
        handler.set(LoadHandler.Keys.MainboardID, root.data.attributes.mainboardID);
        handler.set(LoadHandler.Keys.FileSize, filesize.getText());
        handler.set(LoadHandler.Keys.TimeStamp, "" + System.currentTimeMillis());
        String fname = new File(this.filename.getText()).getName();
        String downloadUrl = "http://${ipaddr}:" + httpServer.getAddress().getPort() + "/" + fname;
        // String downloadUrl = "http://${ipaddr}:" + 80 + "/" + fname;
        handler.set(LoadHandler.Keys.Filename, fname);
        handler.set(LoadHandler.Keys.URL, downloadUrl);
        handler.set(LoadHandler.Keys.RequestID, getRandomHexString(32));
        int result = JOptionPane.showConfirmDialog(this, "Do you want to start printing after uploading the file?", "File Upload",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);
        if (result == JOptionPane.YES_OPTION) {
            wait.setVisible(true);
            select.setEnabled(false);
            send.setEnabled(false);
            print.set(handler);
            printTimer = new Timer(10000, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    printStage2.set(print.getAndSet(null));
                }
            });
            printTimer.setRepeats(false);
            printTimer.start();
        }
        server.setFileLoad(handler);
    }//GEN-LAST:event_sendActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel cFilename;
    private javax.swing.JLabel curJob;
    private javax.swing.JLabel currentLayer;
    private javax.swing.JLabel error;
    private javax.swing.JLabel filemd5;
    private javax.swing.JLabel filename;
    private javax.swing.JLabel filesize;
    private javax.swing.JLabel firmware;
    private javax.swing.JLabel httpDir;
    private javax.swing.JLabel httpPort;
    private javax.swing.JLabel img;
    private javax.swing.JLabel ip;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JLabel labCom;
    private javax.swing.JLabel model;
    private javax.swing.JLabel name;
    private javax.swing.JProgressBar progressLayers;
    private javax.swing.JProgressBar progressTime;
    private javax.swing.JLabel resolution;
    private javax.swing.JButton select;
    private javax.swing.JButton send;
    private javax.swing.JLabel status;
    private javax.swing.JLabel totJob;
    private javax.swing.JLabel totalLayers;
    private javax.swing.JLabel wait;
    // End of variables declaration//GEN-END:variables

    public static String getRandomHexString(int numchars) {
        Random r = new Random();
        StringBuilder sb = new StringBuilder();
        while (sb.length() < numchars) {
            sb.append(Integer.toHexString(r.nextInt()));
        }

        return sb.toString().substring(0, numchars);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        labCom.setText("  Online  ");
        labCom.setBackground(Color.green);
        commTimer.restart();
        StatusRoot statusRoot = server.getStatus();
        if (statusRoot != null) {
            if (statusRoot.data.status.currentStatus == 0) {
                status.setText("Not printing.");
            } else {
                status.setText("Printing.");
            }
            cFilename.setText(statusRoot.data.status.printInfo.filename);
            currentLayer.setText("" + statusRoot.data.status.printInfo.currentLayer);
            totalLayers.setText("" + statusRoot.data.status.printInfo.totalLayer);
            progressLayers.setMaximum(statusRoot.data.status.printInfo.totalLayer);
            progressLayers.setValue(statusRoot.data.status.printInfo.currentLayer);
            progressTime.setMaximum(statusRoot.data.status.printInfo.totalTicks);
            progressTime.setValue(statusRoot.data.status.printInfo.currentTicks);
            totJob.setText(TimeUtils.toFormattedString(statusRoot.data.status.printInfo.totalTicks, TimeUnit.MILLISECONDS));
            curJob.setText(TimeUtils.toFormattedString(statusRoot.data.status.printInfo.currentTicks, TimeUnit.MILLISECONDS));
            error.setText("" + statusRoot.data.status.printInfo.errorNumber);
            filename.setText(xfilename);
            filemd5.setText(xchecksum);
            filesize.setText("" + xfilesize);
            LoadHandler handler = printStage2.getAndSet(null);
            if (handler != null) {
                server.setPrint(handler);
                wait.setVisible(false);
                select.setEnabled(true);
                send.setEnabled(true);
            }

        }
    }
}
