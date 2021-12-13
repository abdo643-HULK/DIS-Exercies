//
// Created by milo on 09/12/2021.
//

package com.fh;

import com.fh.clients.echo.EchoClient;
import com.fh.clients.envi.TcpEnviClient;

import java.io.*;
import java.util.Scanner;

public class Client_main {
    public static void main(String[] _args) throws IOException {
        String serverIP = "127.0.0.1";

        Scanner in = new Scanner(System.in);

        int inputMenu;
        do {

            System.out.println("Choose server type:");
            System.out.println("------------------------:");
            System.out.println("1. Echo Server:");
            System.out.println("2. Envi Client:");
            System.out.println("------------------------:");

            inputMenu = in.nextInt();
        } while (inputMenu < 1 || inputMenu > 2);

        switch (inputMenu) {
            case 1 -> {
                EchoClient echoClient = new EchoClient();
                echoClient.setupConnection(_args, serverIP);
                echoClient.startRequest();
            }
            case 2 -> {
                TcpEnviClient client = new TcpEnviClient();
                client.setupConnection(_args, serverIP);
                client.startRequest();
            }
            default -> System.out.println("Closing");
        }

        in.close();
    }
}
