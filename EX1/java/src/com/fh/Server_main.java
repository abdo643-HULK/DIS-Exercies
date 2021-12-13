//
// Created by milo on 09/12/2021.
//

package com.fh;

import java.net.*;
import java.io.*;

public class Server_main
{
    public static void main(String[] args) throws IOException
    {
        int _port = 3001;

        EchoServer echoServer = new EchoServer();
        echoServer.initializeSocker(_port);
        echoServer.communication();


    }
}