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
4. `unzip VisionServer-<VERSION>.zip`
    - Decompress the package
5. `./VisionServer-<VERSION>/bin/VisionServer`
    - Run Vision Server
    - This will not keep running when you disconnect from the raspberry pi

## Build Types

| BuildType | Description |
| ---: | --- |
| `windows` | **Not Tested** Used to run/build for windows OS|
| `linux` | Used to run/build for x86/x86_64 linux OSs |
| `arm-raspbian` | Used specifically to run/build for the Rasberry Pi|
| `arm` | **Not Tested** Used to run/build for other Arm devices |
| `armhf` | **Not Tested** Used to run/build for Armhf devices (Beaglebone Black or Jetson) |

## TODO

- [ ] Have a way to Stop/Replace/Start the Server for deployment ([systemd](https://learn.adafruit.com/running-programs-automatically-on-your-tiny-computer/systemd-writing-and-enabling-a-service))
- [ ] Create a task to deploy to the Raspberry Pi