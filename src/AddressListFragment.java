package com.example.android.basicgesturedetect;

import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import java.util.UUID;

/**
 * Created by 462904 on 09/06/2016.
 */

// This class inflates the address list layout and actually shows the informaion we need to populate the fragment
public class AddressListFragment extends Fragment{
    private Address mAddress;
    private EditText mAddress1Text;
    private EditText mAddress2Text;
    private EditText mTownText;
    private EditText mCountyText;
    private EditText mPostCode;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //mAddress = CrimeLab.get(getActivity()).getCrime(addressId);
        //mPhotoFile = CrimeLab.get(getActivity()).getPhotoFile(mAddress);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_addresslist, container, false);

        mAddress1Text = (EditText) v.findViewById(R.id.addressTitle);

        return v;
    }
}
