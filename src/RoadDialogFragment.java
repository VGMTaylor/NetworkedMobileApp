package com.example.android.basicgesturedetect;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by 462904 on 22/04/2016.
 */
public class RoadDialogFragment extends DialogFragment implements TextView.OnEditorActionListener{
    private Button mRoadButton;
    private Button mSateliteButton;
    private Button mHeightButton;

    private TextView mTextView;

    public interface EditNameDialogListener {
        void onFinishEditDialog(String inputText);
    }


    public RoadDialogFragment() {
        //Empty constructor
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_roads, container);
        getDialog().setTitle("Select a Road:");
        mRoadButton = (Button)view.findViewById(R.id.roadButton);
        mSateliteButton = (Button)view.findViewById(R.id.sateliteButton);
        mHeightButton = (Button)view.findViewById(R.id.heightButton);

        mRoadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    GestureListener.onRoadChange(1);
                    getDialog().dismiss();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        mSateliteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    GestureListener.onRoadChange(2);
                    getDialog().dismiss();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        mHeightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    GestureListener.onRoadChange(3);
                    getDialog().dismiss();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
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
            activity.onFinishEditDialog(mTextView.getText().toString());
            this.dismiss();
            return true;
        }
        return false;
    }


}
