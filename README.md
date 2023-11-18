# UltraRemote
Wifi Remote control for Elegoo Saturn 3 Ultra

A command line utility that allows you to view the status of your Elegoo Saturn 3 Ultra printer over a Wifi connection, send files and start printing.

Btw. Here you can find the modified firmware with windows share and ssh support: https://github.com/arsi-apli/UltraFirmwareToolkit

Usage:

> java -jar UltraRemote-01.00-shaded.jar 

or 

> java -jar UltraRemote-01.00-shaded.jar "full_path_to.goo"

Attention! If the file name contains a space, use quotation marks.

If you have a problem connecting your printer, try this version:

> java -Djava.net.preferIPv4Stack=true -jar UltraRemote-01.00-shaded.jar full_path_to.goo

Lychee 3D Slicer:

File->Preferences

<img src="https://raw.githubusercontent.com/arsi-apli/UltraRemote/master/img/lychee1.png"></a>


External tools->User command line:

java -jar full_path_to_UltraRemote-01.00-shaded.jar "((file))"

Attention! The quotation marks are necessary, if the filename contains a space it will not work without the use of quotation marks!


![obrázok](https://github.com/arsi-apli/UltraRemote/assets/22594510/c7cdf3fc-4662-4883-805c-e6ede6250e4f)




Printer browser:

<img src="https://raw.githubusercontent.com/arsi-apli/UltraRemote/master/img/browser.png"></a>

File upload:

<img src="https://raw.githubusercontent.com/arsi-apli/UltraRemote/master/img/upload.png"></a>

Printing:

<img src="https://raw.githubusercontent.com/arsi-apli/UltraRemote/master/img/detail.png"></a>



