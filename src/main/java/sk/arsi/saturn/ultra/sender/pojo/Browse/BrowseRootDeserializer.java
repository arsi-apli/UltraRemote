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
package sk.arsi.saturn.ultra.sender.pojo.Browse;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

/**
 *
 * @author arsi
 */
public class BrowseRootDeserializer extends StdDeserializer<BrowseRoot> {

    public BrowseRootDeserializer() {
        this(null);
    }

    public BrowseRootDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public BrowseRoot deserialize(JsonParser jp, DeserializationContext dc) throws IOException, JsonProcessingException {
        BrowseRoot root = new BrowseRoot();
        JsonNode node = jp.getCodec().readTree(jp);
        root.id = node.get("Id").asText();
        node = node.get("Data");
        root.data = new Data();
        //
        JsonNode attrNode = node.get("Attributes");
        root.data.attributes = new Attributes();
        root.data.attributes.firmwareVersion = attrNode.get("FirmwareVersion").asText();
        root.data.attributes.localSDCPAddress = attrNode.get("LocalSDCPAddress").asText();
        root.data.attributes.machineName = attrNode.get("MachineName").asText();
        root.data.attributes.mainboardID = attrNode.get("MainboardID").asText();
        root.data.attributes.mainboardIP = attrNode.get("MainboardIP").asText();
        root.data.attributes.name = attrNode.get("Name").asText();
        root.data.attributes.protocolVersion = attrNode.get("ProtocolVersion").asText();
        root.data.attributes.resolution = attrNode.get("Resolution").asText();
        root.data.attributes.sDCPAddress = attrNode.get("SDCPAddress").asText();
        root.data.attributes.sDCPStatus = attrNode.get("SDCPStatus").asInt();
        root.data.attributes.capabilities = new ArrayList<>();
        Iterator<JsonNode> elements = attrNode.get("Capabilities").elements();
        while (elements.hasNext()) {
            JsonNode next = elements.next();
            root.data.attributes.capabilities.add(next.asText());
        }
        //
        JsonNode statusNode = node.get("Status");
        root.data.status = new Status();
        root.data.status.currentStatus = statusNode.get("CurrentStatus").asInt();
        root.data.status.previousStatus = statusNode.get("PreviousStatus").asInt();
        root.data.status.printInfo = new PrintInfo();
        JsonNode n = statusNode.get("PrintInfo");
        root.data.status.printInfo.currentLayer = n.get("CurrentLayer").asInt();
        root.data.status.printInfo.currentTicks = n.get("CurrentTicks").asInt();
        root.data.status.printInfo.errorNumber = n.get("ErrorNumber").asInt();
        root.data.status.printInfo.status = n.get("Status").asInt();
        root.data.status.printInfo.totalLayer = n.get("TotalLayer").asInt();
        root.data.status.printInfo.totalTicks = n.get("TotalTicks").asInt();
        root.data.status.printInfo.filename = n.get("Filename").asText();
        n = statusNode.get("FileTransferInfo");
        root.data.status.fileTransferInfo = new FileTransferInfo();
        root.data.status.fileTransferInfo.checkOffset = n.get("CheckOffset").asInt();
        root.data.status.fileTransferInfo.downloadOffset = n.get("DownloadOffset").asInt();
        root.data.status.fileTransferInfo.fileTotalSize = n.get("FileTotalSize").asInt();
        root.data.status.fileTransferInfo.status = n.get("Status").asInt();
        root.data.status.fileTransferInfo.filename = n.get("Filename").asText();
        return root;
    }

}
