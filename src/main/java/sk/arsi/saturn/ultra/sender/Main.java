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

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import javax.swing.JFrame;

/**
 *
 * @author arsi
 */
public class Main {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        String filename = null;
        if (args.length > 0) {
            File f = new File(args[0]);
            if (f.exists()) {
                filename = args[0];
            }
        }
        JFrame frame = new JFrame("UltraRemote v1.0.0 by ArSi");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(720, 800);
        frame.setContentPane(new PrinterBrowser(frame, filename));
        frame.setVisible(true);

    }

}
