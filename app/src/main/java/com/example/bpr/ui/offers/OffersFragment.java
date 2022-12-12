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
import com.example.bpr.ui.MainFragment;
import com.example.bpr.ui.list.ListFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class OffersFragment extends Fragment implements RecyclerViewFavoriteAdapter.OnButtonListener{

    Spinner dropdown;
    boolean isPlay;
    // get from firebase
    FavoriteList favoriteList = new FavoriteList();

    RecyclerView recyclerView;
    RecyclerViewFavoriteAdapter adapter;
    private ArrayList<CoopProducts> favs = new ArrayList<>();
    private CoopProductsViewModel coopProductsViewModel;
    private FirebaseFirestore dataB = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_offers, container, false);
        mAuth = FirebaseAuth.getInstance();
        coopProductsViewModel = ViewModelProviders.of(this).get(CoopProductsViewModel.class);
        favoriteList.coopProducts = coopProductsViewModel.getProducts();

        dataB.collection("Users")
                .document(mAuth.getUid())
                .collection("Profiles")
                .document(MainFragment.profileID)
                .collection("Favorites")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    for(QueryDocumentSnapshot document : task.getResult()){

                        CoopProducts product = new CoopProducts();

                        product.setEan(document.getString("Ean"));
                        product.setAmount(document.getDouble("amount"));
                        product.setNavn(document.getString("navn"));
                        product.setNavn2(document.getString("navn2"));
                        product.setLatitude(document.getDouble("latitude"));
                        product.setLongitude(document.getDouble("longitude"));
                        product.setPris(document.getDouble("pris"));
                        product.setStore(document.getString("store"));

                        favs.add(product);

                        recyclerView = view.findViewById(R.id.recyclerviewlist);
                        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
                        adapter = new RecyclerViewFavoriteAdapter(getContext(),favs, OffersFragment.this::onButtonClick);
                        recyclerView.setAdapter(adapter);



                    }
                } else {
                    task.getException();
                }
            }
        });


        recyclerView = view.findViewById(R.id.recyclerviewlist);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        adapter = new RecyclerViewFavoriteAdapter(getContext(),favs, OffersFragment.this::onButtonClick);
        recyclerView.setAdapter(adapter);

        return view;
    }

    @Override
    public void onButtonClick(int position) {
        favoriteList.coopProducts.getValue().remove(favoriteList.coopProducts.getValue().get(position));
        Log.e("deleted product: ", favoriteList.coopProducts.getValue().get(position).navn);
    }

}