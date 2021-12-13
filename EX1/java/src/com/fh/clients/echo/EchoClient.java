//
// Created by milo on 09/12/2021.
//

package com.fh.clients.echo;

import java.io.*;
import java.net.*;

public class EchoClient {
    Socket echoSocket = null;
    PrintWriter out = null;
    BufferedReader in = null;

    public void setupConnection(String[] _args, String serverIP) {
        if (_args.length > 0) {
            serverIP = _args[0];
        }

        System.out.println("Trying to connect to host: " + serverIP + ":3001.");

        try {
            echoSocket = new Socket(serverIP, 3001);
            out = new PrintWriter(echoSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(echoSocket.getInputStream()));
        } catch (UnknownHostException e) {
            System.err.println("Don't know about host: " + serverIP);
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for " + "the connection to: " + serverIP);
            System.exit(1);
        }
    }

    public void startRequest() throws IOException {
        BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));

        String userInput;
        System.out.print("input: ");
        while ((userInput = stdIn.readLine()) != null) {
            out.println(userInput);

            if (userInput.equals("shutdown")) {
                System.out.print("Server shutting down -> Closing client connection: ");
                break;
            }

            System.out.println(in.readLine());
            System.out.print("input: ");
        }

        out.close();
        in.close();
        stdIn.close();
        echoSocket.close();
    }
}
