package com.example.bpr.ui.search;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bpr.Adapters.RecyclerViewAdapter;
import com.example.bpr.Adapters.RecyclerViewListAdapter;
import com.example.bpr.LocationTrack;
import com.example.bpr.MVVM.CoopProducts.CoopProductsViewModel;
import com.example.bpr.MainActivity;
import com.example.bpr.MyAdapter;
import com.example.bpr.NetworkImpl;
import com.example.bpr.Objects.CoopProducts;
import com.example.bpr.Objects.FavoriteList;
import com.example.bpr.Objects.ShoppingCart;
import com.example.bpr.R;
import com.example.bpr.SpinnerStateV0;
import com.example.bpr.VolleyCallBack;
import com.example.bpr.ui.list.ListFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class SearchFragment extends Fragment implements RecyclerViewAdapter.OnButtonListener, RecyclerViewAdapter.OnFavButtonListener {
    private RecyclerView recyclerView;
    private RecyclerViewAdapter adapter;
    private NetworkImpl networkImpl = new NetworkImpl();
    private List<CoopProducts> products = new ArrayList<>();
    private CoopProductsViewModel coopProductsViewModel;
    private SearchView search;
    private FirebaseFirestore dataB = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth;
    public FirebaseUser user;
    ShoppingCart shoppingCart = new ShoppingCart();
    FavoriteList favoriteList = new FavoriteList();
    List<CoopProducts> filteredProductsAfterStore = new ArrayList<>();
    List<CoopProducts> filteredProductsAfterOptions = new ArrayList<>();
    List<CoopProducts> filteredProductsAfterDistance = new ArrayList<>();

    TextView textViewStores;
    boolean[] selectedStores;
    ArrayList<Integer> langListStores = new ArrayList<>();
    String[] langArrayStores = {"SuperBrugsen", "Kvickly", "Dagli' Brugsen", "Irma", "Coop365", "Fakta"};

    TextView[] textViewDistance = new TextView[1];
    boolean[] selectedDistance;
    int[] choice = new int[1];
    ArrayList<Integer> langListDistance = new ArrayList<>();
    String[] langArrayDistance = {"1 km", "2 km", "5 km", "7 km", "10 km"};

    TextView textViewOptions;
    boolean[] selectedOptions;
    ArrayList<Integer> langList = new ArrayList<>();
    String[] langArray = {"Cheapest", "Øko"};
    List<CoopProducts> results = new ArrayList<>();




    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);



        textViewStores = view.findViewById(R.id.textViewStores);
        selectedStores = new boolean[langArrayStores.length];

        textViewOptions = view.findViewById(R.id.textViewOptions);
        selectedOptions = new boolean[langArray.length];

        textViewDistance[0] = view.findViewById(R.id.textViewDistance);

        FirebaseApp.initializeApp(getActivity());
        mAuth = FirebaseAuth.getInstance();

        coopProductsViewModel = ViewModelProviders.of(this).get(CoopProductsViewModel.class);
