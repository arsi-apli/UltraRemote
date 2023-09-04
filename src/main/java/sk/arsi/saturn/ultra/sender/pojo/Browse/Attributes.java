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
import java.util.ArrayList;

public class Attributes {
    public String name;
    public String machineName;
    public String protocolVersion;
    public String firmwareVersion;
    public String resolution;
    public String mainboardIP;
    public String mainboardID;
    public int sDCPStatus;
    public String localSDCPAddress;
    public String sDCPAddress;
    public ArrayList<String> capabilities;
}
