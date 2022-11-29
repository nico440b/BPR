package com.example.bpr.ui.list;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bpr.Adapters.RecyclerViewListAdapter;
import com.example.bpr.Objects.CoopProducts;
import com.example.bpr.Objects.ShoppingCart;
import com.example.bpr.R;

import java.util.ArrayList;
import java.util.List;

public class ListFragment extends Fragment {

    ShoppingCart shoppingCart = new ShoppingCart();
    RecyclerView recyclerView;
    RecyclerViewListAdapter adapter;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);

        CoopProducts coopProducts = new CoopProducts();
        coopProducts.navn = "caca";



        recyclerView = view.findViewById(R.id.recyclerviewlist);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        adapter = new RecyclerViewListAdapter(shoppingCart.getCoopProducts());
        recyclerView.setAdapter(adapter);


        return view;
    }



}