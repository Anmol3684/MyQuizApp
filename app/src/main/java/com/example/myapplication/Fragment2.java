package com.example.myapplication;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class Fragment2 extends Fragment {

    View view;
    TextView textView;
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            view = inflater.inflate(R.layout.fragment_fragment2, container, false);
            MainActivity activity = (MainActivity) getActivity();
            String data = activity.getMyData();
            textView = view.findViewById(R.id.textView3);
            textView.setText(data);
            return view;
        }
    }
