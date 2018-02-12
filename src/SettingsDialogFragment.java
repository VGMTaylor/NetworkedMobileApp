package com.example.android.basicgesturedetect;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 462904 on 20/05/2016.
 */
public class SettingsDialogFragment extends DialogFragment implements AdapterView.OnItemClickListener {

    public Spinner spinner;
    public Spinner speedSpinner;

    private float decimal = 0f;

    private CheckBox mShowFlood;
    private CheckBox mHeightData;
    private CheckBox mParticles;
    private CheckBox mPlumes;
    private CheckBox mLabels;

    private String mSettingHeight;
    private String mSettingFlood;
    private String mSettingParticles;
    private String mSettingPlumes;
    private String mSettingLabels;
    private String mSettingHeightValue;
    private String mSettingResoultion;
    private String mSpinnerValue;
    private String mSpeedValue;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container);
        getDialog().setTitle("Settings:");

        spinner = (Spinner)view.findViewById(R.id.settingsspinner);
        speedSpinner = (Spinner)view.findViewById(R.id.mousespinner);
        mShowFlood = (CheckBox)view.findViewById(R.id.radio_showflood);
        mHeightData = (CheckBox)view.findViewById(R.id.radio_heightdata);
        mParticles = (CheckBox)view.findViewById(R.id.radio_particles);
        //mPlumes = (CheckBox)view.findViewById(R.id.radio_plumes);
        mLabels = (CheckBox)view.findViewById(R.id.radio_labels);
        SeekBar seekbar = (SeekBar)view.findViewById(R.id.seekbartextupdate);
        final TextView text = (TextView)view.findViewById(R.id.seekbartext);
        Button create = (Button)view.findViewById(R.id.applySettings1);
        Button back = (Button)view.findViewById(R.id.closeSettings);
        Button increment = (Button)view.findViewById(R.id.incrementWater);
        Button decrement = (Button)view.findViewById(R.id.decrementWater);

        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                float value = ((float) progress / 10.0f);
                text.setText(Float.toString(value));

                mSettingHeightValue = Float.toString(value);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckValues();
            }
        };

        create.setOnClickListener(clickListener);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
            }
        });

        List<String> height = new ArrayList<String>();
        height.add("1920x1080");
        height.add("1280x1024");
        height.add("1280x720");

        List<String> speed = new ArrayList<String>();
        speed.add("50");
        speed.add("100");
        speed.add("150");
        speed.add("200");
        speed.add("250");
        speed.add("300");
        speed.add("350");
        speed.add("400");
        speed.add("450");
        speed.add("500");


        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, height);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);

        // Creating adapter for spinner
        ArrayAdapter<String> speedAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, speed);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        speedSpinner.setAdapter(speedAdapter);

        return view;
    }

    public void CheckValues() {

        mSpeedValue = speedSpinner.getSelectedItem().toString();
        int value = Integer.parseInt(mSpeedValue);

        TCPClientClass.speedValue = value;

        mSpinnerValue = spinner.getSelectedItem().toString();
        int resoultionSplit = mSpinnerValue.indexOf('x');

        mSpinnerValue = mSpinnerValue.replace("x", "");

        String height = "";
        String width = "";

        for (int i = 0; i < mSpinnerValue.length(); i++) {
            if (i < resoultionSplit)
                height += Character.toString(mSpinnerValue.charAt(i));

            else
                width += Character.toString(mSpinnerValue.charAt(i));
        }

        if (mShowFlood.isChecked()) {
            mSettingFlood = "1";
        }

        else {
            mSettingFlood = "0";
        }

        if (mHeightData.isChecked()) {
            mSettingHeight = "1";
        }

        else {
            mSettingHeight = "0";
        }

        if (mParticles.isChecked()) {
            mSettingParticles = "1";
        }

        else {
            mSettingParticles = "0";
        }

        /*if (mPlumes.isChecked()) {
            mSettingPlumes = "1";
        }

        else {
            mSettingPlumes = "0";
        }*/

        if (mLabels.isChecked()) {
            mSettingLabels = "1";
        }

        else {
            mSettingLabels = "0";
        }

        GestureListener.ChangeSettings(mSettingFlood, mSettingHeight, mSettingParticles, mSettingLabels, mSettingHeightValue, height, width);
    }

    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
    {
        int id = buttonView.getId();
        if(id == R.id.radio_heightdata)
        {
            if(mHeightData.isChecked()){
                mHeightData.setChecked(false);
            }
            else{
                mHeightData.setChecked(true);
            }
        }

        else if(id == R.id.radio_showflood){
            if(mShowFlood.isChecked()){
                mShowFlood.setChecked(false);
            }
            else{
                mShowFlood.setChecked(true);
            }
        }

        else if(id == R.id.radio_particles){
            if(mParticles.isChecked()){
                mParticles.setChecked(false);
            }
            else{
                mParticles.setChecked(true);
            }
        }

        /*else if(id == R.id.radio_plumes){
            if(mPlumes.isChecked()){
                mPlumes.setChecked(false);
            }
            else{
                mPlumes.setChecked(true);
            }
        }*/

        else if(id == R.id.radio_labels){
            if(mLabels.isChecked()){
                mLabels.setChecked(false);
            }
            else{
                mLabels.setChecked(true);
            }
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }


}
