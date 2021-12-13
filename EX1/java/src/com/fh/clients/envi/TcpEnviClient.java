//
// Created by milo on 09/12/2021.
//

package com.fh.clients.envi;


import java.io.*;
import java.net.*;
import java.util.Arrays;
import java.util.Objects;
import java.util.Scanner;


public class TcpEnviClient implements IEnvService {

    Socket socket = null;
    PrintWriter out = null;
    BufferedReader in = null;
    OutputStream out2 = null;

    public void setupConnection(String[] _args, String serverIP) {
        if (_args.length > 0) {
            serverIP = _args[0];
        }

        System.out.println("Trying to connect to host: " + serverIP + ":3001.");

        try {
            socket = new Socket(serverIP, 3001);
            out2 = socket.getOutputStream();
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (UnknownHostException e) {
            System.err.println("Don't know about host: " + serverIP);
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to: " + serverIP);
            System.exit(1);
        }
    }

    public void startRequest() throws IOException {
        Scanner in = new Scanner(System.in);
        int inputMenu;
        boolean exit = false;

        while (!exit) {
            do {
                System.out.println();
                System.out.println("Envi Client Menu:");
                System.out.println("------------------------:");
                System.out.println("1. Get sensor types:");
                System.out.println("2. Get sensor data:");
                System.out.println("3. Get all Sensor:");
                System.out.println("4. EXIT");
                System.out.println("------------------------:");

                inputMenu = in.nextInt();

            } while (inputMenu < 1 || inputMenu > 4);

            switch (inputMenu) {
                case 1 -> getSensorType();
                case 2 -> {
                    System.out.print("Please provide the sensor type: ");
                    String input = in.next();
                    getSensor(input);
                }
                case 3 -> getAllSensors();
                default -> exit = true;
            }

        }

        out.close();
        in.close();
        socket.close();

    }

    public void getSensorType() {
        String[] s = requestEnvironmentDataTypes();

        for (String str : s) {
            System.out.println(str);
        }

    }

    public void getAllSensors() {
        EnvData[] s = requestAll();

        int i = 0;
        for (EnvData str : s) {
            System.out.println("Timestamp: " + str.mTimeStamp + ", Sensor: " + str.mValues[0] + ", Value: " + str.mValues[1]);
            i += 2;
        }
    }

    public void getSensor(String _type) {
        EnvData s = requestEnvironmentData(_type);

        for (String str : s.mValues) {
            System.out.println("Timestamp: " + s.mTimeStamp + ", Value: " + str);
        }
    }


    @Override
    public String[] requestEnvironmentDataTypes() {
        String endpoint = "GET_SENSOR_TYPES";
        String input = null;

        try {

            out.printf(endpoint);
            input = in.readLine();

            if (Objects.equals(input, "NOT FOUND")) {
                System.err.println("ERROR FETCHING ENV DATA");
                return null;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        if (input == null) return null;
        return input.split(";");
    }

    @Override
    public EnvData requestEnvironmentData(String _type) {
        out.println(_type);
        EnvData envData = null;

        try {
            String input = in.readLine();

            if (Objects.equals(input, "NOT FOUND")) {
                System.err.println("ERROR FETCHING ENV DATA");
                return null;
            }

            String[] s = input.split("\\|");
            String timestamp = s[0];
            String data = s[1];

            envData = new EnvData(timestamp, data.split(";"));

        } catch (IOException e) {
            e.printStackTrace();
        }

        return envData;
    }

    @Override
    public EnvData[] requestAll() {
        String endpoint = "GET_ALL";
        out.println(endpoint);

        String input = "";
        EnvData[] envData = new EnvData[0];

        try {
            input = in.readLine();
            String[] s = input.split("\\|");

            if (Objects.equals(input, "NOT FOUND")) {
                System.err.println("ERROR FETCHING ENV DATA");
                return null;
            }

            String timestamp = s[0];
            String[] data = Arrays.copyOfRange(s, 1, s.length);
            envData = new EnvData[data.length];

            for (int i = 0; i < data.length; i++) {
                envData[i] = new EnvData(timestamp, data[i].split(";"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return envData;

    }
}