_____________________________________________
Network ID: ocuNET
Network Password: OcutherapyAtYorkU2018
_____________________________________________
Device Login: pi
Device Password: luca416
Static IP Address: 192.168.4.1
_____________________________________________
Plug-in the Raspberry Pi and wait 30 seconds
to allow server to configure.
_____________________________________________
SSH Into Pi And Run Server

  1. Connect to ocuNET
  2. Enter WiFi password: OcutherapyAtYorkU2018
  2. Install/Open putty
  3. Enter Static IP address: 192.168.4.1
  4. Port Number: 22
  5. Press Enter
  4. login as: pi
  5. pi@192.168.4.1's password: luca416
_____________________________________________
Once you have successfully SSH'd into the 
Raspberry Pi you must locate and run the 
server file.

  1. cd Desktop    
  2. cd Ocutherapy
  3. java OcuServer  
  
You may stop the server by using the keyboard 
shortcut (ctrl + c).
_____________________________________________
The server will now be open to receive
data through its static ip address at port 
8080. 
 
 ---> 192.168.4.1:8080

You may change the port if desired by typing:

    java OcuServer [port number] 
______________________________________________
When turning off the Raspberry Pi please use 
the following command:

---> sudo shutdown now

wait 30 seconds, then unplug the cable.
_____________________________________________
To send data to the server, please look at 
postMessageJSON.cs.

The api is as follows:
 ---> PostRequest(string url, string id, 
                  string fileName, string json)

IMPORTANT: url must be the following:
--->  [http://192.168.4.1:8080/]

When a successful post request is made a 
message will appear on the terminal.
______________________________________________
If desired, you may check the contents of the
folder where the data is being stored on the
pi by connecting a device to ocuNET, opening a
browser and typing the following:

  --> http://192.168.4.1:8080/
______________________________________________
If connecting to the network with a phone 
ensure that your mobile network is turned off.

Ive included a sample apk in this respository
that sends a simple post request to the server.
_______________________________________________


