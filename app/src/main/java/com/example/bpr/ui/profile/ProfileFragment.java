package com.example.bpr.ui.profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.bpr.MainActivity;
import com.example.bpr.R;
import com.example.bpr.ui.MainFragment;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

public class ProfileFragment extends Fragment {

    Spinner dropdown;
    private FirebaseAuth mAuth;
    private String profile;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        profile = MainFragment.profileName;
        mAuth = FirebaseAuth.getInstance();
        TextView txt = view.findViewById(R.id.text_profile);
        txt.setText(profile);

        return view;
    }

}