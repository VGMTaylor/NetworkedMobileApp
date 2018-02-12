package com.example.android.basicgesturedetect;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

/**
 * Created by 462904 on 04/07/2016.
 */
public class MouseTypeFragment extends DialogFragment implements ListView.OnItemSelectedListener {

    private ListView mListView;
    private String[] mouseList;
    private Button mBackButton;

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mousetype, container);
        getDialog().setTitle("Mouse Type");

        mouseList = getResources().getStringArray(R.array.mouseList);
        mBackButton = (Button)view.findViewById(R.id.backMouseButton);

        mListView = (ListView)view.findViewById(R.id.listViewMouse);
        mListView.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, mouseList));
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Object listItem = mListView.getItemAtPosition(position);

                for (int i = 0; i < mouseList.length; i++) {
                    if (listItem == mouseList[i]) {
                        try {
                            GestureListener.OnMouseChange(i);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
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

}
