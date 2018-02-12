package com.example.android.basicgesturedetect;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.gesture.Gesture;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

/**
 * Created by 462904 on 21/04/2016.
 */
public class AddDialogFragment extends DialogFragment implements ListView.OnItemSelectedListener {

    private ListView mListView;
    private String[] addList;

    public String[] POIitems = new String[] {"Schools", "Police Stations", "Fire Stations", "Hospitals", "Airports", "Stadiums"};
    public String[] radiusItems = new String[]{"500", "1000", "2000", "5000", "10000", "25000", "50000"};

    private Button mBackButton;
    private float radius = 100;

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public interface EditNameDialogListener {
        void onFinishEditDialog(String inputText);
    }


    public AddDialogFragment() {
        //Empty constructor
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add, container);
        getDialog().setTitle("Select Object");

        addList = getResources().getStringArray(R.array.addList);
        mBackButton = (Button)view.findViewById(R.id.backAddButton);

        mListView = (ListView) view.findViewById(R.id.listViewAdd);
        mListView.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, addList));
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Object listItem = mListView.getItemAtPosition(position);

                for (int i = 0; i < addList.length; i++) {
                    if (listItem == addList[i]) {
                        int selected = i;

                        if (i == 4) {
                            informationDialog();
                        }
                        
                        if (i == 6) {
                            cordonDialog();
                        }

                        else {
                            try {
                                GestureListener.onObjectPlace(i, radius);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
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

    // This function opens a dialog that allows the user to add text to their information placeable
    private void informationDialog() {

        final AlertDialog.Builder inputAlert = new AlertDialog.Builder(getActivity());
        inputAlert.setTitle("Information:");
        inputAlert.setMessage("Enter Text");
        final EditText userInput = new EditText(getActivity());
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
                    GestureListener.onInfo(userInputValue);
                    getDialog().dismiss();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });


        AlertDialog alertDialog = inputAlert.create();
        alertDialog.show();

    }


    // This function opens a dialog which will let the user select what objects they want to search for with their POI placeables
    private void personOfInterestDialog() {

        final AlertDialog.Builder inputAlert = new AlertDialog.Builder(getActivity());
        inputAlert.setTitle("Information:");
        inputAlert.setMessage("Enter Text");

        final Spinner userInput = new Spinner(getActivity());
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_list_item_1, POIitems);
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        userInput.setAdapter(adapter);

        final Spinner radInput = new Spinner(getActivity());
        ArrayAdapter<String> radAdapter = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_list_item_1, POIitems);
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        userInput.setAdapter(radAdapter);

        inputAlert.setView(radInput);

        inputAlert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        inputAlert.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String userInputValue = userInput.getSelectedItem().toString();
                String radius = radInput.getSelectedItem().toString();
                try {
                    GestureListener.onPOI(userInputValue, radius);
                    getDialog().dismiss();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });


        AlertDialog alertDialog = inputAlert.create();
        alertDialog.show();

    }

    // Cordon dialog will prompt user to enter a radius for the cordon
    private void cordonDialog() {
        final AlertDialog.Builder inputAlert = new AlertDialog.Builder(getActivity());
        inputAlert.setTitle("Cordon");
        inputAlert.setMessage("Enter Radius");

        LinearLayout layout = new LinearLayout(getActivity());
        layout.setOrientation(LinearLayout.VERTICAL);

        final EditText userInput = new EditText(getActivity());
        layout.addView(userInput);
        userInput.setHint("Radius");

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
                String radiusString = userInput.getText().toString();
                int radius = Integer.parseInt(radiusString);

                try {
                    GestureListener.onObjectPlace(6, radius);
                    getDialog().dismiss();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });


        AlertDialog alertDialog = inputAlert.create();
        alertDialog.show();
    }
}
