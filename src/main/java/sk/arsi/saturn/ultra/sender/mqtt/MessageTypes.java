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

/**
 *
 * @author arsi
 */
public enum MessageTypes {
    Reserved0((short) 0),
    CONNECT((short) 1),
    CONNACK((short) 2),
    PUBLISH((short) 3),
    PUBACK((short) 4),
    PUBREC((short) 5),
    PUBREL((short) 6),
    PUBCOMP((short) 7),
    SUBSCRIBE((short) 8),
    SUBACK((short) 9),
    UNSUBSCRIBE((short) 10),
    UNSUBACK((short) 11),
    PINGREQ((short) 12),
    PINGRESP((short) 13),
    DISCONNECT((short) 14),
    Reserved15((short) 15),
    Unknown((short) 16),;

    private final short messageType;

    private MessageTypes(short messageType) {
        this.messageType = messageType;
    }

    public short getMessageId() {
        return messageType;
    }

    public static MessageTypes getMessageType(short messageType) {
        MessageTypes[] values = MessageTypes.values();
        for (MessageTypes value : values) {
            if (messageType == value.messageType) {
                return value;
            }
        }
        return Unknown;
    }

}
