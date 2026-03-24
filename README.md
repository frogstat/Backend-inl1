# Backendprogrammering Inlämning 1
This is a repo for an assignment using JPA and Apache Derby database.

This supports Linux and Windows (Powershell).

# How to Run
Ensure you have Maven installed.

Ensure that an Apache Derby database is up and running on localhost:50000

Linux:

&emsp;Start derby database with `./NetworkServerControl -p 50000 start` in your `derby/bin` folder.

&emsp;Then type the following commands in the project directory:

&emsp;`chmod u+x run.sh`

&emsp;`./run.sh`

Windows (Powershell):

&emsp;Ensure that maven is in your PATH.

&emsp;Start derby database with `.\NetworkServerControl -p 50000 start` in your `derby\bin` folder.

&emsp;open run.bat
