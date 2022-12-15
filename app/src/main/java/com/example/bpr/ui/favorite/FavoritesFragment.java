package com.example.bpr.ui.favorite;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bpr.Adapters.RecyclerViewFavoriteAdapter;
import com.example.bpr.MVVM.CoopProducts.CoopProductsViewModel;
import com.example.bpr.Objects.CoopProducts;
import com.example.bpr.R;
import com.example.bpr.ui.MainFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class FavoritesFragment extends Fragment implements RecyclerViewFavoriteAdapter.OnButtonListener, RecyclerViewFavoriteAdapter.OnAddCartListener{

    Spinner dropdown;
    boolean isPlay;
    // get from firebase


    RecyclerView recyclerView;
    RecyclerViewFavoriteAdapter adapter;
    private ArrayList<CoopProducts> favs = new ArrayList<>();
    private CoopProductsViewModel coopProductsViewModel;
    private FirebaseFirestore dataB = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth;
    private TextView profileIndicator;


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorites, container, false);
        mAuth = FirebaseAuth.getInstance();
        coopProductsViewModel = ViewModelProviders.of(this).get(CoopProductsViewModel.class);

        profileIndicator = view.findViewById(R.id.profileIDIndicator);
        profileIndicator.setText("Currently signed in as: " + MainFragment.profileName);


        dataB.collection("Users")
                .document(mAuth.getUid())
                .collection("Profiles")
                .document(MainFragment.profileID)
                .collection("Favorites")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
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
                        initRecycler(view);
                    }
                } else {
                    task.getException();
                }
            }
        });

        recyclerView = view.findViewById(R.id.recyclerviewlist);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        adapter = new RecyclerViewFavoriteAdapter(getContext(),favs, FavoritesFragment.this::onButtonClick, this::onAddButtonClick);
        recyclerView.setAdapter(adapter);

        return view;
    }

    public void initRecycler(View view){
        recyclerView = view.findViewById(R.id.recyclerviewlist);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        adapter = new RecyclerViewFavoriteAdapter(getContext(),favs, FavoritesFragment.this::onButtonClick, FavoritesFragment.this::onAddButtonClick);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onButtonClick(int position) {
        dataB.collection("Users")
                .document(mAuth.getUid())
                .collection("Profiles")
                .document(MainFragment.profileID)
                .collection("Favorites")
                .whereEqualTo("navn",favs.get(position).navn)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                if (task.isSuccessful() && !task.getResult().isEmpty()){
                    DocumentSnapshot snap  = task.getResult().getDocuments().get(0);
                    String dID = snap.getId();
                    dataB.collection("Users")
                            .document(mAuth.getUid())
                            .collection("Profiles")
                            .document(MainFragment.profileID)
                            .collection("Favorites")
                            .document(dID).delete()
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    favs.remove(position);
                                    adapter.notifyDataSetChanged();
                                }
                            });
                }
            }
        });
        Toast.makeText(getContext(), "Product removed from favorite list", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onAddButtonClick(int position) {
        dataB.collection("Users")
                .document(mAuth.getUid())
                .collection("Shopping List")
                .whereEqualTo("navn",favs.get(position).navn)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful() && !task.getResult().isEmpty()){
                            DocumentSnapshot snap  = task.getResult().getDocuments().get(0);
                            String dID = snap.getId();
                            dataB.collection("Users")
                                    .document(mAuth.getUid())
                                    .collection("Shopping List")
                                    .document(dID).update("amount", FieldValue.increment(1));

                            dataB.collection("Users")
                                    .document(mAuth.getUid())
                                    .collection("Shopping List")
                                    .document(dID).update("check",false);
                        }
                        else {
                            dataB.collection("Users")
                                    .document(mAuth.getUid())
                                    .collection("Shopping List")
                                    .add(favs.get(position))
                                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                        @Override
                                        public void onSuccess(DocumentReference documentReference) {
                                            dataB.collection("Users")
                                                    .document(mAuth.getUid())
                                                    .collection("Shopping List")
                                                    .document(documentReference.getId()).update("check",false);
                                        }
                                    });
                        }
                    }
                });
        Toast.makeText(getContext(), "Product added to shopping cart", Toast.LENGTH_SHORT).show();
    }
}