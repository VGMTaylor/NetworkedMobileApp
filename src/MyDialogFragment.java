package com.example.android.basicgesturedetect;

import android.app.AlertDialog;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.android.common.logger.Log;

/**
 * Created by 462904 on 21/04/2016.
 */
public class MyDialogFragment extends DialogFragment implements TextView.OnEditorActionListener {

    public interface EditNameDialogListener {
        void onFinishEditDialog(String inputText);
    }

    private EditText mEditText;
    private EditText mEditText2;
    private Button mOkButton;
    private Button mCancelButton;
    private Button mRotateClockwise;
    private Button mRotateAnticlockwise;

    private double windSpeed;
    private double windAngle;

    private float windDirection = 10f;

    public MyDialogFragment() {
        //Empty constructor
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_wind, container);
        getDialog().setTitle("Enter Wind values:");
        mEditText = (EditText)view.findViewById(R.id.txt_your_name);
        mEditText2 = (EditText)view.findViewById(R.id.txt_your_name3);
        mOkButton = (Button)view.findViewById(R.id.okButton);
        mRotateClockwise = (Button)view.findViewById(R.id.incrementWindButton);
        mRotateAnticlockwise = (Button)view.findViewById(R.id.decrementWindButton);

        mOkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String speed = String.valueOf(mEditText.getText());
                String angle = String.valueOf(mEditText2.getText());

                boolean correctInput = true;


                for (int i = 0; i < mEditText.length(); i++){
                    if (Character.isDigit(speed.charAt(i))) {

                    }

                    else {
                        correctInput = false;
                    }

                    if (Character.isDigit(angle.charAt(i))) {

                    }

                    else {
                        correctInput = false;
                    }
                }

                if (correctInput) {
                    try {
                        windSpeed = Double.parseDouble(mEditText.getText().toString());
                        windAngle = Double.parseDouble(mEditText2.getText().toString());
                        GestureListener.onWindChange(windSpeed, windAngle);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    getDialog().dismiss();
                }

                else {
                    final AlertDialog.Builder windError = new AlertDialog.Builder(getContext());
                    windError.setTitle("Incorrect format");
                    windError.setMessage("Detected non-numeric values in the input");
                    windError.setIcon(android.R.drawable.ic_dialog_alert);

                    AlertDialog alertDialog = windError.create();
                    alertDialog.show();
                }
            }
        });

        mRotateClockwise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GestureListener.IncrementWind(1);
            }
        });

        mRotateAnticlockwise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GestureListener.IncrementWind(-1);
            }
        });


        //getDialog().setTitle("Hello");
        return view;
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (EditorInfo.IME_ACTION_DONE == actionId) {
            // Return input text to activity
            EditNameDialogListener activity = (EditNameDialogListener) getActivity();
            activity.onFinishEditDialog(mEditText.getText().toString());
            this.dismiss();
            return true;
        }
        return false;
    }


}
