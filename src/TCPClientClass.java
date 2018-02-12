package com.example.android.basicgesturedetect;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

/**
 * Created by 462904 on 27/06/2016.
 */
public class TCPClientClass extends AsyncTask<String, Void, String> {
    //AUDITORIUM"150.237.96.16"
    //2120
    //BOX"150.237.94.47"
    public static String SERVERIP = "150.237.94.47";
    public static int SERVERPORT = 2120;
    public boolean mRun = true;
    public static Socket socket;
    public static float vX;
    public static float vY;
    PrintWriter out;
    BufferedReader in;
    public static byte[] message;
    public static int speedValue = 100;

    public static boolean sending = false;
    private boolean receivingData = true;
    private boolean initialising = true;
    private boolean clientConnected = false;
    private int count = 0;

    public static ArrayList<String> temporaryAddress = new ArrayList<String>();
    private Socket clientSocket;
    private ServerSocket serverSocket;

    TCPClientClass() {
        SERVERIP = "150.237.94.47";
        SERVERPORT = 2120;
    }

    TCPClientClass(String IP, Integer port) {
        SERVERIP = IP;
        SERVERPORT = port;
    }

    @Override
    protected String doInBackground(String... params) {
        try {
            InetAddress serverAddr = InetAddress.getByName(SERVERIP);
            android.util.Log.e("TCP Client", "C: Connecting...");
            socket = new Socket(serverAddr, SERVERPORT);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        while (mRun) {

            if (receivingData) {

                while (initialising) {

                    try {
                        serverSocket = new ServerSocket(3120);
                        clientSocket = serverSocket.accept();
                        clientConnected = true;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    if (clientConnected) {
                        try {

                            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

                            String message = in.readLine();
                            byte[] msg = message.getBytes();

                            RecieveData(message, msg);
                            initialising = false;

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }

                if (sending) {
                    sending = false;
                    android.util.Log.e("TCP Client", "C: Try loop...");
                    DataOutputStream dOut = null;
                    try {
                        dOut = new DataOutputStream(socket.getOutputStream());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    try {
                        dOut.writeBytes(new String(message));
                        dOut.flush();
                        String msg = message.toString();

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        return null;
    }

    private static void SplitMessage(String s) {

        int addressIndex = 0;
        int address2Index = 0;
        int townIndex = 0;
        int countyIndex = 0;
        int postcodeIndex = 0;
        int latIndex = 0;
        int longIndex = 0;
        int endIndex = 0;

        String address = "";
        String address2 = "";
        String town = "";
        String county = "";
        String postcode = "";
        String latitude = "";
        String longitude = "";
        String end = "";

        //Split up individual messages
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == '|')
                addressIndex = i;

            if (s.charAt(i) == '^')
                address2Index = i;

            if (s.charAt(i) == '/')
                townIndex = i;

            if (s.charAt(i) == '*')
                countyIndex = i;

            if (s.charAt(i) == '&')
                postcodeIndex = i;

            if (s.charAt(i) == '?')
                latIndex = i;

            if (s.charAt(i) == '=')
                longIndex = i;

            if (s.charAt(i) == '!')
                endIndex = i;
        }

        for (int i = 0; i < s.length(); i++) {
            if (i > addressIndex && i < address2Index)
                address += s.charAt(i);

            if (i > address2Index && i < townIndex)
                address2 += s.charAt(i);

            if (i > townIndex && i < countyIndex)
                town += s.charAt(i);

            if (i > countyIndex && i < postcodeIndex)
                county += s.charAt(i);

            if (i > postcodeIndex && i < latIndex)
                postcode += s.charAt(i);

            if (i > latIndex && i < longIndex)
                latitude += s.charAt(i);

            if (i > longIndex && i < endIndex)
                longitude += s.charAt(i);
        }

        message = null;
        GestureListener.addresses.add(new Address(address, address2, town, county, postcode, latitude, longitude));
    }

    public static void RecieveData(String message, byte[] msg) {
        //HERE SPLIT THE MESSAGE INTO SEPERATE MESSAGES THEN MAKE THIS FOR LOOP A FUNCTION WHICH WILL ADD THE DATA TO RELEVANT ADDRESS
        ArrayList<Integer> chars = new ArrayList<Integer>();
        String find = "!";

        int addressLength = 0;

        if (Character.isDigit(message.charAt(1))) {
            addressLength = Integer.parseInt(String.valueOf(message.charAt(0) + String.valueOf(message.charAt(1))));

            if (Character.isDigit(message.charAt(2))) {
                addressLength = Integer.parseInt(String.valueOf(message.charAt(0) + String.valueOf(message.charAt(1) + String.valueOf(message.charAt(2)))));
            }
        } else {
            addressLength = Integer.parseInt(String.valueOf(message.charAt(0)));
        }


        for (int i = 0; i < message.length(); i++) {
            if (message.charAt(i) == '|')
                chars.add(i);
        }

        //Adds individual messages to a list
        for (int i = 0, z = 1; i <= addressLength - 1; i++, z++) {
            if (i != addressLength - 1)
                temporaryAddress.add(new String(message.substring(chars.get(i), chars.get(z))));

            else
                temporaryAddress.add(new String(message.substring(chars.get(i), message.length())));
        }

        for (int i = 0; i < temporaryAddress.size(); i++) {
            SplitMessage(temporaryAddress.get(i));
        }

        msg = null;
    }

    public static void SendMessage(String eventMessage, String vX, String vY, String diffX, String diffY) throws InterruptedException {

        if (eventMessage.contains("Move")) {
            Thread.sleep(speedValue);
        }

        String msg = (eventMessage + "^VelocityX=" + vX + "<" + "VelocityY!" + vY + "/" + diffX + ">" + diffY + "|");
        message = msg.getBytes();
    }

    //public static void SendMessage(String eventMessage) {
    //String msg = (eventMessage);
    //message = msg.getBytes();
    //}

    public static String GetMessage(byte[] event) throws IOException {
        //if (event.toString() == "Swipe Up") {
        //DataInputStream is = new DataInputStream(socket.getInputStream());
        //DataOutputStream dOut = new DataOutputStream((socket.getOutputStream()));
        //dOut.writeByte(1);
        //dOut.writeUTF("Swipe Up");
        //dOut.flush();
        //message = event;
        //}

        return event.toString();
    }
}