export GRAALVM_HOME=/java/bellsoft-liberica-vm-openjdk17-22.3.1
/java/bellsoft-liberica-vm-openjdk17-22.3.1/bin/native-image -Djava.awt.headless=false  -jar /projekty/SaturnSender/target/SaturnSender-01.00-shaded.jar --no-fallback --enable-http
