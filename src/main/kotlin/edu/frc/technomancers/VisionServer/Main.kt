/*
 * Copyright 2017 Technomancers
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

package edu.frc.technomancers.VisionServer

import edu.wpi.first.wpilibj.networktables.NetworkTable
import org.opencv.core.Core
import org.opencv.core.Mat
import org.opencv.core.Point
import org.opencv.core.Scalar
import org.opencv.imgproc.Imgproc

fun main(args: Array<String>) {
    System.loadLibrary(Core.NATIVE_LIBRARY_NAME)
    setNTServer()
    val camera = Camera()
    while (true) {
        camera.process(::processor)
    }
}

//Setup Network tables to communicate with the correct server
private fun setNTServer() {
    val ntIP = System.getProperty("networktable.server")
    NetworkTable.setClientMode()
    if (ntIP.isNullOrEmpty()) {
        NetworkTable.setTeam(1758)
    } else {
        NetworkTable.setIPAddress(ntIP)
    }
    NetworkTable.initialize()
}

private fun processor(input: Mat): Mat {
    Imgproc.rectangle(input, Point(310.toDouble(), 230.toDouble()), Point(330.toDouble(), 250.toDouble()), Scalar(255.toDouble(), 0.toDouble(), 255.toDouble()))
    return input
}
