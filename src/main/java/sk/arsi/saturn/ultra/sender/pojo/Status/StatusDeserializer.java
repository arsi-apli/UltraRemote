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
package sk.arsi.saturn.ultra.sender.pojo.Status;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import java.io.IOException;

/**
 *
 * @author arsi
 */
public class StatusDeserializer extends StdDeserializer<StatusRoot> {

    public StatusDeserializer() {
        this(null);
    }

    public StatusDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public StatusRoot deserialize(JsonParser jp, DeserializationContext dc) throws IOException, JsonProcessingException {
        StatusRoot root = new StatusRoot();
        try {
            JsonNode node = jp.getCodec().readTree(jp);
            root.id = node.get("Id").asText();
            node = node.get("Data");
            root.data = new Data();
            root.data.mainboardID = node.get("MainboardID").asText();
            root.data.timeStamp = node.get("TimeStamp").asInt();
            node = node.get("Status");
            root.data.status = new Status();
            root.data.status.currentStatus = node.get("CurrentStatus").asInt();
            root.data.status.previousStatus = node.get("PreviousStatus").asInt();
            root.data.status.printInfo = new PrintInfo();
            JsonNode n = node.get("PrintInfo");
            root.data.status.printInfo.currentLayer = n.get("CurrentLayer").asInt();
            root.data.status.printInfo.currentTicks = n.get("CurrentTicks").asInt();
            root.data.status.printInfo.errorNumber = n.get("ErrorNumber").asInt();
            root.data.status.printInfo.status = n.get("Status").asInt();
            root.data.status.printInfo.totalLayer = n.get("TotalLayer").asInt();
            root.data.status.printInfo.totalTicks = n.get("TotalTicks").asInt();
            root.data.status.printInfo.filename = n.get("Filename").asText();
            n = node.get("FileTransferInfo");
            root.data.status.fileTransferInfo = new FileTransferInfo();
            root.data.status.fileTransferInfo.checkOffset = n.get("CheckOffset").asInt();
            root.data.status.fileTransferInfo.downloadOffset = n.get("DownloadOffset").asInt();
            root.data.status.fileTransferInfo.fileTotalSize = n.get("FileTotalSize").asInt();
            root.data.status.fileTransferInfo.status = n.get("Status").asInt();
            root.data.status.fileTransferInfo.filename = n.get("Filename").asText();

            System.out.println("sk.arsi.saturn.ultra.sender.pojo.Status.StatusDeserializer.deserialize()");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return root;
    }

}
