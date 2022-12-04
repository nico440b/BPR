package com.example.bpr.ui.list;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bpr.Adapters.RecyclerViewListAdapter;
import com.example.bpr.MVVM.CoopProducts.CoopProductsViewModel;
import com.example.bpr.Objects.CoopProducts;
import com.example.bpr.Objects.ShoppingCart;
import com.example.bpr.R;

import java.util.ArrayList;
import java.util.List;

public class ListFragment extends Fragment implements RecyclerViewListAdapter.OnButtonListener {

    // get from firebase
    ShoppingCart shoppingCart = new ShoppingCart();

    RecyclerView recyclerView;
    RecyclerViewListAdapter adapter;
    Double sum = 0.0;
    private CoopProductsViewModel coopProductsViewModel;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);

        coopProductsViewModel = ViewModelProviders.of(this).get(CoopProductsViewModel.class);
        shoppingCart.coopProducts = coopProductsViewModel.getProducts();

        TextView totalPrice;
        totalPrice = view.findViewById(R.id.totalPrice);
        totalPrice.setText(Double.toString(calculateTotalPrice(shoppingCart.coopProducts.getValue())) + " kr");

        recyclerView = view.findViewById(R.id.recyclerviewlist);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        adapter = new RecyclerViewListAdapter(getContext(),shoppingCart.coopProducts.getValue(), this::onButtonClick);
        recyclerView.setAdapter(adapter);
        totalPrice.setText(Double.toString(calculateTotalPrice(shoppingCart.coopProducts.getValue())) + " kr");


        return view;
    }

    public double calculateTotalPrice(List<CoopProducts> products){
        if(products!=null){
        for(int i=0;i<products.size();i++){
            sum+= products.get(i).pris;
        }}
        return sum;
    }


    @Override
    public void onButtonClick(int position) {
        shoppingCart.coopProducts.getValue().remove(shoppingCart.coopProducts.getValue().get(position));
        Log.e("deleted product: ", shoppingCart.coopProducts.getValue().get(position).navn);
    }
}