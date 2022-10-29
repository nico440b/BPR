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

        MaterialButton addToCart;
        addToCart = view.findViewById(R.id.addToCartButton);

        MaterialButton favorite;
        favorite = view.findViewById(R.id.favoriteButton);
        favorite.setBackgroundResource(R.drawable.ic_baseline_favorite_border_24);

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

        addToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //functionality
                Toast.makeText(getActivity().getApplicationContext(), "Product added to the shopping cart", Toast.LENGTH_SHORT).show();
            }
        });

        favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //functionality
                if(isPlay){
                    view.setBackgroundResource(R.drawable.ic_baseline_favorite_border_24);
                }
                else {
                    view.setBackgroundResource(R.drawable.ic_baseline_favorite_24);
                }
                isPlay = !isPlay;
                Toast.makeText(getActivity().getApplicationContext(), "Added to favorites", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

}