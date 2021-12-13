//
// Created by milo on 09/12/2021.
//

package com.fh;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Client_main {
    public static void main(String[] args) throws IOException {

        String serverIP = "127.0.0.1";

        Scanner in = new Scanner(System.in);
        int inputMenu = 0;

        while(true) {
            do {

                System.out.println("Choose server type:");
                System.out.println("------------------------:");
                System.out.println("1. Echo Server:");
                System.out.println("2. Envi Client:");
                System.out.println("------------------------:");

                inputMenu = in.nextInt();



            } while (inputMenu != 1 || inputMenu != 2);

            switch(inputMenu) {
                case 1:
                    EchoClient echoClient = new EchoClient();
                    echoClient.setupConnection(args, serverIP);
                    echoClient.startRequest();
                    break;
                case 2:

                    break;
                default:
                    break;
            }

        }





    }
}
