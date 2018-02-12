package com.example.android.basicgesturedetect;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.android.basicgesturedetect.Address;
import com.example.android.basicgesturedetect.R;

import java.util.ArrayList;

/**
 * Created by 462904 on 09/06/2016.
 */

// Address adapter to populate the list with addresses pulled from the program
public class AddressListAdapter extends ArrayAdapter<Address> {

    public AddressListAdapter(Context context, ArrayList<Address> addresses)
    {
        super(context, 0, addresses);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        //Get data from this position
        Address address = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.fragment_addresslist, parent, false);
        }

        // Lookup view for data population
        TextView addressName = (TextView)convertView.findViewById(R.id.AddressName);
        TextView addressDesc = (TextView)convertView.findViewById(R.id.AddressDescription);

        // Populate the data into the view using data object
        addressName.setText(address.Address1);
        addressDesc.setText(address.Town + ", " + address.Postcode);

        return convertView;
    }
}
