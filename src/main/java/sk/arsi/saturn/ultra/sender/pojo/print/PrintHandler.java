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
package sk.arsi.saturn.ultra.sender.pojo.print;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author arsi
 */
public class PrintHandler {

    private final Map<String, String> data = new HashMap<>();
    private static final String JSON = "{\n"
            + "    \"Data\": {\n"
            + "        \"Cmd\": 128,\n"
            + "        \"Data\": {\n"
            + "            \"Filename\": \"-Filename-\",\n"
            + "            \"StartLayer\": 0\n"
            + "        },\n"
            + "        \"From\": 0,\n"
            + "        \"MainboardID\": \"-MainboardID-\",\n"
            + "        \"RequestID\": \"-RequestID-\",\n"
            + "        \"TimeStamp\": -TimeStamp-\n"
            + "    },\n"
            + "    \"Id\": \"-Id-\"\n"
            + "}";

    public enum Keys {
        Filename("-Filename-", "file.goo"),
        MainboardID("-MainboardID-", "0001D270D7709C12"),
        RequestID("-RequestID-", "586de9f5a6334c84b9e9061e79303e39"),
        TimeStamp("-TimeStamp-", "1693595472915"),
        Id("-Id-", "f25273b12b094c5a8b9513a30ca60049"),;
        private final String key;
        private final String defaultValue;

        private Keys(String key, String defaultValue) {
            this.key = key;
            this.defaultValue = defaultValue;
        }

        public String getKey() {
            return key;
        }

        public String getDefaultValue() {
            return defaultValue;
        }

    }

    public PrintHandler() {
        Keys[] values = Keys.values();
        for (Keys value : values) {
            data.put(value.key, value.defaultValue);
        }
    }

    public void set(Keys key, String value) {
        data.put(key.key, value);
    }

    public String get(Keys key) {
        return data.get(key.key);
    }

    public void clear() {
        data.clear();
        Keys[] values = Keys.values();
        for (Keys value : values) {
            data.put(value.key, value.defaultValue);
        }
    }

    public String getJson() {
        String json = JSON;
        for (Map.Entry<String, String> entry : data.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            json = json.replace(key, value);
        }
        return json;
    }

    public byte[] getJsonBytes() {
        try {
            return getJson().getBytes("UTF-8");
        } catch (UnsupportedEncodingException ex) {
            return getJson().getBytes();
        }
    }
}

//{
//    "Data": {
//        "Cmd": 128,
//        "Data": {
//            "Filename": "_20x20x20.goo",
//            "StartLayer": 0
//        },
//        "From": 0,
//        "MainboardID": "0001D270D7709C12",
//        "RequestID": "1d46920dd3c24933b5e9253ae4eb7426",
//        "TimeStamp": 1693595483221
//    },
//    "Id": "f25273b12b094c5a8b9513a30ca60049"
//}
//{
//    "Data": {
//        "Cmd": 128,
//        "Data": {
//            "Filename": "_20x20x202.goo",
//            "StartLayer": 0
//        },
//        "From": 0,
//        "MainboardID": "0001D270D7709C12",
//        "RequestID": "ee1151fd882776435318f92238509621",
//        "TimeStamp": 1693833085978
//    },
//    "Id": "f25273b12b094c5a8b9513a30ca60049"
//}
//{
//    "Data": {
//        "Cmd": 64,
//        "Data": null,
//        "From": 0,
//        "MainboardID": "0001D270D7709C12",
//        "RequestID": "3fed91ad0706412c8b47d06ebc21713f",
//        "TimeStamp": 1693595483300
//    },
//    "Id": "f25273b12b094c5a8b9513a30ca60049"
//}
