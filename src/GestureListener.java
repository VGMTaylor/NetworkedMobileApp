/*
 * Copyright 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.basicgesturedetect;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.gesture.Gesture;
import android.graphics.Path;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v4.view.VelocityTrackerCompat;
import android.util.Xml;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.example.android.common.logger.Log;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import static android.app.PendingIntent.getActivity;

public class GestureListener extends GestureDetector.SimpleOnGestureListener
{
    public static final String TAG = "GestureListener";
    public static ArrayList<Actions> actions = new ArrayList<Actions>();
    public static ArrayList<Address> addresses = new ArrayList<Address>();
    public static int mouseSpeed;


    private static String[] addList;

    private static final int SWIPE_DISTANCE_THRESHOLD = 100;
    private static final int SWIPE_VELOCITY_THRESHOLD = 100;

    private int mActivePointerId;

    private static float x1;
    private static float y1;
    private static float x2;
    private static float y2;
    private static float prevX;
    private static float prevY;
    private static float velocityX;
    private static float velocityY;


    private VelocityTracker mVelocityTracker;

    public static void startServer() throws InterruptedException {
        new TCPClientClass().execute();
    }

    public static void startServer(String ip, int port) {
        new TCPClientClass(ip, port).execute();
    }

    public static void FindServer() throws IOException {

        ServerSocket serverSocket = new ServerSocket(2120);
        boolean connected = true;
        int count = 0;

        while (connected)
        {
            Socket socket = serverSocket.accept();
            count++;
        }

    }

    public static void onSearch(String input) throws InterruptedException {
        TCPClientClass.SendMessage("L0cation: " + input, String.valueOf(1f), String.valueOf(1f), String.valueOf(1f), String.valueOf(1f));
        TCPClientClass.sending = true;
    }

    public static void onSearchGPS(String lon, String lat) {
        try {
            TCPClientClass.SendMessage("GPS: ", String.valueOf(lat), String.valueOf(lon), String.valueOf(1f), String.valueOf(1f));
            TCPClientClass.sending = true;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void onInfo(String input) throws InterruptedException {
        TCPClientClass.SendMessage("Info: " + input, String.valueOf(1f), String.valueOf(1f), String.valueOf(1f), String.valueOf(1f));
        TCPClientClass.sending = true;
    }

    public static void onPOI(String input, String radius) throws InterruptedException {
        TCPClientClass.SendMessage("POI: " + input, String.valueOf(radius), String.valueOf(1f), String.valueOf(1f), String.valueOf(1f));
        TCPClientClass.sending = true;
    }

    public static void onZoom(Float input) throws InterruptedException {
        if (input > 0) {
            TCPClientClass.SendMessage("+Zoom: ", String.valueOf(1f), String.valueOf(1f), String.valueOf(1f), String.valueOf(1f));
        }

        else {
            TCPClientClass.SendMessage("-Zoom: " + input.toString(), String.valueOf(1f), String.valueOf(1f), String.valueOf(1f), String.valueOf(1f));
        }

        TCPClientClass.sending = true;
    }

    public static void onPlay() {
        try {
            TCPClientClass.SendMessage("Play:", String.valueOf(1f), String.valueOf(1f), String.valueOf(1f), String.valueOf(1f));
            TCPClientClass.sending = true;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void onPauseTimer() {
        try {
            TCPClientClass.SendMessage("Pause:", String.valueOf(1f), String.valueOf(1f), String.valueOf(1f), String.valueOf(1f));
            TCPClientClass.sending = true;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void incrementSpeed() {
        try {
            TCPClientClass.SendMessage("Increment:", String.valueOf(1f), String.valueOf(1f), String.valueOf(1f), String.valueOf(1f));
            TCPClientClass.sending = true;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void decrementSpeed() {
        try {
            TCPClientClass.SendMessage("Decrement:", String.valueOf(1f), String.valueOf(1f), String.valueOf(1f), String.valueOf(1f));
            TCPClientClass.sending = true;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    public static void onWindChange(double speed, double angle) throws InterruptedException {
        TCPClientClass.SendMessage("Wind:", String.valueOf(speed),String.valueOf(angle), String.valueOf(1f), String.valueOf(1f));
        TCPClientClass.sending = true;
    }

    public static void onRoadChange(int i) throws InterruptedException {
        if (i == 1) {
            TCPClientClass.SendMessage("Road:", String.valueOf(1f),String.valueOf(1f), String.valueOf(1f), String.valueOf(1f));
            TCPClientClass.sending = true;
        }

        else if (i == 2) {
            TCPClientClass.SendMessage("Satelite:", String.valueOf(1f),String.valueOf(1f), String.valueOf(1f), String.valueOf(1f));
            TCPClientClass.sending = true;
        }

        else if (i == 3) {
            TCPClientClass.SendMessage("Height:", String.valueOf(1f),String.valueOf(1f), String.valueOf(1f), String.valueOf(1f));
            TCPClientClass.sending = true;
        }
    }

    public static void onObjectPlace(int i, float radius) throws InterruptedException {
        if (i == 0){
            TCPClientClass.SendMessage("RVP:", String.valueOf(1f),String.valueOf(1f), String.valueOf(1f), String.valueOf(1f));
            TCPClientClass.sending = true;
        }

        if (i == 1){
            TCPClientClass.SendMessage("Cordon:", String.valueOf(1f),String.valueOf(1f), String.valueOf(1f), String.valueOf(1f));
            TCPClientClass.sending = true;
        }

        if (i == 2){
            TCPClientClass.SendMessage("Fire:", String.valueOf(1f),String.valueOf(1f), String.valueOf(1f), String.valueOf(1f));
            TCPClientClass.sending = true;
        }

        if (i == 3){
            TCPClientClass.SendMessage("Gas:", String.valueOf(1f),String.valueOf(1f), String.valueOf(1f), String.valueOf(1f));
            TCPClientClass.sending = true;
        }

        if (i == 5){
            TCPClientClass.SendMessage("POI:", String.valueOf(1f),String.valueOf(1f), String.valueOf(1f), String.valueOf(1f));
            TCPClientClass.sending = true;
        }

        if (i == 6){
            TCPClientClass.SendMessage("DNE:", String.valueOf(radius),String.valueOf(1f), String.valueOf(1f), String.valueOf(1f));
            TCPClientClass.sending = true;
        }

        if (i == 7){
            TCPClientClass.SendMessage("Service:", String.valueOf(1f),String.valueOf(1f), String.valueOf(1f), String.valueOf(1f));
            TCPClientClass.sending = true;
        }

        if (i == 8){
            TCPClientClass.SendMessage("Police:", String.valueOf(1f),String.valueOf(1f), String.valueOf(1f), String.valueOf(1f));
            TCPClientClass.sending = true;
        }
    }

    public static void OnMouseChange(int i) throws InterruptedException {
        if (i == 0) {
            TCPClientClass.SendMessage("Pan",  String.valueOf(1f),String.valueOf(1f), String.valueOf(1f), String.valueOf(1f));
            TCPClientClass.sending = true;
        }

        if (i == 1) {
            TCPClientClass.SendMessage("Place", String.valueOf(1f),String.valueOf(1f), String.valueOf(1f), String.valueOf(1f));
            TCPClientClass.sending = true;
        }

        if (i == 2) {
            TCPClientClass.SendMessage("Edit", String.valueOf(1f),String.valueOf(1f), String.valueOf(1f), String.valueOf(1f));
            TCPClientClass.sending = true;
        }

        if (i == 3) {
            TCPClientClass.SendMessage("Maneuver", String.valueOf(1f),String.valueOf(1f), String.valueOf(1f), String.valueOf(1f));
            TCPClientClass.sending = true;
        }

        if (i == 4) {
            TCPClientClass.SendMessage("Measure", String.valueOf(1f),String.valueOf(1f), String.valueOf(1f), String.valueOf(1f));
            TCPClientClass.sending = true;
        }

        if (i == 5) {
            TCPClientClass.SendMessage("Draw", String.valueOf(1f),String.valueOf(1f), String.valueOf(1f), String.valueOf(1f));
            TCPClientClass.sending = true;
        }

        if (i == 6) {
            TCPClientClass.SendMessage("Delete", String.valueOf(1f),String.valueOf(1f), String.valueOf(1f), String.valueOf(1f));
            TCPClientClass.sending = true;
        }
    }

    public static void onMenuChange(int i) throws InterruptedException {
        if (i == 1) {
            TCPClientClass.SendMessage("Event Timer:", String.valueOf(1f),String.valueOf(1f), String.valueOf(1f), String.valueOf(1f));
            TCPClientClass.sending = true;
        }

        else if (i == 2) {
            TCPClientClass.SendMessage("Category Responder:", String.valueOf(1f),String.valueOf(1f), String.valueOf(1f), String.valueOf(1f));
            TCPClientClass.sending = true;
        }

        else if (i == 3) {
            if (MainActivity.locked) {
                TCPClientClass.SendMessage("Measure:", String.valueOf(1f),String.valueOf(1f), String.valueOf(1f), String.valueOf(1f));
                TCPClientClass.sending = true;
            }

            else {
                MainActivity.locked = true;
                TCPClientClass.SendMessage("Measure:", String.valueOf(1f),String.valueOf(1f), String.valueOf(1f), String.valueOf(1f));
                TCPClientClass.sending = true;
            }
        }

        else if (i == 4) {
            if (MainActivity.locked) {
                TCPClientClass.SendMessage("Draw:", String.valueOf(1f),String.valueOf(1f), String.valueOf(1f), String.valueOf(1f));
                TCPClientClass.sending = true;
            }

            else {
                MainActivity.locked = true;
                TCPClientClass.SendMessage("Draw:", String.valueOf(1f),String.valueOf(1f), String.valueOf(1f), String.valueOf(1f));
                TCPClientClass.sending = true;
            }
        }

        else if (i == 5) {
            if (MainActivity.locked) {
                TCPClientClass.SendMessage("Delete:", String.valueOf(1f),String.valueOf(1f), String.valueOf(1f), String.valueOf(1f));
                TCPClientClass.sending = true;
            }

            else {
                MainActivity.locked = true;
                TCPClientClass.SendMessage("Delete:", String.valueOf(1f),String.valueOf(1f), String.valueOf(1f), String.valueOf(1f));
                TCPClientClass.sending = true;
            }
        }

        else if (i == 6) {
            TCPClientClass.SendMessage("Quit:", String.valueOf(1f),String.valueOf(1f), String.valueOf(1f), String.valueOf(1f));
            TCPClientClass.sending = true;
        }

        else if (i == 7) {
            TCPClientClass.SendMessage("Settings:", String.valueOf(1f),String.valueOf(1f), String.valueOf(1f), String.valueOf(1f));
            TCPClientClass.sending = true;
        }

        else if (i == 8) {
            TCPClientClass.SendMessage("Close:", String.valueOf(1f),String.valueOf(1f), String.valueOf(1f), String.valueOf(1f));
            TCPClientClass.sending = true;
        }

        else if (i == 9) {
            TCPClientClass.SendMessage("Place:", String.valueOf(1f),String.valueOf(1f), String.valueOf(1f), String.valueOf(1f));
            TCPClientClass.sending = true;
        }

        else if (i == 10) {
            TCPClientClass.SendMessage("Address Book:", String.valueOf(1f),String.valueOf(1f), String.valueOf(1f), String.valueOf(1f));
            TCPClientClass.sending = true;
        }
    }

    public static void timeChange(int hours, int minutes) {
        try {
            TCPClientClass.SendMessage("Change Time:", String.valueOf(hours), String.valueOf(minutes), String.valueOf(1f), String.valueOf(1f));
            TCPClientClass.sending = true;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public boolean onTouchEvent(MotionEvent event) {

        if (event.getAction() == MotionEvent.ACTION_UP) {
            Log.i(TAG, "Release: ");
            try {
                TCPClientClass.SendMessage("Release", String.valueOf(1f), String.valueOf(1f), String.valueOf(1f), String.valueOf(1f));
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }
            TCPClientClass.sending = true;
        }

        return true;

    }

    public static void ChangeSpeed(String speed) {
        int s = Integer.parseInt(speed);
        TCPClientClass.speedValue = s;
    }

    @Override
    public boolean onDoubleTap(MotionEvent e) {
        if (MainActivity.locked) {
            Log.i(TAG, "onDoubleTap: ");
            try {
                TCPClientClass.SendMessage("Double Tap", String.valueOf(1f), String.valueOf(1f), String.valueOf(1f), String.valueOf(1f));
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }
            TCPClientClass.sending = true;
        }
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {
        if (MainActivity.locked) {
            Log.i(TAG, "onLongPress: ");

            //TCPClientClass.speedValue += 50;

            //try {
            //    TCPClient.SendMessage("Long Press", String.valueOf(1f), String.valueOf(1f), String.valueOf(1f), String.valueOf(1f));
            //} catch (InterruptedException e1) {
            //    e1.printStackTrace();
            //}
            //TCPClientClass.sending = true;
        }

        //else {
            //TCPClientClass.speedValue -= 50;
        //}
    }


    @Override
    public boolean onSingleTapConfirmed(MotionEvent event) {
        if (MainActivity.locked) {
            Log.d(TAG, "onSingleTapConfirmed: ");
            try {
                TCPClientClass.SendMessage("Single Tap", String.valueOf(1f), String.valueOf(1f), String.valueOf(1f), String.valueOf(1f));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            TCPClientClass.sending = true;
        }

        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {

        if (MainActivity.locked) {
            // Grab two events located on the plane at e1=(x1, y1) and e2=(x2, y2)
            // Let e1 be the initial event
            // e2 can be located at 4 different positions, consider the following diagram
            // (Assume that lines are separated by 90 degrees.)
            //
            //
            //         \ A  /
            //          \  /
            //       D   e1   B
            //          /  \
            //         / C  \
            //
            // So if (x2,y2) falls in region:
            //  A => it's an UP swipe
            //  B => it's a RIGHT swipe
            //  C => it's a DOWN swipe
            //  D => it's a LEFT swipe
            //

            x1 = e1.getX();
            y1 = e1.getY();

            x2 = e2.getX();
            y2 = e2.getY();

            //New shit
            float horizontalDiff = e2.getX() - e1.getX();
            float verticalDiff = e2.getY() - e1.getY();
            float absHDiff = Math.abs(horizontalDiff);
            float absVDiff = Math.abs(verticalDiff);
            float absVelocityX = Math.abs(velocityX);
            float absVelocityY = Math.abs(velocityY);

            velocityX = velocityX;
            velocityY = velocityY;

            Direction direction = null;
            try {
                direction = getDirection(x1,y1,x2,y2, absVelocityX, absVelocityY);

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            try {
                return onSwipe(direction, absVelocityX, absVelocityY);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return false;
    }


    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {

        if (!MainActivity.locked) {
            // Grab two events located on the plane at e1=(x1, y1) and e2=(x2, y2)
            // Let e1 be the initial event
            // e2 can be located at 4 different positions, consider the following diagram
            // (Assume that lines are separated by 90 degrees.)
            //
            //
            //         \ A  /
            //          \  /
            //       D   e1   B
            //          /  \
            //         / C  \
            //
            // So if (x2,y2) falls in region:
            //  A => it's an UP swipe
            //  B => it's a RIGHT swipe
            //  C => it's a DOWN swipe
            //  D => it's a LEFT swipe
            //

            x1 = e1.getX();
            y1 = e1.getY();

            x2 = e2.getX();
            y2 = e2.getY();

            //New shit
            float horizontalDiff = e2.getX() - e1.getX();
            float verticalDiff = e2.getY() - e1.getY();
            float absHDiff = Math.abs(horizontalDiff);
            float absVDiff = Math.abs(verticalDiff);
            float absVelocityX = Math.abs(velocityX);
            float absVelocityY = Math.abs(velocityY);

            velocityX = velocityX;
            velocityY = velocityY;

            Direction direction = null;
            try {
                direction = getDirection(x1,y1,x2,y2, absVelocityX, absVelocityY);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            try {
                return onSwipe(direction, absVelocityX, absVelocityY);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return false;
    }

    //@Override
    //public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        // Grab two events located on the plane at e1=(x1, y1) and e2=(x2, y2)
        // Let e1 be the initial event
        // e2 can be located at 4 different positions, consider the following diagram
        // (Assume that lines are separated by 90 degrees.)
        //
        //
        //         \ A  /
        //          \  /
        //       D   e1   B
        //          /  \
        //         / C  \
        //
        // So if (x2,y2) falls in region:
        //  A => it's an UP swipe
        //  B => it's a RIGHT swipe
        //  C => it's a DOWN swipe
        //  D => it's a LEFT swipe
        //

        //x1 = e1.getX();
        //y1 = e1.getY();

        //x2 = e2.getX();
        //y2 = e2.getY();

        //New shit
        //float horizontalDiff = e2.getX() - e1.getX();
        //float verticalDiff = e2.getY() - e1.getY();
        //float absHDiff = Math.abs(horizontalDiff);
        //float absVDiff = Math.abs(verticalDiff);
        //float absVelocityX = Math.abs(velocityX);
        //float absVelocityY = Math.abs(velocityY);

        //velocityX = velocityX;
        //velocityY = velocityY;

        //Direction direction = getDirection(x1,y1,x2,y2, absVelocityX, absVelocityY);
        //try {
        //    return onSwipe(direction, absVelocityX, absVelocityY);
        //} catch (IOException e) {
        //    e.printStackTrace();
        //}
        //return false;
    //}

    public boolean onSwipe(Direction direction, float velocityX, float velocityY) throws IOException {

        //Change all this so direction is a class so it has predefined text to output and so we can return velocity
        if (direction == Direction.up) {
            TCPClientClass tcp = new TCPClientClass();
            //tcp.Send("Swipe Up");
        }

        return false;
    }

    /**
     * Given two points in the plane p1=(x1, x2) and p2=(y1, y1), this method
     * returns the direction that an arrow pointing from p1 to p2 would have.
     * @param x1 the x position of the first point
     * @param y1 the y position of the first point
     * @param x2 the x position of the second point
     * @param y2 the y position of the second point
     * @return the direction
     */
    public Direction getDirection(float x1, float y1, float x2, float y2, float vX, float vY) throws InterruptedException {
        double angle = getAngle(x1, y1, x2, y2, vX, vY);
        return Direction.get(angle, vX, vY);
    }

    /**
     *
     * Finds the angle between two points in the plane (x1,y1) and (x2, y2)
     * The angle is measured with 0/360 being the X-axis to the right, angles
     * increase counter clockwise.
     *
     * @param x1 the x position of the first point
     * @param y1 the y position of the first point
     * @param x2 the x position of the second point
     * @param y2 the y position of the second point
     * @return the angle between two points
     */
    public double getAngle(float x1, float y1, float x2, float y2, float vX, float vY) {

        double rad = Math.atan2(y1-y2,x2-x1) + Math.PI;
        return (rad*180/Math.PI + 180)%360;
    }

    public static void ChangeSettings(String settingFlood, String settingHeight, String settingParticles, String settingLabels, String settingHeightValue, String height, String width) {
        try {
            TCPClientClass.SendMessage("Settings", String.valueOf((settingFlood + settingHeight + settingParticles + settingLabels)), settingHeightValue, height, width);
            TCPClientClass.sending = true;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void IncrementWind(int speed) {
        if (speed > 0)
        {
            try {
                TCPClientClass.SendMessage("INCS", String.valueOf(1f), String.valueOf(1f), String.valueOf(1f), String.valueOf(1f));
                TCPClientClass.sending = true;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        else
        {
            try {
                TCPClientClass.SendMessage("DECS", String.valueOf(1f), String.valueOf(1f), String.valueOf(1f), String.valueOf(1f));
                TCPClientClass.sending = true;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void WindDirection(float v) {
        try {
            TCPClientClass.SendMessage("Rotate WDirection", String.valueOf(v), String.valueOf(1f), String.valueOf(1f), String.valueOf(1f));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public enum Direction{
        up,
        down,
        left,
        right,
        diagonalUpLeft,
        diagonalUpRight,
        diagonalDownLeft,
        diagonalDownRight;

        private static boolean test = false;

        /**
         * Returns a direction given an angle.
         * Directions are defined as follows:
         *
         * Up: [45, 135]
         * Right: [0,45] and [315, 360]
         * Down: [225, 315]
         * Left: [135, 225]
         *
         * @param angle an angle from 0 to 360 - e
         * @return the direction of an angle
         */

        public static Direction get(double angle, float vX, float vY) throws InterruptedException {
           float diffX = x2 - x1;
           float diffY = y2 - y1;

           if(inRange(angle, 45, 135)){
               Log.i(TAG, "Fling up: \nX=" + String.valueOf(diffX) + " Y=" + String.valueOf(diffY) + " VelocityX=" + String.valueOf(vX) + " VelocityY=" + String.valueOf(vY));
               if (!MainActivity.locked) {
                   TCPClientClass.SendMessage("Swipe Up", String.valueOf(vX),String.valueOf(vY), String.valueOf(diffX), String.valueOf(diffY));
                   TCPClientClass.sending = true;
               }

               else {
                   TCPClientClass.SendMessage("Move Up", String.valueOf(vX),String.valueOf(vY), String.valueOf(diffX), String.valueOf(diffY));
                   TCPClientClass.sending = true;
               }

                if (test) {
                   Actions actionUp = new Actions();
                   actionUp.action = "Move Up";
                   actionUp.velocityX = vX;
                   actionUp.velocityY = vY;
                   actionUp.diffX = diffX;
                   actionUp.diffY = diffY;

                   actions.add(actionUp);

                   if (actions.size() > 10) {
                       for (int i = 0; i < actions.size(); i++) {
                           TCPClientClass.SendMessage(actions.get(i).action, Float.toString(actions.get(i).velocityX), Float.toString(actions.get(i).velocityY), Float.toString(actions.get(i).diffX), Float.toString(actions.get(i).diffY));
                           TCPClientClass.sending = true;
                       }
                   }
               }

               return Direction.up;
           }


           else if(inRange(angle, 0, 45)|| inRange(angle, 315, 360))  {
               Log.i(TAG, "Fling Right: \nX= " + String.valueOf(diffX) + " Y=" + String.valueOf(diffY) + " VelocityX=" + String.valueOf(vX) + " VelocityY=" + String.valueOf(vY));
               if (!MainActivity.locked) {
                   TCPClientClass.SendMessage("Swipe Right", String.valueOf(vX),String.valueOf(vY), String.valueOf(diffX), String.valueOf(diffY));
                   TCPClientClass.sending = true;
               }

               else {
                   TCPClientClass.SendMessage("Move Right", String.valueOf(vX),String.valueOf(vY), String.valueOf(diffX), String.valueOf(diffY));
                   TCPClientClass.sending = true;
               }

               if (test) {
                   Actions actionRight = new Actions();
                   //actionRight.action = "Move Right";
                   actionRight.velocityX = vX;
                   actionRight.velocityY = vY;
                   actionRight.diffX = diffX;
                   actionRight.diffY = diffY;

                   actions.add(actionRight);

                   if (actions.size() > 10) {
                       for (int i = 0; i < actions.size(); i++) {
                           TCPClientClass.SendMessage(actions.get(i).action, Float.toString(actions.get(i).velocityX), Float.toString(actions.get(i).velocityY), Float.toString(actions.get(i).diffX), Float.toString(actions.get(i).diffY));
                           TCPClientClass.sending = true;
                       }
                   }
               }

               return Direction.right;

           }

            else if(inRange(angle, 225, 315)){
               Log.i(TAG, "Fling Down: \nX=" + String.valueOf(diffX) + " Y=" + String.valueOf(diffY) + " VelocityX=" + String.valueOf(vX) + " VelocityY=" + String.valueOf(vY));
               if (!MainActivity.locked) {
                   TCPClientClass.SendMessage("Swipe Down", String.valueOf(vX),String.valueOf(vY), String.valueOf(diffX), String.valueOf(diffY));
                   TCPClientClass.sending = true;
               }

               else {
                   TCPClientClass.SendMessage("Move Down", String.valueOf(vX),String.valueOf(vY), String.valueOf(diffX), String.valueOf(diffY));
                   TCPClientClass.sending = true;
               }


               if (test) {
                   Actions actionDown = new Actions();
                   actionDown.action = "Move Down";
                   actionDown.velocityX = vX;
                   actionDown.velocityY = vY;
                   actionDown.diffX = diffX;
                   actionDown.diffY = diffY;

                   actions.add(actionDown);

                   if (actions.size() > 10) {
                       for (int i = 0; i < actions.size(); i++) {
                           TCPClientClass.SendMessage(actions.get(i).action, Float.toString(actions.get(i).velocityX), Float.toString(actions.get(i).velocityY), Float.toString(actions.get(i).diffX), Float.toString(actions.get(i).diffY));
                           TCPClientClass.sending = true;
                       }
                   }
               }
                return Direction.down;
            }


            else {
               Log.i(TAG, "Fling Left: \nX=" + String.valueOf(diffX) + " Y=" + String.valueOf(diffY) + " VelocityX=" + String.valueOf(vX) + " VelocityY=" + String.valueOf(vY));
               if (!MainActivity.locked) {
                   TCPClientClass.SendMessage("Swipe Left", String.valueOf(vX),String.valueOf(vY), String.valueOf(diffX), String.valueOf(diffY));
                   TCPClientClass.sending = true;
               }

               else {
                   TCPClientClass.SendMessage("Move Left", String.valueOf(vX),String.valueOf(vY), String.valueOf(diffX), String.valueOf(diffY));
                   TCPClientClass.sending = true;
               }

               if (test) {
                   Actions actionLeft = new Actions();
                   actionLeft.action = "Move Left";
                   actionLeft.velocityX = vX;
                   actionLeft.velocityY = vY;
                   actionLeft.diffX = diffX;
                   actionLeft.diffY = diffY;

                   actions.add(actionLeft);

                   if (actions.size() > 10) {

                       for (int i = 0; i < actions.size(); i++) {
                           TCPClientClass.SendMessage(actions.get(i).action, Float.toString(actions.get(i).velocityX), Float.toString(actions.get(i).velocityY), Float.toString(actions.get(i).diffX), Float.toString(actions.get(i).diffY));
                           TCPClientClass.sending = true;
                       }
                   }
               }

               return Direction.left;
            }
        }

        /**
         * @param angle an angle
         * @param init the initial bound
         * @param end the final bound
         * @return returns true if the given angle is in the interval [init, end).
         */
        private static boolean inRange(double angle, float init, float end){
            return (angle >= init) && (angle < end);
        }
    }

    private static String getTouchType(MotionEvent e){

        String touchTypeDescription = " ";
        int touchType = e.getToolType(0);

        switch (touchType) {
            case MotionEvent.TOOL_TYPE_FINGER:
                touchTypeDescription += "(finger)";
                break;
            case MotionEvent.TOOL_TYPE_STYLUS:
                touchTypeDescription += "(stylus, ";
                //Get some additional information about the stylus touch
                float stylusPressure = e.getPressure();
                touchTypeDescription += "pressure: " + stylusPressure;

                if(Build.VERSION.SDK_INT >= 21) {
                    touchTypeDescription += ", buttons pressed: " + getButtonsPressed(e);
                }

                touchTypeDescription += ")";
                break;
            case MotionEvent.TOOL_TYPE_ERASER:
                touchTypeDescription += "(eraser)";
                break;
            case MotionEvent.TOOL_TYPE_MOUSE:
                touchTypeDescription += "(mouse)";
                break;
            default:
                touchTypeDescription += "(unknown tool)";
                break;
        }

        return touchTypeDescription;
    }

    /**
     * Returns a human-readable string listing all the stylus buttons that were pressed when the
     * input MotionEvent occurred.
     */
    @TargetApi(21)
    private static String getButtonsPressed(MotionEvent e){
        String buttons = "";

        if(e.isButtonPressed(MotionEvent.BUTTON_PRIMARY)){
            buttons += " primary";
        }

        if(e.isButtonPressed(MotionEvent.BUTTON_SECONDARY)){
            buttons += " secondary";
        }

        if(e.isButtonPressed(MotionEvent.BUTTON_TERTIARY)){
            buttons += " tertiary";
        }

        if(e.isButtonPressed(MotionEvent.BUTTON_BACK)){
            buttons += " back";
        }

        if(e.isButtonPressed(MotionEvent.BUTTON_FORWARD)){
            buttons += " forward";
        }

        if (buttons.equals("")){
            buttons = "none";
        }

        return buttons;
    }

    /*
     public static class TCPClient extends AsyncTask<String, Void, String> {
        //AUDITORIUM"150.237.96.16"
         //2120
         //BOX"150.237.94.47"
        public static String SERVERIP = "150.237.94.42";
        public static int SERVERPORT = 2120;
        public boolean mRun = true;
        public static Socket socket;
        public static float vX;
        public static float vY;
        PrintWriter out;
        BufferedReader in;
        public static byte[] message;
        public static int speedValue = 0;

        public static boolean sending = false;
        private boolean receivingData = true;
         private boolean initialising = true;
         private boolean clientConnected = false;
         private int count = 0;

         public static ArrayList<String> temporaryAddress = new ArrayList<String>();
        private Socket clientSocket;
        private ServerSocket serverSocket;

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
    }*/

}
