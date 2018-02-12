package com.example.android.basicgesturedetect;

import android.net.DhcpInfo;
import android.net.wifi.WifiManager;

import com.example.android.common.logger.Log;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by 462904 on 05/05/2016.
 */
public class DiscoveryClient extends Thread {

    private static final String TAG = "Discovery";
    private static final String REMOTE_KEY = "b0xeeRem0tE!";
    private static final int DISCOVERY_PORT = 2562;
    private static final int TIMEOUT_MS = 500;


    public void run() {
        try {
            DatagramSocket socket = new DatagramSocket(5120, InetAddress.getByName("255.255.255.255"));
            socket.setBroadcast(true);
            System.out.println("Listen on " + socket.getLocalAddress() + " from " + socket.getInetAddress() + " port " + socket.getBroadcast());
            byte[] buff = new byte[512];
            DatagramPacket packet = new DatagramPacket(buff, buff.length);
            socket.receive(packet);
            String s = new String(packet.getData(), 0, packet.getLength());
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }








    /*private static final String TAG = "Discovery";
    private static final String REMOTE_KEY = "b0xeeRem0tE!";
    private static final int DISCOVERY_PORT = 2562;
    private static final int TIMEOUT_MS = 500;

    private static final String mChallenge = "myvoice";
    private WifiManager mWifi;


    interface DiscoveryReceiver {
        void addAnnouncedServers(InetAddress[] host, int port[]);
    }



    public void run() {
        try {
            DatagramSocket socket = new DatagramSocket(DISCOVERY_PORT);
            socket.setBroadcast(true);
            socket.setSoTimeout(TIMEOUT_MS);

            sendDiscoveryRequest(socket);
            listenForResponses(socket);
        } catch (IOException e) {
            Log.e(TAG, "Could not send discovery request", e);
        }
    }

    //Sends a braodcast UDP packet containing a request for the service to announce themselves
    private void sendDiscoveryRequest(DatagramSocket socket) throws IOException {
        String data = String.format("<bdp1 cmd=\"discover\" application=\"iphone_remote\" challenge=\"%s\" signature=\"%s\"/>", mChallenge, getSignature(mChallenge));
        Log.d(TAG, "Sending data " + data);
        DatagramPacket packet = new DatagramPacket(data.getBytes(), DISCOVERY_PORT);
        socket.send(packet);
    }

    //Calculate the broadcast IP we need to send the packet along, if we send to 255.255.255.255 it never gets sent, mobile network doesn't want to do broadcast
    private InetAddress getBroadcastAddress() throws IOException {
        DhcpInfo dhcp = mWifi.getDhcpInfo();
        if (dhcp == null) {
            Log.d(TAG, "Could not get dhcp info");
            return null;
        }

        int broadcast = (dhcp.ipAddress & dhcp.netmask) | ~dhcp.netmask;
        byte[] quads = new byte[4];

        for (int k = 0; k < 4; k++) {
            quads[k] = (byte) ((broadcast >> k * 8) & 0xFF);
        }

        return InetAddress.getByAddress(quads);
    }

    //Listen on socket for responses, timing out after TIMEOUT_MS
    private void listenForResponses(DatagramSocket socket) throws IOException {
        byte[] buf = new byte[1024];
        try {
            while (true) {
                DatagramPacket packet = new DatagramPacket(buf, buf.length);
                socket.receive(packet);
                String s = new String(packet.getData(), 0, packet.getLength());
                Log.d(TAG, "Received response " + s);
            }

        } catch (SocketTimeoutException e) {
            Log.d(TAG, "Recieve timed out");
        }
    }

    //Calculate the signature we need to send with the request. It is a string containing the hex md5sum of the challenge and REMOTE_KEY
    private String getSignature(String challenge) {
        MessageDigest digest;
        byte[] md5sum = null;
        try {
            digest = java.security.MessageDigest.getInstance("MD5");
            digest.update(challenge.getBytes());
            digest.update(REMOTE_KEY.getBytes());
            md5sum = digest.digest();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        StringBuffer hexString = new StringBuffer();
        for (int k = 0; k < md5sum.length; ++k) {
            String s = Integer.toHexString((int) md5sum[k] & 0xFF);

            if (s.length() == 1)
                hexString.append('0');

            hexString.append(s);
        }
        return hexString.toString();
    }*/
}
