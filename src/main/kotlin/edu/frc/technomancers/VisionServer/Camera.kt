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

import edu.wpi.cscore.*
import org.opencv.core.Mat

class Camera(id:Int = 0, rawPort:Int = 1185, procPort:Int = 1186, width:Int = 640, height:Int = 480, fps:Int = 30) {
    private val sink:CvSink
    private val source:CvSource
    private val inputImage = Mat()
    init {
        val camera = setupCamera(id, rawPort)
        camera.setResolution(width, height)
        sink = setupCvSink(camera)
        source = setupCvSource(width, height, fps, procPort)
    }

    fun process(processor:(Mat) -> Mat){
        val frameTime = sink.grabFrame(inputImage)
        if (frameTime == 0.toLong()) return
        val outputImage = processor(inputImage)
        source.putFrame(outputImage)
    }

    //Create a new camera with device ID and have a server on port
    private fun setupCamera(id:Int, port:Int): UsbCamera {
        val inputStream = MjpegServer("MJPEG Server", port)
        val camera = UsbCamera("CoprocessorCamera", id)
        inputStream.source = camera
        return camera
    }

    //Get a way to grab the image from the camera
    private fun setupCvSink(camera: UsbCamera): CvSink {
        val imageSink = CvSink("CV Image Grabber")
        imageSink.source = camera
        return imageSink
    }

    //Get a way to put processed images to a server on port
    private fun setupCvSource(width:Int, height:Int, fps:Int, port:Int): CvSource {
        val imageSource = CvSource("CV Image Source", VideoMode.PixelFormat.kMJPEG, width, height, fps)
        val cvStream = MjpegServer("CV Image Stream", port)
        cvStream.source = imageSource
        return imageSource
    }
}