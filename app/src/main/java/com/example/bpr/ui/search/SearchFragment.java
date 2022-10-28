package com.example.bpr.ui.search;

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
import com.example.bpr.MyAdapter;
import com.example.bpr.R;
import com.example.bpr.SpinnerStateV0;

import java.util.ArrayList;
import java.util.List;

public class SearchFragment extends Fragment {

    Spinner dropdown;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        Spinner spinnerOptions = (Spinner) view.findViewById(R.id.optionsFilter);
        Spinner spinnerStores = (Spinner) view.findViewById(R.id.supermarketsFilter);
        Spinner spinnerPrice = (Spinner) view.findViewById(R.id.priceFilter);

        String[] valuesOptions = getResources().getStringArray(R.array.options);
        String[] valuesStores = getResources().getStringArray(R.array.supermarkets);
        String[] valuesPrice = getResources().getStringArray(R.array.price);

        ArrayList<SpinnerStateV0> listOptions = new ArrayList<>();
        ArrayList<SpinnerStateV0> listStores = new ArrayList<>();
        ArrayList<SpinnerStateV0> listPrice = new ArrayList<>();

        for (int i = 0; i < valuesOptions.length; i++) {
            SpinnerStateV0 stateVO = new SpinnerStateV0();
            stateVO.setTitle(valuesOptions[i]);
            stateVO.setSelected(false);
            listOptions.add(stateVO);
        }

        for (int i = 0; i < valuesStores.length; i++) {
            SpinnerStateV0 stateVO = new SpinnerStateV0();
            stateVO.setTitle(valuesStores[i]);
            stateVO.setSelected(false);
            listStores.add(stateVO);
        }

        for (int i = 0; i < valuesPrice.length; i++) {
            SpinnerStateV0 stateVO = new SpinnerStateV0();
            stateVO.setTitle(valuesPrice[i]);
            stateVO.setSelected(false);
            listPrice.add(stateVO);
        }

        MyAdapter adapterOptions = new MyAdapter(getActivity().getApplicationContext(), 0, listOptions);
        spinnerOptions.setAdapter(adapterOptions);

        MyAdapter adapterStores = new MyAdapter(getActivity().getApplicationContext(), 0, listStores);
        spinnerStores.setAdapter(adapterStores);

        MyAdapter adapterPrice = new MyAdapter(getActivity().getApplicationContext(), 0, listPrice);
        spinnerPrice.setAdapter(adapterPrice);

        return view;
    }

}