package com.example.bpr.ui.offers;

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
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bpr.Adapters.RecyclerViewFavoriteAdapter;
import com.example.bpr.Adapters.RecyclerViewListAdapter;
import com.example.bpr.MVVM.CoopProducts.CoopProductsViewModel;
import com.example.bpr.MainActivity;
import com.example.bpr.MyAdapter;
import com.example.bpr.Objects.CoopProducts;
import com.example.bpr.Objects.FavoriteList;
import com.example.bpr.Objects.ShoppingCart;
import com.example.bpr.R;
import com.example.bpr.SpinnerStateV0;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;

public class OffersFragment extends Fragment implements RecyclerViewFavoriteAdapter.OnButtonListener{

    Spinner dropdown;
    boolean isPlay;
    // get from firebase
    FavoriteList favoriteList = new FavoriteList();

    RecyclerView recyclerView;
    RecyclerViewFavoriteAdapter adapter;
    private CoopProductsViewModel coopProductsViewModel;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_offers, container, false);

        coopProductsViewModel = ViewModelProviders.of(this).get(CoopProductsViewModel.class);
        favoriteList.coopProducts = coopProductsViewModel.getProducts();

        recyclerView = view.findViewById(R.id.recyclerviewlist);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        adapter = new RecyclerViewFavoriteAdapter(getContext(),favoriteList.coopProducts.getValue(), this::onButtonClick);
        recyclerView.setAdapter(adapter);

        return view;
    }

    @Override
    public void onButtonClick(int position) {
        favoriteList.coopProducts.getValue().remove(favoriteList.coopProducts.getValue().get(position));
        Log.e("deleted product: ", favoriteList.coopProducts.getValue().get(position).navn);
    }

}