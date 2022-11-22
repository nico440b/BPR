package com.example.bpr.ui.search;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
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
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bpr.Adapters.RecyclerViewAdapter;
import com.example.bpr.MVVM.CoopProducts.CoopProductsViewModel;
import com.example.bpr.MainActivity;
import com.example.bpr.MyAdapter;
import com.example.bpr.NetworkImpl;
import com.example.bpr.Objects.CoopProducts;
import com.example.bpr.R;
import com.example.bpr.SpinnerStateV0;
import com.example.bpr.VolleyCallBack;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;

public class SearchFragment extends Fragment {

    private Spinner dropdown;
    private boolean isPlay = false;
    private RecyclerView recyclerView;
    private RecyclerViewAdapter adapter;
    private NetworkImpl networkImpl = new NetworkImpl();
    private List<CoopProducts> products = new ArrayList<>();
    private CoopProductsViewModel coopProductsViewModel;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);

//        MaterialButton addToCart;
//        addToCart = view.findViewById(R.id.addToCartButton);
//
//        MaterialButton favorite;
//        favorite = view.findViewById(R.id.favoriteButton);
//        favorite.setBackgroundResource(R.drawable.ic_baseline_favorite_border_24);

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

//        addToCart.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                // functionality
//                Toast.makeText(getActivity().getApplicationContext(), "Product added to shopping cart", Toast.LENGTH_SHORT).show();
//            }
//        });
//
//        favorite.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if(isPlay){
//                    view.setBackgroundResource(R.drawable.ic_baseline_favorite_border_24);
//                }
//                else {
//                    view.setBackgroundResource(R.drawable.ic_baseline_favorite_24);
//                }
//                isPlay = !isPlay;
//                Toast.makeText(getActivity().getApplicationContext(), "Added to fav", Toast.LENGTH_SHORT).show();
//            }
//        });



        coopProductsViewModel = ViewModelProviders.of(this).get(CoopProductsViewModel.class);
        coopProductsViewModel.getProducts().observe(getViewLifecycleOwner(), new Observer<List<CoopProducts>>() {
            @Override
            public void onChanged(List<CoopProducts> coopProducts) {
                Toast.makeText(view.getContext(), "Changed", Toast.LENGTH_SHORT).show();
                products = coopProducts;
                Log.e("Rest Respone", products.get(0).navn);
                updateView();
            }
        });


        recyclerView = view.findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        adapter = new RecyclerViewAdapter(products);
        recyclerView.setAdapter(adapter);

        return view;
    }

    public void updateView(){
        adapter = new RecyclerViewAdapter(products);
        recyclerView.setAdapter(adapter);
    }



    //-----------MAYBE USE SHARED PREFERENCES FOR STORING FAVORITES------------//
//    public void addToFav(String name, String brand, int amount){
//        SharedPreferences sharedPreferences = getContext().getSharedPreferences("SharedPref", Context.MODE_PRIVATE);
//        SharedPreferences.Editor editor = sharedPreferences.edit();
//        editor.putString("name", name);
//        editor.putString("brand", name);
//        editor.putInt("amount", amount);
//        editor.commit();
//    }

}