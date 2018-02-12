package com.example.android.basicgesturedetect;

import android.content.Context;
import android.net.DhcpInfo;
import android.net.wifi.WifiManager;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 462904 on 10/03/2016.
 */
public class ConnectToServer {

    public static InetAddress SERVER_IP;

    private static Context sContext;

    static void FindServerName() throws IOException {
        int port = 5120;
        DatagramSocket packetUdp = new DatagramSocket(port);
        packetUdp.setBroadcast(true);
        byte[] b = "83hcX1".getBytes("UTF-8");
        DatagramPacket outgoing = new DatagramPacket(b, b.length, getBroadcastAddress(ApplicationContentProvidor.getContext()), port);
        packetUdp.send(outgoing);

        //Recieve UDP packet
        boolean go = false;
        while (!go) {
            byte[] buffer = new byte[1024];
            DatagramPacket incoming = new DatagramPacket(buffer, buffer.length);
            packetUdp.receive(incoming);
            String message = new String(incoming.getData(), 0, incoming.getLength(), "UTF-8");
            if (message.equals("COMAH")) {
                go = true;
                SERVER_IP = incoming.getAddress();
            }
        }
    }

    static InetAddress getBroadcastAddress(Context context) throws IOException {
        WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        DhcpInfo dhcp = wifi.getDhcpInfo();
        if (dhcp == null) {
            return null;
        }
        int broadcast = (dhcp.ipAddress & dhcp.netmask) | ~dhcp.netmask;
        byte[] quads = new byte[4];
        for (int k = 0; k < 4; k++)
            quads[k] = (byte) ((broadcast >> k * 8) & 0xFF);
        return InetAddress.getByAddress(quads);
    }
}
