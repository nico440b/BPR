package com.example.bpr.ui.list;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bpr.Adapters.RecyclerViewListAdapter;
import com.example.bpr.MVVM.CoopProducts.CoopProductsViewModel;
import com.example.bpr.Objects.CoopProducts;
import com.example.bpr.R;
import com.example.bpr.ui.MainFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ListFragment extends Fragment implements RecyclerViewListAdapter.OnButtonListener {



    RecyclerView recyclerView;
    RecyclerViewListAdapter adapter;
    Double sum = 0.0;
    private CoopProductsViewModel coopProductsViewModel;
    private FirebaseFirestore dataB = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth;
    public FirebaseUser user;
    private TextView totalPrice;
    private View view;
    private Button addBtn, subBtn;
    private TextView profileIndicator;

    private List<CoopProducts> cart = new ArrayList<>();

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_list, container, false);
        mAuth = FirebaseAuth.getInstance();
        totalPrice = view.findViewById(R.id.totalPrice);
        addBtn = view.findViewById(R.id.addBtn);
        subBtn = view.findViewById(R.id.removeBtn);
        profileIndicator = view.findViewById(R.id.profileIDIndicator);
        profileIndicator.setText("Currently signed in as: " + MainFragment.profileName);


        coopProductsViewModel = ViewModelProviders.of(this).get(CoopProductsViewModel.class);
        dataB.collection("Users")
                .document(mAuth.getUid())
                .collection("Shopping List")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    for(QueryDocumentSnapshot document : task.getResult()){
                        totalPrice.setVisibility(View.INVISIBLE);

                        CoopProducts product = new CoopProducts();

                        product.setEan(document.getString("Ean"));
                        product.setAmount(document.getDouble("amount"));
                        product.setNavn(document.getString("navn"));
                        product.setNavn2(document.getString("navn2"));
                        product.setLatitude(document.getDouble("latitude"));
                        product.setLongitude(document.getDouble("longitude"));
                        product.setPris(document.getDouble("pris"));
                        product.setStore(document.getString("store"));

                        cart.add(product);

                        recyclerView = view.findViewById(R.id.recyclerviewlist);
                        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
                        adapter = new RecyclerViewListAdapter(getContext(),cart, ListFragment.this);
                        recyclerView.setAdapter(adapter);
                        totalPrice.setText(Double.toString(calculateTotalPrice(cart)) + " kr");
                        if (calculateTotalPrice(cart)!=0){
                            totalPrice.setVisibility(View.VISIBLE);
                        }
                    }
                } else {
                    task.getException();
                }
            }
        });
        return view;
    }

    public void getShoppingList(){
        dataB.collection("Users").document(mAuth.getUid())
                .collection("Shopping List")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    for(QueryDocumentSnapshot document : task.getResult()){
                        totalPrice.setVisibility(View.INVISIBLE);
                        CoopProducts product = new CoopProducts();

                        product.setEan(document.getString("Ean"));
                        product.setAmount(document.getDouble("amount"));
                        product.setNavn(document.getString("navn"));
                        product.setNavn2(document.getString("navn2"));
                        product.setLatitude(document.getDouble("latitude"));
                        product.setLongitude(document.getDouble("longitude"));
                        product.setPris(document.getDouble("pris"));
                        product.setStore(document.getString("store"));

                        cart.add(product);

                        adapter.notifyDataSetChanged();
                        recyclerView.setAdapter(adapter);
                        totalPrice.setText(Double.toString(calculateTotalPrice(cart)) + " kr");
                        if (calculateTotalPrice(cart)!=0){
                            totalPrice.setVisibility(View.VISIBLE);
                        }
                    }
                } else {
                    task.getException();
                }
            }
        });
    }

    public double calculateTotalPrice(List<CoopProducts> products){
        sum = 0.0;
        if(products!=null){
        for(int i=0;i<products.size();i++){
            sum+= products.get(i).pris * products.get(i).amount;
        }}
        return sum;
    }


    @Override
    public void removeItem(int position) {
      dataB.collection("Users")
              .document(mAuth.getUid())
              .collection("Shopping List")
              .whereEqualTo("navn",cart.get(position).navn)
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
                          .document(dID).delete()
                          .addOnSuccessListener(new OnSuccessListener<Void>() {
                              @Override
                              public void onSuccess(Void unused) {
                                  cart.remove(position);
                                  adapter = new RecyclerViewListAdapter(getContext(),cart, ListFragment.this);
                                  recyclerView.setAdapter(adapter);
                                  totalPrice.setText(Double.toString(calculateTotalPrice(cart)) + " kr");
                              }
                          });
              }
          }
      });
        Toast.makeText(getContext(), "Product removed from shopping cart", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void checkItem(int position){
        dataB.collection("Users")
                .document(mAuth.getUid())
                .collection("Shopping List")
                .whereEqualTo("navn",cart.get(position).navn).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                if (task.isSuccessful() && !task.getResult().isEmpty()){
                    DocumentSnapshot snap  = task.getResult().getDocuments().get(0);
                    String dID = snap.getId();
                    dataB.collection("Users")
                            .document(mAuth.getUid())
                            .collection("Shopping List")
                            .document(dID).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            if (documentSnapshot.getBoolean("check")== false){
                                dataB.collection("Users")
                                        .document(mAuth.getUid())
                                        .collection("Shopping List")
                                        .document(documentSnapshot.getId()).update("check",true);
                                        totalPrice.setText(Double.toString(calculateTotalPrice(cart)-cart
                                                .get(position)
                                                .pris*cart
                                                .get(position)
                                                .amount) + " kr");
                                }
                            else{
                                dataB.collection("Users")
                                        .document(mAuth.getUid())
                                        .collection("Shopping List")
                                        .document(documentSnapshot.getId()).update("check",false);
                                adapter = new RecyclerViewListAdapter(getContext(),cart, ListFragment.this);
                                recyclerView.setAdapter(adapter);
                                totalPrice.setText(Double.toString(calculateTotalPrice(cart)) + " kr");
                            }
                        }
                    });
                }
            }
        });
    }

    @Override
    public void addItem(int position) {
        dataB.collection("Users")
                .document(mAuth.getUid())
                .collection("Shopping List")
                .whereEqualTo("navn",cart.get(position).navn)
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
                                .document(dID).update("amount", FieldValue.increment(1))
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        cart.clear();
                                        getShoppingList();
                                    }
                                });
                }
            }
        });
    }

    @Override

    public void subItem(int position) {
        dataB.collection("Users")
                .document(mAuth.getUid())
                .collection("Shopping List")
                .whereEqualTo("navn",cart.get(position).navn)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                if (task.isSuccessful() && !task.getResult().isEmpty()){
                    DocumentSnapshot snap  = task.getResult().getDocuments().get(0);
                    String dID = snap.getId();
                    if (snap.getLong("amount")!=1){
                        dataB.collection("Users")
                                .document(mAuth.getUid())
                                .collection("Shopping List")
                                .document(dID).update("amount", FieldValue.increment(-1))
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        cart.clear();
                                        getShoppingList();

                                    }
                                });
                    }else {
                        dataB.collection("Users")
                                .document(mAuth.getUid())
                                .collection("Shopping List")
                                .document(dID)
                                .delete()
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                cart.clear();
                                getShoppingList();
                                adapter = new RecyclerViewListAdapter(getContext(),cart, ListFragment.this);
                                recyclerView.setAdapter(adapter);
                                totalPrice.setText(Double.toString(calculateTotalPrice(cart)) + " kr");
                            }
                        });
                    }
                }
            }
        });
    }
}