package com.example.hamamlaha.screens;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.hamamlaha.R;

public class StepFragment extends Fragment {

    private static final String ARG_STEP = "step_number";

    public static StepFragment newInstance(int stepNumber) {
        StepFragment fragment = new StepFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_STEP, stepNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_step, container, false);

        int step = getArguments() != null ? getArguments().getInt(ARG_STEP) : 1;

        TextView tv = view.findViewById(R.id.stepText);
        tv.setText("תוכן של שלב " + step);

        return view;
    }
}