package com.example.android.basicgesturedetect;

import android.app.Dialog;
import android.gesture.Gesture;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toolbar;

/**
 * Created by 462904 on 12/05/2016.
 */
public class TimerDialogFragment extends DialogFragment implements TimePicker.OnTimeChangedListener  {

    private TimePicker mTimePicker;
    private Spinner mSpinner;
    private Button mPlayTimer;
    private Button mIncrementTimer;
    private Button mDecrementTimer;
    private Button mPauseTimer;
    private Button mApplyTimer;

    public int hours = 0;
    public int minutes = 0;

    public boolean playing = false;

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        setStyle(DialogFragment.STYLE_NO_FRAME, R.style.Theme_Base);
        return super.onCreateDialog(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_timer, container);
        getDialog().setTitle("Select a Time and Speed:");


        mTimePicker = (TimePicker)view.findViewById(R.id.timePicker);
        mDecrementTimer = (Button)view.findViewById(R.id.decrementTimerButton);
        mPauseTimer = (Button)view.findViewById(R.id.pauseTimerButton);
        mApplyTimer = (Button)view.findViewById(R.id.applyTimerButton);
        mPlayTimer = (Button)view.findViewById(R.id.playTimerButton);
        mIncrementTimer = (Button)view.findViewById(R.id.incrementTimerButton);



        mApplyTimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTimePicker.clearFocus();
                hours = mTimePicker.getCurrentHour();
                minutes = mTimePicker.getCurrentMinute();

                try {
                    GestureListener.timeChange(hours, minutes);
                }

                catch (Exception e) {

                }
            }
        });

        mPlayTimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    GestureListener.onPlay();
                }

                catch (Exception e) {

                }
            }
        });

        mPauseTimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTimePicker.clearFocus();

                try {
                    GestureListener.onPauseTimer();
                }

                catch (Exception e) {

                }
            }
        });

        mIncrementTimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTimePicker.clearFocus();

                try {
                    GestureListener.incrementSpeed();
                }

                catch (Exception e) {

                }
            }
        });

        mDecrementTimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTimePicker.clearFocus();

                try {
                    GestureListener.decrementSpeed();
                } catch (Exception e) {

                }
            }
        });



        //getDialog().setTitle("Hello");
        return view;
    }


    @Override
    public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {

    }
}
