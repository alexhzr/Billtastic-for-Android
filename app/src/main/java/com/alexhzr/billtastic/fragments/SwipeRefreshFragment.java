package com.alexhzr.billtastic.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alexhzr.billtastic.R;

public class SwipeRefreshFragment extends Fragment {

    public SwipeRefreshFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recycler_view, null);

        return view;
    }

    

}
