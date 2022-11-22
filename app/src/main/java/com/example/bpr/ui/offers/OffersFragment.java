package com.example.bpr.ui.offers;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.bpr.MainActivity;
import com.example.bpr.MyAdapter;
import com.example.bpr.R;
import com.example.bpr.SpinnerStateV0;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;

public class OffersFragment extends Fragment {

    Spinner dropdown;
    boolean isPlay;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_offers, container, false);



        return view;
    }

}