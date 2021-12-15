//
// Created by milo on 09/12/2021.
//

package com.fh.servers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * The EchoServer class waits for a message
 * and replies with the input after receiving one
 */
public class EchoServer {
    ServerSocket mServerSocket = null;
    Socket mClientSocket = null;

    /**
     * initializeSocket() waits on the server side
     * for a connection to be established
     *
     * @param _port contains the port of the server
     */
    public void initializeSocket(int _port) {
        try {
            mServerSocket = new ServerSocket(_port);
            System.err.println("Listening...");
        } catch (IOException e) {
            System.err.println("Could not listen on port: " + _port);
            System.exit(1);
        }

        System.out.println("Waiting for connection.....");

        try {
            mClientSocket = mServerSocket.accept();
        } catch (IOException e) {
            System.err.println("Accept failed.");
            System.exit(1);
        }

        System.out.println("Connection successful");
    }

    /**
     * communication() waits for a message
     * and replies with the input after receiving a message.
     *
     * @throws IOException
     */
    public void communication() throws IOException {
        System.out.println("Waiting for input.....");

        PrintWriter out = new PrintWriter(mClientSocket.getOutputStream(), true);
        BufferedReader in = new BufferedReader(new InputStreamReader(mClientSocket.getInputStream()));

        String inputLine;
        while ((inputLine = in.readLine()) != null) {
            System.out.println("Server: " + inputLine);
            out.println(inputLine);

            if (inputLine.equals("shutdown")) {
                System.out.print("Server shutting down..");
                break;
            }

        }

        out.close();
        in.close();
        mClientSocket.close();
        mServerSocket.close();
    }

}
