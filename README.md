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


## How to deploy to the Raspberry Pi

Currently, there is no `deployPi` task that would do all the magic so things need to be done manually. Maybe in the near future we can add that.

Run the following commands:

1. `./gradlew distZip`
    - Creates a zip file of all the necessary files and libraries
2. `scp build/distributions/VisionServer-<VERSION>.zip pi@<PIHOST>:~/.`
    - This will copy the created zip to the raspberry pi in the pi users home directory
    - Replace `<VERSION>` with the version found in `build.gradle`
    - Replace `<PIHOST>` with the Raspberry Pi's IP-Address or HostName
3. `ssh pi@<PIHOST>`
    - Login to the raspberry pi
4. `sudo systemctl stop VisionServer`
    - Stop the currently running server if there is one
5. `unzip VisionServer-<VERSION>.zip`
    - Decompress the package
6. `ln -sf VisionServer-<VERSION> VisionServer`
    - Link the new VisionServer to where our service can see it
7. `sudo systemctl start VisionServer`
    - Start the server back up

> If `<VERSION>` was not changed then step six is not needed but it would not hurt it.

## Build Types

| BuildType | Description |
| ---: | --- |
| `windows` | **Not Tested** Used to run/build for windows OS|
| `linux` | Used to run/build for x86/x86_64 linux OSs |
| `arm-raspbian` | Used specifically to run/build for the Rasberry Pi|
| `arm` | **Not Tested** Used to run/build for other Arm devices |
| `armhf` | **Not Tested** Used to run/build for Armhf devices (Beaglebone Black or Jetson) |

## TODO

- [ ] Create a task to deploy to the Raspberry Pi
