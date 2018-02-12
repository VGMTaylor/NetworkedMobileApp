package com.example.android.basicgesturedetect;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by 462904 on 09/06/2016.
 */

// The actual activity of the address list, stores the list which will be populated full of addresses
public class AddressListViewActivity extends Activity {
    ListView listViewAddress;
    Cursor cursor;
    AddressListAdapter addressListAdapter;
    Context context;
    public ArrayList<Address> arrayOfAdddresses;

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
        getActionBar().setTitle("Address Book");
        setContentView(R.layout.addressview_activity);
        populateAddresses();

    }

    private void populateAddresses() {
        arrayOfAdddresses = new ArrayList<Address>();
        arrayOfAdddresses = GestureListener.addresses;
        AddressListAdapter adapter = new AddressListAdapter(this, arrayOfAdddresses);
        ListView listView = (ListView)findViewById(R.id.listViewAddress);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String longitude = arrayOfAdddresses.get(position).Long;
                String latitude = arrayOfAdddresses.get(position).Lat;

                GestureListener.onSearchGPS(longitude, latitude);
            }
        });
    }
}
