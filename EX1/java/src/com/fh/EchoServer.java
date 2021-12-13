//
// Created by milo on 09/12/2021.
//

package com.fh;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.*;
import java.io.*;

public class EchoServer {

    ServerSocket serverSocket = null;
    Socket clientSocket = null;

    public void initializeSocker(int _port) {

        try {
            serverSocket = new ServerSocket(_port);
            System.err.println("Listening...");
        }
        catch (IOException e)
        {
            System.err.println("Could not listen on port: " + _port);
            System.exit(1);
        }


        System.out.println ("Waiting for connection.....");

        try {
            clientSocket = serverSocket.accept();
        }
        catch (IOException e)
        {
            System.err.println("Accept failed.");
            System.exit(1);
        }

        System.out.println ("Connection successful");
    }

    public void communication() throws IOException {

        System.out.println ("Waiting for input.....");

        PrintWriter out = new PrintWriter(clientSocket.getOutputStream(),
                true);
        BufferedReader in = new BufferedReader(
                new InputStreamReader( clientSocket.getInputStream()));

        String inputLine;

        while ((inputLine = in.readLine()) != null)
        {
            System.out.println ("Server: " + inputLine);
            out.println(inputLine);

            if (inputLine.equals("shutdown")) {
                System.out.print ("Server shutting down..");
                break;
            }

        }

        out.close();
        in.close();
        clientSocket.close();
        serverSocket.close();
    }

}
