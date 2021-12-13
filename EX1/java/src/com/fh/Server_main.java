//
// Created by milo on 09/12/2021.
//

package com.fh;

import com.fh.servers.EchoServer;

import java.io.*;

public class Server_main
{
    public static void main(String[] _args) throws IOException
    {
        int _port = 3001;

        EchoServer echoServer = new EchoServer();
        echoServer.initializeSocket(_port);
        echoServer.communication();
    }
}