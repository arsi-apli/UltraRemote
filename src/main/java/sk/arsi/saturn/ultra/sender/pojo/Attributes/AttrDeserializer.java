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
package sk.arsi.saturn.ultra.sender.pojo.Attributes;

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
public class AttrDeserializer extends StdDeserializer<AttrRoot> {

    public AttrDeserializer() {
        this(null);
    }

    public AttrDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public AttrRoot deserialize(JsonParser jp, DeserializationContext dc) throws IOException, JsonProcessingException {
        AttrRoot root = new AttrRoot();
        JsonNode node = jp.getCodec().readTree(jp);
        root.id = node.get("Id").asText();
        root.data = new Data();
        node = node.get("Data");
        root.data.mainboardID = node.get("MainboardID").asText();
        root.data.timeStamp = node.get("TimeStamp").asInt();
        root.data.attributes = new Attributes();
		JsonNode attrs = node.get("Attributes");
        if (attrs == null) attrs = node.get("Status");
		if (attrs != null)
		{
			root.data.attributes.currentStatus = attrs.get("CurrentStatus").asInt();
			root.data.attributes.previousStatus = attrs.get("PreviousStatus").asInt();
			root.data.attributes.fileTransferInfo = new FileTransferInfo();
			JsonNode n = attrs.get("FileTransferInfo");
			root.data.attributes.fileTransferInfo.checkOffset = n.get("CheckOffset").asInt();
			root.data.attributes.fileTransferInfo.downloadOffset = n.get("DownloadOffset").asInt();
			root.data.attributes.fileTransferInfo.fileTotalSize = n.get("FileTotalSize").asInt();
			root.data.attributes.fileTransferInfo.status = n.get("Status").asInt();
			root.data.attributes.fileTransferInfo.filename = n.get("Filename").asText();
			n = attrs.get("PrintInfo");
			root.data.attributes.printInfo = new PrintInfo();
			root.data.attributes.printInfo.currentLayer = n.get("CurrentLayer").asInt();
			root.data.attributes.printInfo.currentTicks = n.get("CurrentTicks").asInt();
			root.data.attributes.printInfo.errorNumber = n.get("ErrorNumber").asInt();
			root.data.attributes.printInfo.status = n.get("Status").asInt();
			root.data.attributes.printInfo.totalLayer = n.get("TotalLayer").asInt();
			root.data.attributes.printInfo.totalTicks = n.get("TotalTicks").asInt();
			root.data.attributes.printInfo.filename = n.get("Filename").asText();
		}
        return root;
    }

}
