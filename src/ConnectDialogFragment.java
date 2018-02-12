package com.example.android.basicgesturedetect;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.gesture.Gesture;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.DialogFragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import java.io.IOException;
import java.util.List;

/**
 * Created by 462904 on 05/05/2016.
 */

// This class represents the dialog for connecting to an IP manually
public class ConnectDialogFragment extends DialogFragment implements TextView.OnEditorActionListener {

    public static final String PREFS_NAME = "IP_PREFS";
    public static final String PREFS_KEY = "PREFS_String";

    private EditText mIPAddressText;
    private EditText mPortText;
    private Button mOkButton;
    private Button mBackButton;
    private Spinner mIPSpinner;

    private String mIPAddress;
    private int mPortNumber;
    private List<String> listOfIps;

    public ConnectDialogFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_connect, container);
        getDialog().setTitle("Enter an IP and Port to connect to:");
        mIPAddressText = (EditText) view.findViewById(R.id.ipEditText);
        //mPortText = (EditText) view.findViewById(R.id.portEditText);
        mOkButton = (Button)view.findViewById(R.id.okButton2);
        mBackButton = (Button)view.findViewById(R.id.backButton2);

        mOkButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                mIPAddress = mIPAddressText.getText().toString();
                //mPortNumber = Integer.parseInt(mPortText.getText().toString());

                //String port = mPortText.toString();

                boolean correctInput = true;

                for (int i = 0; i < mIPAddress.length(); i++){
                    if (Character.isDigit(mIPAddress.charAt(i))) {
                    }

                    else {
                        char c = mIPAddress.charAt(i);
                        if (c == '.') {

                        }

                        else {
                            correctInput = false;
                        }
                    }
                }

                if (correctInput) {
                    try {
                        GestureListener.startServer(mIPAddress, 2120);

                    } catch (Exception e) {
                        final AlertDialog.Builder ipError = new AlertDialog.Builder(getContext());
                        ipError.setTitle("Failed to connect to server");
                        ipError.setMessage("Either incorrect IP entered or server is not reachable /n Error Message: " + e);
                        ipError.setIcon(android.R.drawable.ic_dialog_alert);

                        AlertDialog alertDialog = ipError.create();
                        alertDialog.show();
                    }

                    getDialog().dismiss();
                }

                else {
                    final AlertDialog.Builder ParseError = new AlertDialog.Builder(getContext());
                    ParseError.setTitle("Incorrect format");
                    ParseError.setMessage("Only use numberic or full stops in the input");
                    ParseError.setIcon(android.R.drawable.ic_dialog_alert);

                    AlertDialog alertDialog = ParseError.create();
                    alertDialog.show();
                }
            }

        });

        mBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
            }
        });

        return view;
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        return false;
    }
}
