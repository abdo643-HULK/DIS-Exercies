//
// Created by milo on 09/12/2021.
//

package com.fh;

import com.fh.servers.EchoServer;

import java.io.*;

/**
 * The ServerMain simply contains a main method
 * where the server side will be executed.
 */
public class ServerMain
{
    /**
     * main method for executing Client side.
     *
     * @param _args contains the server IP address and server port
     * @throws IOException
     */
    public static void main(String[] _args) throws IOException
    {
        int _port = 3001;

        EchoServer echoServer = new EchoServer();
        echoServer.initializeSocket(_port);
        echoServer.communication();
    }
}