//        shoppingCart.coopProducts = coopProductsViewModel.getProducts();
//        favoriteList.coopProducts = coopProductsViewModel.getProducts();
        coopProductsViewModel.getProducts().observe(getViewLifecycleOwner(), new Observer<List<CoopProducts>>() {
            @Override
            public void onChanged(List<CoopProducts> coopProducts) {
                Toast.makeText(view.getContext(), "Changed", Toast.LENGTH_SHORT).show();
                products = coopProducts;
//                Log.e("Rest Respone", products.get(0).navn);
                updateView(products);
            }
        });



        recyclerView = view.findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        search = view.findViewById(R.id.searchBar);

        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (adapter!= null){
                    adapter.getFilter().filter(newText);
                }

                return false;
            }
        });

        //--------STORES

        textViewStores.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Stores");
                builder.setCancelable(false);
                builder.setMultiChoiceItems(langArrayStores, selectedStores, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i, boolean b) {
                        if(b){
                            langListStores.add(i);
                            Collections.sort(langListStores);
                        }
                        else{
                            langListStores.remove(Integer.valueOf(i));
                        }
                    }
                });
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i){
                        StringBuilder stringBuilder = new StringBuilder();
                        for (int j = 0; j < langListStores.size(); j++) {
                            stringBuilder.append(langArrayStores[langListStores.get(j)]);
                            if (j != langListStores.size() - 1) {
                                stringBuilder.append(", ");
                            }
                        }

                        for (int j = 0; j < selectedOptions.length; j++) {
                            selectedOptions[j] = false;
                            langList.clear();
                            textViewOptions.setText("");
                        }

                        langListDistance.clear();
                        textViewDistance[0].setText("");

                        textViewStores.setText(stringBuilder.toString());

                        switch (Arrays.toString(selectedStores)){
                            case "[false, false, false, false, false, false]":
                                updateView(products);
                                break;
                            case "[true, false, false, false, false, false]":
                                filteredProductsAfterStore.clear();
                                filteredProductsAfterStore.addAll(getStore(products, "SuperBrugsen"));
                                updateView(filteredProductsAfterStore);
                                break;
                            case "[false, true, false, false, false, false]":
                                filteredProductsAfterStore.clear();
                                filteredProductsAfterStore.addAll(getStore(products, "Kvickly"));
                                updateView(filteredProductsAfterStore);
                                break;
                            case "[false, false, true, false, false, false]":
                                filteredProductsAfterStore.clear();
                                filteredProductsAfterStore.addAll(getStore(products, "Dagli' Brugsen"));
                                updateView(filteredProductsAfterStore);
                                break;
                            case "[false, false, false, true, false, false]":
                                filteredProductsAfterStore.clear();
                                filteredProductsAfterStore.addAll(getStore(products, "Irma"));
                                updateView(filteredProductsAfterStore);
                                break;
                            case "[false, false, false, false, true, false]":
                                filteredProductsAfterStore.clear();
                                filteredProductsAfterStore.addAll(getStore(products, "Coop 365"));
                                updateView(filteredProductsAfterStore);
                                Log.e("size:", Integer.toString(filteredProductsAfterStore.size()));
                                break;
                            case "[false, false, false, false, false, true]":
                                filteredProductsAfterStore.clear();
                                filteredProductsAfterStore.addAll(getStore(products, "365discount"));
                                updateView(filteredProductsAfterStore);
                                Log.e("size:", Integer.toString(filteredProductsAfterStore.size()));
                                break;
                            case "[true, true, false, false, false, false]":
                                filteredProductsAfterStore.clear();
                                filteredProductsAfterStore.addAll(getStore(products, "SuperBrugsen"));
                                filteredProductsAfterStore.addAll(getStore(products, "Kvickly"));
                                updateView(filteredProductsAfterStore);
                                break;
                            case "[false, true, true, false, false, false]":
                                filteredProductsAfterStore.clear();
                                filteredProductsAfterStore.addAll(getStore(products, "Kvickly"));
                                filteredProductsAfterStore.addAll(getStore(products, "Dagli' Brugsen"));
                                updateView(filteredProductsAfterStore);
                                break;
                            case "[false, false, true, true, false, false]":
                                filteredProductsAfterStore.clear();
                                filteredProductsAfterStore.addAll(getStore(products, "Dagli' Brugsen"));
                                filteredProductsAfterStore.addAll(getStore(products, "Irma"));
                                updateView(filteredProductsAfterStore);
                                break;
                            case "[false, false, false, true, true, false]":
                                filteredProductsAfterStore.clear();
                                filteredProductsAfterStore.addAll(getStore(products, "Irma"));
                                filteredProductsAfterStore.addAll(getStore(products, "Coop 365"));
                                updateView(filteredProductsAfterStore);
                                break;
                            case "[false, false, false, false, true, true]":
                                filteredProductsAfterStore.clear();
                                filteredProductsAfterStore.addAll(getStore(products, "Coop 365"));
                                filteredProductsAfterStore.addAll(getStore(products, "365discount"));
                                updateView(filteredProductsAfterStore);
                                Log.e("size:", Integer.toString(filteredProductsAfterStore.size()));
                                break;
                            case "[true, true, true, false, false, false]":
                                filteredProductsAfterStore.clear();
                                filteredProductsAfterStore.addAll(getStore(products, "SuperBrugsen"));
                                filteredProductsAfterStore.addAll(getStore(products, "Kvickly"));
                                filteredProductsAfterStore.addAll(getStore(products, "Dagli' Brugsen"));
                                updateView(filteredProductsAfterStore);
                                break;
                            case "[false, true, true, true, false, false]":
                                filteredProductsAfterStore.clear();
                                filteredProductsAfterStore.addAll(getStore(products, "Kvickly"));
                                filteredProductsAfterStore.addAll(getStore(products, "Dagli' Brugsen"));
                                filteredProductsAfterStore.addAll(getStore(products, "Irma"));
                                updateView(filteredProductsAfterStore);
                                break;
                            case "[false, false, true, true, true, false]":
                                filteredProductsAfterStore.clear();
                                filteredProductsAfterStore.addAll(getStore(products, "Dagli' Brugsen"));
                                filteredProductsAfterStore.addAll(getStore(products, "Irma"));
                                filteredProductsAfterStore.addAll(getStore(products, "Coop 365"));
                                updateView(filteredProductsAfterStore);
                                break;
                            case "[false, false, false, true, true, true]":
                                filteredProductsAfterStore.clear();
                                filteredProductsAfterStore.addAll(getStore(products, "Irma"));
                                filteredProductsAfterStore.addAll(getStore(products, "Coop 365"));
                                filteredProductsAfterStore.addAll(getStore(products, "365discount"));
                                updateView(filteredProductsAfterStore);
                                break;
                            case "[true, true, true, true, false, false]":
                                filteredProductsAfterStore.clear();
                                filteredProductsAfterStore.addAll(getStore(products, "SuperBrugsen"));
                                filteredProductsAfterStore.addAll(getStore(products, "Kvickly"));
                                filteredProductsAfterStore.addAll(getStore(products, "Dagli' Brugsen"));
                                filteredProductsAfterStore.addAll(getStore(products, "Irma"));
                                updateView(filteredProductsAfterStore);
                                break;
                            case "[false, true, true, true, true, false]":
                                filteredProductsAfterStore.clear();
                                filteredProductsAfterStore.addAll(getStore(products, "Kvickly"));
                                filteredProductsAfterStore.addAll(getStore(products, "Dagli' Brugsen"));
                                filteredProductsAfterStore.addAll(getStore(products, "Irma"));
                                filteredProductsAfterStore.addAll(getStore(products, "Coop 365"));
                                updateView(filteredProductsAfterStore);
                                break;
                            case "[false, false, true, true, true, true]":
                                filteredProductsAfterStore.clear();
                                filteredProductsAfterStore.addAll(getStore(products, "Dagli' Brugsen"));
                                filteredProductsAfterStore.addAll(getStore(products, "Irma"));
                                filteredProductsAfterStore.addAll(getStore(products, "Coop 365"));
                                filteredProductsAfterStore.addAll(getStore(products, "365discount"));
                                updateView(filteredProductsAfterStore);
                                break;
                            case "[true, true, true, true, true, false]":
                                filteredProductsAfterStore.clear();
                                filteredProductsAfterStore.addAll(getStore(products, "SuperBrugsen"));
                                filteredProductsAfterStore.addAll(getStore(products, "Kvickly"));
                                filteredProductsAfterStore.addAll(getStore(products, "Dagli' Brugsen"));
                                filteredProductsAfterStore.addAll(getStore(products, "Irma"));
                                filteredProductsAfterStore.addAll(getStore(products, "Coop 365"));
                                updateView(filteredProductsAfterStore);
                                break;
                            case "[false, true, true, true, true, true]":
                                filteredProductsAfterStore.clear();
                                filteredProductsAfterStore.addAll(getStore(products, "Kvickly"));
                                filteredProductsAfterStore.addAll(getStore(products, "Dagli' Brugsen"));
                                filteredProductsAfterStore.addAll(getStore(products, "Irma"));
                                filteredProductsAfterStore.addAll(getStore(products, "Coop 365"));
                                filteredProductsAfterStore.addAll(getStore(products, "365discount"));
                                updateView(filteredProductsAfterStore);
                                break;
                            case "[true, true, true, true, true, true]":
                                filteredProductsAfterStore.clear();
                                filteredProductsAfterStore.addAll(getStore(products, "SuperBrugsen"));
                                filteredProductsAfterStore.addAll(getStore(products, "Kvickly"));
                                filteredProductsAfterStore.addAll(getStore(products, "Dagli' Brugsen"));
                                filteredProductsAfterStore.addAll(getStore(products, "Irma"));
                                filteredProductsAfterStore.addAll(getStore(products, "Coop 365"));
                                filteredProductsAfterStore.addAll(getStore(products, "365discount"));
                                updateView(filteredProductsAfterStore);
                                break;

                            default: Log.e("msg", "default");
                        }
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // dismiss dialog
                        dialogInterface.dismiss();
                    }
                });
                builder.setNeutralButton("Clear all", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        for (int j = 0; j < selectedStores.length; j++) {
                            selectedStores[j] = false;
                            langListStores.clear();
                            textViewStores.setText("");
                        }

                        for (int j = 0; j < selectedOptions.length; j++) {
                            selectedOptions[j] = false;
                            langList.clear();
                            textViewOptions.setText("");
                        }

                        langListDistance.clear();
                        textViewDistance[0].setText("");

                        updateView(products);
                    }
                });
                builder.show();
            }
        });


        //////////////////OPTIONS
        textViewOptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Options");
                builder.setCancelable(false);
                builder.setMultiChoiceItems(langArray, selectedOptions, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i, boolean b) {
                        if(b){
                            langList.add(i);
                            Collections.sort(langList);
                        }
                        else{
                            langList.remove(Integer.valueOf(i));
                        }
                    }
                });
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i){
                        StringBuilder stringBuilder = new StringBuilder();
                        for (int j = 0; j < langList.size(); j++) {
                            stringBuilder.append(langArray[langList.get(j)]);
                            if (j != langList.size() - 1) {
                                stringBuilder.append(", ");
                            }
                        }
                        langListDistance.clear();
                        textViewDistance[0].setText("");

                        textViewOptions.setText(stringBuilder.toString());

                        if(filteredProductsAfterStore.size()==0){
                            for (int j = 0; j < selectedStores.length; j++) {
                                selectedStores[j] = false;
                                langListStores.clear();
                                textViewStores.setText("");
                            }
                            filteredProductsAfterStore.addAll(products);
                        }

                        switch (Arrays.toString(selectedOptions)){
                            case "[false, false]":
                                filteredProductsAfterOptions.clear();
                                updateView(filteredProductsAfterStore);
                                break;
                            case "[false, true]":
                                filteredProductsAfterOptions.clear();
                                filteredProductsAfterOptions.addAll(getOko(filteredProductsAfterStore));
                                updateView(filteredProductsAfterOptions);
                                break;
                            case "[true, false]":
                                filteredProductsAfterOptions.clear();
                                filteredProductsAfterOptions.addAll(getCheapest(filteredProductsAfterStore));
                                updateView(filteredProductsAfterOptions);
                                break;
                            case "[true, true]":
                                filteredProductsAfterOptions.clear();
                                filteredProductsAfterOptions.addAll(getOko(filteredProductsAfterStore));
                                filteredProductsAfterOptions.addAll(getCheapest(filteredProductsAfterStore));
                                updateView(filteredProductsAfterOptions);
                                break;
                            default: Log.e("msg", "default");
                        }
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                builder.setNeutralButton("Clear all", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        for (int j = 0; j < selectedOptions.length; j++) {
                            selectedOptions[j] = false;
                            langList.clear();
                            textViewOptions.setText("");
                        }
                        langListDistance.clear();
                        textViewDistance[0].setText("");
                        updateView(filteredProductsAfterStore);
                        filteredProductsAfterStore.clear();
                    }
                });
                builder.show();
            }
        });

        /////////////////// DISTANCE

        textViewDistance[0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Distance");
                builder.setCancelable(false);

                builder.setSingleChoiceItems(langArrayDistance, -1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                            langListDistance.add(i);
                            choice[0] = i;
                            Log.e("choice", Integer.toString(choice[0]));
                    }
                });

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i){
                        StringBuilder stringBuilder = new StringBuilder();
                        textViewDistance[0].setText(stringBuilder.toString());

                        if(filteredProductsAfterOptions.size()==0){
                            if(filteredProductsAfterStore.size()==0){
                                for (int j = 0; j < selectedStores.length; j++) {
                                    selectedStores[j] = false;
                                    langListStores.clear();
                                    textViewStores.setText("");
                                }
                                filteredProductsAfterOptions.addAll(products);
                            }
                            else
                            {
                                filteredProductsAfterOptions.addAll(filteredProductsAfterStore);
                            }
                        }


                        switch (choice[0]){
                            case 0:
                                filteredProductsAfterDistance.clear();
                                textViewDistance[0].setText("1 km");
                                filteredProductsAfterDistance.addAll(getKm(filteredProductsAfterOptions, 1.0));
                                updateView(filteredProductsAfterDistance);
                                break;
                            case 1:
                                filteredProductsAfterDistance.clear();
                                textViewDistance[0].setText("2 km");
                                filteredProductsAfterDistance.addAll(getKm(filteredProductsAfterOptions, 2.0));
                                updateView(filteredProductsAfterDistance);
                                break;
                            case 2:
                                filteredProductsAfterDistance.clear();
                                textViewDistance[0].setText("5 km");
                                filteredProductsAfterDistance.addAll(getKm(filteredProductsAfterOptions, 5.0));
                                updateView(filteredProductsAfterDistance);
                                break;
                            case 3:
                                filteredProductsAfterDistance.clear();
                                textViewDistance[0].setText("7 km");
                                filteredProductsAfterDistance.addAll(getKm(filteredProductsAfterOptions, 7.0));
                                updateView(filteredProductsAfterDistance);
                                break;
                            case 4:
                                filteredProductsAfterDistance.clear();
                                textViewDistance[0].setText("10 km");
                                filteredProductsAfterDistance.addAll(getKm(filteredProductsAfterOptions, 10.0));
                                Log.e("1000km", Integer.toString(filteredProductsAfterDistance.size()));
                                updateView(filteredProductsAfterDistance);
                                break;
                            default: Log.e("msg", "default");
                        }
                    }


                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                builder.setNeutralButton("Clear all", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                            langListDistance.clear();
                            textViewDistance[0].setText("");
                            updateView(filteredProductsAfterOptions);
                    }
                });
                builder.show();
            }
        });
        return view;
    }

    public void updateView(List<CoopProducts> updatedProducts){
        adapter = new RecyclerViewAdapter(getContext(), updatedProducts, this::onButtonClick, this::onFavButtonClick);
        recyclerView.setAdapter(adapter);
    }


    public List<CoopProducts> getCheapest(List<CoopProducts> allProducts){
        List<CoopProducts> result = new ArrayList<>();
        boolean foundSimilarEan = false;
        for(int i=0; i<allProducts.size();i++){
            for (int j = i + 1; j < allProducts.size(); j++){
                if(allProducts.get(i).ean ==allProducts.get(j).ean){
                    foundSimilarEan = true;
                    Log.e("similar ean", "yup");
                    Log.e("1", allProducts.get(i).navn);
                    Log.e("2", allProducts.get(j).navn);
                    if(allProducts.get(i).pris<allProducts.get(j).pris){
                        result.add(allProducts.get(i));
                    }
                    else{
                        result.add(allProducts.get(j));
                    }
                }
                else{
                    Log.e("no similar ean", "nope");
                }
            }
            if (!foundSimilarEan){
                Log.e("no similar ean found", Boolean.toString(foundSimilarEan));
                result.add(allProducts.get(i));
            }
        }
        return result;
    }

    public List<CoopProducts> getKm(List<CoopProducts> allProducts, double km){
        MainActivity mainActivity = (MainActivity) getActivity();
        List<CoopProducts> result = new ArrayList<>();
        for (int i=0; i<allProducts.size(); i++){
            if(Double.compare(allProducts.get(i).calculateDistanceDouble(mainActivity.locationTrack.loc), km)<0){
                result.add(allProducts.get(i));
            }
        }
        for (int i=0; i<result.size();i++)
            Log.e("distances", result.get(i).calculateDistanceString(mainActivity.locationTrack.loc));
        return result;
    }

    public List<CoopProducts> getStore(List<CoopProducts> allProducts, String storeName){
        List<CoopProducts> result = new ArrayList<>();
        for (int i=0; i<allProducts.size(); i++){
            Log.e("store name", allProducts.get(i).store);
            if(allProducts.get(i).store.toLowerCase().contains(storeName.toLowerCase())){
                result.add(allProducts.get(i));
            }
        }
        Log.e("store", storeName);
        return result;
    }

    public List<CoopProducts> getOko(List<CoopProducts> allProducts){
        List<CoopProducts> result = new ArrayList<>();
        String filter = "øko";
        for (int i=0; i<allProducts.size(); i++){
            if(allProducts.get(i).navn.toLowerCase().contains(filter.toLowerCase()) || allProducts.get(i).navn2.toLowerCase().contains(filter.toLowerCase())){
                result.add(allProducts.get(i));
            }
        }
        return result;
    }

    @Override
    public void onButtonClick(int position) {
        dataB.collection("Users")
                .document(mAuth.getUid())
                .collection("Shopping List")
                .whereEqualTo("navn",products.get(position).navn).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
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
                            .add(products.get(position)).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
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

        Log.e("added product:", products.get(position).navn);
    }

    @Override
    public void onFavButtonClick(int position){
        favoriteList.coopProducts.getValue().add(products.get(position));
        Log.e("added product to fav:", products.get(position).navn);

    }


}