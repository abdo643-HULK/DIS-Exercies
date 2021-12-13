//
// Created by milo on 09/12/2021.
//

package com.fh.sensor;



import com.fh.EchoClient;

import java.io.*;
import java.net.*;

public class SensorClient_main {
    public static void main(String[] args) throws IOException {

        String serverIP = "127.0.0.1";

        EchoClient echoClient = new EchoClient();
        echoClient.setupConnection(args, serverIP);
        echoClient.startRequest();

    }
}


