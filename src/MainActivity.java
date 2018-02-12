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

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.gesture.Gesture;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.ToggleButton;
import android.widget.ZoomControls;

import com.example.android.common.activities.SampleActivityBase;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;

/**
 * A simple launcher activity containing a summary sample description
 * and a few action bar buttons.
 */

public class MainActivity extends SampleActivityBase implements OnClickListener, AdapterView.OnItemClickListener {

    public static final String TAG = "MainActivity";
    public static final String FRAGTAG = "LIMA Mobile";
    private static boolean autoconnect = false;

    private ListView mList;
    private ArrayList<String> arrayList;
    private Context context = this;
    //private MyCustomAdapter mAdapter;

    public float zoomLevel = 0.1f;

    private ZoomControls mZoomControls;
    private static ToggleButton mToggleButton;

    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private String[] planets;
    private ActionBarDrawerToggle mDrawerToggle;

    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    private String[] mPlanetTiles;

    private Button mConfirmWind;

    public static boolean locked;
    public static String[] addList;

    private static Context context2;

    public static Context getAppContext() {
        return MainActivity.context2;
    }

    @Override
    //This will tell the server to shutdown the socket in the case of a crash
    protected  void onDestroy() {
        try {
            GestureListener.onMenuChange(6);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RelativeLayout myLayout = (RelativeLayout)findViewById(R.id.sample_main_layout);

        MainActivity.context2 = getApplicationContext();

        addList = getResources().getStringArray(R.array.addList);

        locked = true;

        //Adapter will use this reference to create a layout specifieid by simple list item
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        planets = getResources().getStringArray(R.array.planets);
        mDrawerList = (ListView)findViewById(R.id.drawerList);
        mDrawerList.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, planets));
        mDrawerList.setOnItemClickListener(this);

        mToggleButton = (ToggleButton) findViewById(R.id.toggleButton);

        /*myLayout.setOnTouchListener(
            new RelativeLayout.OnTouchListener() {

                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    try {
                        handleTouch(event);
                    } catch (SocketException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return true;
                }
            }
        );*/

        mToggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked == true) {
                    locked = false;
                }

                else {
                    locked = true;
                }
            }
        });

        mZoomControls = (ZoomControls) findViewById(R.id.zoomControls) ;

        mZoomControls.setOnZoomInClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    GestureListener.onZoom(zoomLevel);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        });

        mZoomControls.setOnZoomOutClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    GestureListener.onZoom(-zoomLevel);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        });

        try {
            runServer();
        } catch (IOException e) {
            e.printStackTrace();
        }

        arrayList = new ArrayList<String>();

        if (getSupportFragmentManager().findFragmentByTag(FRAGTAG) == null) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();       //Get the fragment and begin transaction
            BasicGestureDetectFragment fragment = new BasicGestureDetectFragment();                 //Create a BasicGestureDetectFragment object
            transaction.add(fragment, FRAGTAG);                                                     //Add a fragment to the activity state
            transaction.commit();                                                                   //Commits the transaction.
        }
    }

    private void handleTouch(MotionEvent m) throws IOException {
        int pointerCount = m.getPointerCount();


        for (int i = 0; i < pointerCount; i++)
        {
            int x = (int) m.getX(i);
            int y = (int) m.getY(i);
            int id = m.getPointerId(i);
            int action = m.getActionMasked();
            int actionIndex = m.getActionIndex();
            String actionString;

            float initX = 0;
            float initY = 0;


            switch (action)
            {
                case MotionEvent.ACTION_DOWN:
                    actionString = "DOWN";
                    initX = m.getX();
                    initY = m.getY();
                    break;
                case MotionEvent.ACTION_UP:
                    actionString = "UP";
                    break;
                case MotionEvent.ACTION_POINTER_DOWN:
                    actionString = "PNTR DOWN";
                    break;
                case MotionEvent.ACTION_POINTER_UP:
                    actionString = "PNTR UP";
                    break;
                case MotionEvent.ACTION_MOVE:
                    actionString = "MOVE";
                    float curX = initX - m.getX();
                    float curY = initY - m.getY();
                    //sendThroughUDP(curX, curY);
                break;
                default:
                    actionString = "";
            }
        }
    }

    private void showEditDialog() {
        android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
        MyDialogFragment myDialogFragment = new MyDialogFragment();
        myDialogFragment.show(fm, "fragment_wind");
    }

    private void showRoadDialog() {
        android.support.v4.app.FragmentManager fm2 = getSupportFragmentManager();
        RoadDialogFragment rdFragment = new RoadDialogFragment();
        rdFragment.show(fm2, "fragment_roads");
    }

    private void showAddressBook() {
        Intent intent = new Intent(this, AddressListViewActivity.class);

        try {
            startActivity(intent);
        }

        catch (Exception e) {
            Log.i(TAG, String.valueOf(e));
        }
    }

    private void showConnectDialog() {
        android.support.v4.app.FragmentManager fm3 = getSupportFragmentManager();
        ConnectDialogFragment cFragment = new ConnectDialogFragment();
        cFragment.show(fm3, "fragment_connect");
    }

    private void showSettingsDialog() {
        android.support.v4.app.FragmentManager fm4 = getSupportFragmentManager();
        SettingsDialogFragment sFragment = new SettingsDialogFragment();
        sFragment.show(fm4, "fragment_settings");
    }

    private void showMouseDialog() {
        android.support.v4.app.FragmentManager fm2 = getSupportFragmentManager();
        MouseTypeFragment msFragment = new MouseTypeFragment();
        msFragment.show(fm2, "fragment_mousetype");
    }

    private void addItem() {
        android.support.v4.app.FragmentManager fm4 = getSupportFragmentManager();
        AddDialogFragment addDialogFragment = new AddDialogFragment();
        addDialogFragment.show(fm4, "fragment_add");
    }

    private void showTimerDialog() {
        android.support.v4.app.FragmentManager fm5 = getSupportFragmentManager();
        TimerDialogFragment timerDialogFragment = new TimerDialogFragment();
        timerDialogFragment.show(fm5, "fragment_timer");
    }



    //Here we override onCreateOptionsMenu to inflate our menu resource in XML. into the menu provided by callback.
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);                                               //Inflate the menu resource
        MenuItem menuItem = menu.findItem(R.id.search_action);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.search_action:
                searchLocation();
                return true;
            case R.id.add_action:
                addItem();
                return true;
        }
        return false;
    }

    private void searchLocation() {

        int selection = 0;

        final AlertDialog.Builder radioAlert = new AlertDialog.Builder(context);
        radioAlert.setTitle("Choose Search Method:");

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.select_dialog_singlechoice);
        arrayAdapter.add("Location ");
        arrayAdapter.add("GPS ");
        arrayAdapter.add("NationalGrid ");


        radioAlert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        radioAlert.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {
                    final AlertDialog.Builder inputAlert = new AlertDialog.Builder(context);
                    inputAlert.setTitle("Search Location:");
                    inputAlert.setMessage("Enter a location or longitude/latitude");
                    final EditText userInput = new EditText(context);
                    inputAlert.setView(userInput);

                    inputAlert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });

                    inputAlert.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String userInputValue = userInput.getText().toString();
                            try {
                                GestureListener.onSearch(userInputValue);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    });


                    AlertDialog alertDialog = inputAlert.create();
                    alertDialog.show();
                }

                if (which == 1) {
                    final AlertDialog.Builder inputAlert = new AlertDialog.Builder(context);
                    LinearLayout layout = new LinearLayout(context);
                    layout.setOrientation(LinearLayout.VERTICAL);

                    inputAlert.setTitle("Search Location:");
                    inputAlert.setMessage("Enter a longitude/latitude");

                    final EditText userInput4 = new EditText(context);
                    layout.addView(userInput4);
                    userInput4.setHint("Latitude");

                    final EditText userInput3 = new EditText(context);
                    layout.addView(userInput3);
                    userInput3.setHint("Longitude");


                    inputAlert.setView(layout);

                    inputAlert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });


                    inputAlert.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String userInputValue1 = userInput3.getText().toString();
                            String userInputValue2 = userInput4.getText().toString();
                            GestureListener.onSearchGPS(userInputValue1, userInputValue2);
                        }
                    });


                    AlertDialog alertDialog = inputAlert.create();
                    alertDialog.show();
                }
            }
        });

        radioAlert.show();
    }

    public void SendMessage(String message) {
        //TCPClient.Send();
    }

    /**
     * Create a chain of targets that will receive log data
     */

    public static void runServer() throws IOException {

        //DiscoveryClient f = new DiscoveryClient();
        //f.run();

        if (autoconnect) {
            try {
                GestureListener.startServer();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void sendThroughUDP(float curX, float curY) throws IOException {
        String udpMsg = "Move " + curX + " " + curY;

        DatagramSocket ds = null;

        try {
            ds = new DatagramSocket();

            InetAddress serverAddr = InetAddress.getByName("150.237.94.42");

            DatagramPacket dp;

            dp = new DatagramPacket(udpMsg.getBytes(), udpMsg.length(), serverAddr, 1120);

            ds.send(dp);
        } catch (Exception e) {
            e.printStackTrace();
        }

        finally {
            if (ds != null) {
                ds.close();
            }
        }
    };

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (position == 0) {
            showConnectDialog();
        }

        if (position == 1) {
            showMouseDialog();
        }

        if (position == 2) {
            showRoadDialog();
        }

        if (position == 3) {
            showEditDialog();
        }

        if (position == 4) {
            Toast.makeText(this, "Event Timer", Toast.LENGTH_LONG).show();
            showTimerDialog();
        }

        if (position == 5) {
            Toast.makeText(this, "Category Responder", Toast.LENGTH_LONG).show();
            try {
                GestureListener.onMenuChange(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        if (position == 6) {
            Toast.makeText(this, "Address Book", Toast.LENGTH_LONG).show();

            showAddressBook();
            //setContentView(R.layout.fragment_address);

           // try {
                //GestureListener.onMenuChange(10);
            //} catch (InterruptedException e) {
               // e.printStackTrace();
            //}
        }

        if (position == 7) {
            Toast.makeText(this, "Settings", Toast.LENGTH_LONG).show();
            showSettingsDialog();
            //try {
                //GestureListener.onMenuChange(7);
            //} catch (InterruptedException e) {
                //e.printStackTrace();
            //}
        }

        if (position == 8) {
            Toast.makeText(this, "Quit", Toast.LENGTH_LONG).show();
            try {
                GestureListener.onMenuChange(6);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.exit(0);
        }

        //if (position == 1) {
        //Close menus
        //try {
        //GestureListener.onMenuChange(8);
        //} catch (InterruptedException e) {
        //e.printStackTrace();
        //}
        //}

        //if (position == 4) {
        //Toast.makeText(this, "Measure", Toast.LENGTH_LONG).show();
        //try {
        //GestureListener.onMenuChange(3);
        //} catch (InterruptedException e) {
        //e.printStackTrace();
        //}
        //}

        //if (position == 5) {
        //Toast.makeText(this, "Draw", Toast.LENGTH_LONG).show();
        //try {
        //GestureListener.onMenuChange(4);
        //} catch (InterruptedException e) {
        //e.printStackTrace();
        //}
        //}

        //if (position == 6) {
        //Toast.makeText(this, "Delete", Toast.LENGTH_LONG).show();
        //try {
        //GestureListener.onMenuChange(5);
        //} catch (InterruptedException e) {
        //e.printStackTrace();
        //}
        //}

        //if (position == 7) {
        //Toast.makeText(this, "Move", Toast.LENGTH_LONG).show();
        //try {
        //GestureListener.onMenuChange(9);
        //} catch (InterruptedException e) {
        //e.printStackTrace();
        //}
        //}

    }
}
