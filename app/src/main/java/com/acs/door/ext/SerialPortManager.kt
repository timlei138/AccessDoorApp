package com.acs.door.ext

import com.google.serialport.SerialPort
import java.io.File

object SerialPortManager {

    fun openSerialPort(){
        SerialPort(File(""),1,0)
    }
}