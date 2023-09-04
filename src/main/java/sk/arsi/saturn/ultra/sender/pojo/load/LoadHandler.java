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

package sk.arsi.saturn.ultra.sender.pojo.load;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author arsi
 */
public class LoadHandler {

    private final Map<String, String> data = new HashMap<>();

    private static final String JSON = "{\n"
            + "    \"Data\": {\n"
            + "        \"Cmd\": 256,\n"
            + "        \"Data\": {\n"
            + "            \"Check\": -Check-,\n"
            + "            \"CleanCache\": -CleanCache-,\n"
            + "            \"Compress\": -Compress-,\n"
            + "            \"FileSize\": -FileSize-,\n"
            + "            \"Filename\": \"-Filename-\",\n"
            + "            \"MD5\": \"-MD5-\",\n"
            + "            \"URL\": \"-URL-\"\n"
            + "        },\n"
            + "        \"From\": 0,\n"
            + "        \"MainboardID\": \"-MainboardID-\",\n"
            + "        \"RequestID\": \"-RequestID-\",\n"
            + "        \"TimeStamp\": -TimeStamp-\n"
            + "    },\n"
            + "    \"Id\": \"-Id-\"\n"
            + "}";

    public enum Keys {
        Check("-Check-", "0"),
        CleanCache("-CleanCache-", "1"),
        Compress("-Compress-", "0"),
        FileSize("-FileSize-", "0"),
        Filename("-Filename-", "file.goo"),
        MD5("-MD5-", "8ce58297c31d1b46592ce42662937eaf"),
        URL("-URL-", "http://${ipaddr}:26321/09471c770b704db5b0d0123b186e3eb2.goo"),
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

    public LoadHandler() {
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
//        "Cmd": 256,
//        "Data": {
//            "Check": 0,
//            "CleanCache": 1,
//            "Compress": 0,
//            "FileSize": 1895088,
//            "Filename": "_20x20x20.goo",
//            "MD5": "8ce58297c31d1b46592ce42662937eaf",
//            "URL": "http://${ipaddr}:26321/09471c770b704db5b0d0123b186e3eb2.goo"
//        },
//        "From": 0,
//        "MainboardID": "0001D270D7709C12",
//        "RequestID": "586de9f5a6334c84b9e9061e79303e39",
//        "TimeStamp": 1693595472915
//    },
//    "Id": "f25273b12b094c5a8b9513a30ca60049"
//}
//{
//    "Data": {
//        "Cmd": 256,
//        "Data": {
//            "Check": 0,
//            "CleanCache": 1,
//            "Compress": 0,
//            "FileSize": 6601157,
//            "Filename": "betacorrector_V5_lock_tight.goo",
//            "MD5": "c3d6e17bb8e80d089a687170b609520f",
//            "URL": "http://${ipaddr}:40183/betacorrector_V5_lock_tight.goo"
//        },
//        "From": 0,
//        "MainboardID": "0001D270D7709C12",
//        "RequestID": "d7ff262da27b7b069200b764be8fc81e",
//        "TimeStamp": 1693824334072
//    },
//    "Id": "f25273b12b094c5a8b9513a30ca60049"
//}
