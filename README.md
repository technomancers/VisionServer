# VisionServer

This is meant to run on a Raspberry Pi to do vision processing. It processes the images and puts values on the Network Table to be used by robot code.

## Important

As this project uses native code for your operating system (pre-compiled code specific to your OS), you will need to run `./gradlew clean` before you run/build for a different OS. If you do not, then gradle will not grab the appropriate files for the target OS.

The following has only been tested on Linux operating systems and the Raspberry Pi.

The tested Raspberry Pi image is [Raspbian Stretch Lite](https://www.raspberrypi.org/downloads/raspbian/).

## How to test locally

Running `./gradlew run` will try and run everything for the Raspberry Pi not your local machine. To change, append `-PbuildType=<PLATFORM>` where `<PLATFORM>` is one of the [Buid Types](#build-types).

You may need to run a Network Table server locally using [OutlineViewer](https://github.com/wpilibsuite/OutlineViewer). To tell your build/run to use a specific server, append `-PntServer=<HOST>` where `<HOST>` is the IP-Address or HostName of the server.

> By default, the network server is listening for RoboRIO on the default ports for team 1758. Meaning you do not need to specify the `ntServer` when deploying for robot use. 

Final command should look like `./gradlew run -PbuildType=linux -PntServer=192.168.0.100`.

## Setup Raspberry Pi for deployment

Run the following commands:

1. `ssh pi@<PIHOST>`
    - Login to the raspberry pi
    - Replace `<PIHOST>` with the Raspberry Pi's IP-Address or HostName
2. `touch VisionServer.service`
    - Create a file named `VisionServer.service`
3. `nano VisionServer.service`
    - Edit `VisionServer.service`
4. Copy the contents from [VisionServer.service](#visionserverservice)
5. Press `CTRL` + `x`
    - Attempt to exit `nano`
6. Press `y`
    - To write changes to the file
7. Press `ENTER`
    - To commit the change
8. `sudo ln -s /home/pi/VisionServer.service /etc/systemd/system/.`
    - Links `VisionServer.service` to the appropriate folder for `systemd` to see
9. `sudo systemctl daemon-reload`
    - Reload `systemd` to see the new file
10. `sudo systemctl enable VisionServer.service`
    - Enable `VisionServer` on system boot

> Service will not run correctly yet as we have not deployed anything to the Pi

### VisionServer.service

```ini
[Unit]
Description=Vision Processing
Requires=network-online.target

[Service]
ExecStart=/home/pi/VisionServer/bin/VisionServer

[Install]
WantedBy=multi-user.target
Alias=VisionServer
```

## Setup Your Machine for deployment

Run the following commands (in the root directory of the project):

1. `ssh-keygen -b 2048 -t rsa -f ./ssh/id_rsa`
    - Create a new rsa key pair
2. When asked for a passphrase just press `ENTER`. We do not want to set a password on these keys.
3. `ssh-copy-id -i ./ssh/id_rsa <PIUSER>@<PIHOST>`
    - Tell the pi about our new key. Will need to input the users password to upload to it.
    - `<PIUSER>` is usually `pi` but set it to your regular user that you use on the pi.
    - `<PIHOST>` this needs to be either the IP-Address or HostName of the raspberry pi.

## How to deploy to the Raspberry Pi

Run the following command:

1. `./gradlew deploy`

There are optional parameters you can pass in if the pi is not on host name `technopi.local`or has a user `pi`.

- `-PpiHost=<PIHOST>` - replace `<PIHOST>` with the HostName or IP-Address of the pi you want to deploy to.
    - Example `./gradlew deploy -PpiHost willspi.local`
- `-PpiUser=<PIUSER>` - replace `<PIUSER>` with the username used to get onto the pi.
    - Example `./gradlew deploy -PpiUser pi`

> The above options can be combined

## Build Types

| BuildType | Description |
| ---: | --- |
| `windows` | **Not Tested** Used to run/build for windows OS|
| `linux` | Used to run/build for x86/x86_64 linux OSs |
| `arm-raspbian` | Used specifically to run/build for the Rasberry Pi|
| `arm` | **Not Tested** Used to run/build for other Arm devices |
| `armhf` | **Not Tested** Used to run/build for Armhf devices (Beaglebone Black or Jetson) |
