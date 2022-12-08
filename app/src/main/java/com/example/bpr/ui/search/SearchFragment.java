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

public class SearchFragment extends Fragment implements RecyclerViewAdapter.OnButtonListener {
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

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        MainActivity mainActivity = (MainActivity) getActivity();

        FirebaseApp.initializeApp(getActivity());
        mAuth = FirebaseAuth.getInstance();

        coopProductsViewModel = ViewModelProviders.of(this).get(CoopProductsViewModel.class);

        coopProductsViewModel.getProducts().observe(getViewLifecycleOwner(), new Observer<List<CoopProducts>>() {
            @Override
            public void onChanged(List<CoopProducts> coopProducts) {

                products = coopProducts;

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
                adapter.getFilter().filter(newText);
                return false;
            }
        });

        TextView textViewOptions;
        boolean[] selectedOptions;
        ArrayList<Integer> langList = new ArrayList<>();
        String[] langArray = {"Cheapest", "Øko"};
        List<CoopProducts> results = new ArrayList<>();

        textViewOptions = view.findViewById(R.id.textViewOptions);
        selectedOptions = new boolean[langArray.length];
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
                            // concat array value
                            stringBuilder.append(langArray[langList.get(j)]);
                            // check condition
                            if (j != langList.size() - 1) {
                                // When j value  not equal
                                // to lang list size - 1
                                // add comma
                                stringBuilder.append(", ");
                            }
                        }
                        textViewOptions.setText(stringBuilder.toString());

                        switch (Arrays.toString(selectedOptions)){
                            case "[false, false]":
                                updateView(products);
                                break;
                            case "[false, true]":
                                Log.e("msg", "in oko");
                                getOko(products);
                                break;
                            case "[true, false]":
                                Log.e("msg", "in cheapest");
                                getCheapest(products);
                                break;
                            case "[true, true]":
                                getCheapest(products);
                                getOko(products);
                                Log.e("msg", "in cheapest");
                                Log.e("msg", "in closest");
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
                        // use for loop
                        for (int j = 0; j < selectedOptions.length; j++) {
                            // remove all selection
                            selectedOptions[j] = false;
                            // clear language list
                            langList.clear();
                            // clear text view value
                            textViewOptions.setText("");
                        }
                        updateView(products);
                    }
                });
                builder.show();
            }
        });

        //--------STORES

        List<CoopProducts> filteredProducts = new ArrayList<>();

        TextView textViewStores;
        boolean[] selectedStores;
        ArrayList<Integer> langListStores = new ArrayList<>();
        String[] langArrayStores = {"Super Brugsen", "Kvickly", "Dagli' Brugsen", "Irma", "Coop365", "Fakta"};

        textViewStores = view.findViewById(R.id.textViewStores);
        selectedStores = new boolean[langArrayStores.length];
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
                            // concat array value
                            stringBuilder.append(langArrayStores[langListStores.get(j)]);
                            // check condition
                            if (j != langListStores.size() - 1) {
                                // When j value  not equal
                                // to lang list size - 1
                                // add comma
                                stringBuilder.append(", ");
                            }
                        }
                        textViewStores.setText(stringBuilder.toString());

                        switch (Arrays.toString(selectedStores)){
                            case "[true, false, false, false, false, false]":
                                filteredProducts.clear();
                                filteredProducts.addAll(getStore(products, "Super Brugsen"));
                                updateView(filteredProducts);
                                break;
                            case "[false, true, false, false, false, false]":
                                filteredProducts.clear();
                                filteredProducts.addAll(getStore(products, "Kvickly"));
                                updateView(filteredProducts);
                                break;
                            case "[false, false, true, false, false, false]":
                                filteredProducts.clear();
                                filteredProducts.addAll(getStore(products, "Dagli' Brugsen"));
                                updateView(filteredProducts);
                                break;
                            case "[false, false, false, true, false, false]":
                                filteredProducts.clear();
                                filteredProducts.addAll(getStore(products, "Irma"));
                                updateView(filteredProducts);
                                break;
                            case "[false, false, false, false, true, false]":
                                filteredProducts.clear();
                                filteredProducts.addAll(getStore(products, "Coop 365"));
                                updateView(filteredProducts);
                                Log.e("size:", Integer.toString(filteredProducts.size()));
                                break;
                            case "[false, false, false, false, false, true]":
                                filteredProducts.clear();
                                filteredProducts.addAll(getStore(products, "Fakta"));
                                updateView(filteredProducts);
                                Log.e("size:", Integer.toString(filteredProducts.size()));
                                break;
                            case "[true, true, false, false, false, false]":
                                filteredProducts.clear();
                                filteredProducts.addAll(getStore(products, "Super Brugsen"));
                                filteredProducts.addAll(getStore(products, "Kvickly"));
                                updateView(filteredProducts);
                                break;
                            case "[false, true, true, false, false, false]":
                                filteredProducts.clear();
                                filteredProducts.addAll(getStore(products, "Kvickly"));
                                filteredProducts.addAll(getStore(products, "Dagli' Brugsen"));
                                updateView(filteredProducts);
                                break;
                            case "[false, false, true, true, false, false]":
                                filteredProducts.clear();
                                filteredProducts.addAll(getStore(products, "Dagli' Brugsen"));
                                filteredProducts.addAll(getStore(products, "Irma"));
                                updateView(filteredProducts);
                                break;
                            case "[false, false, false, true, true, false]":
                                filteredProducts.clear();
                                filteredProducts.addAll(getStore(products, "Irma"));
                                filteredProducts.addAll(getStore(products, "Coop 365"));
                                updateView(filteredProducts);
                                break;
                            case "[false, false, false, false, true, true]":
                                filteredProducts.clear();
                                filteredProducts.addAll(getStore(products, "Coop 365"));
                                filteredProducts.addAll(getStore(products, "Fakta"));
                                updateView(filteredProducts);
                                Log.e("size:", Integer.toString(filteredProducts.size()));
                                break;
                            case "[true, true, true, false, false, false]":
                                filteredProducts.clear();
                                filteredProducts.addAll(getStore(products, "Super Brugsen"));
                                filteredProducts.addAll(getStore(products, "Kvickly"));
                                filteredProducts.addAll(getStore(products, "Dagli' Brugsen"));
                                updateView(filteredProducts);
                                break;
                            case "[false, true, true, true, false, false]":
                                filteredProducts.clear();
                                filteredProducts.addAll(getStore(products, "Kvickly"));
                                filteredProducts.addAll(getStore(products, "Dagli' Brugsen"));
                                filteredProducts.addAll(getStore(products, "Irma"));
                                updateView(filteredProducts);
                                break;
                            case "[false, false, true, true, true, false]":
                                filteredProducts.clear();
                                filteredProducts.addAll(getStore(products, "Dagli' Brugsen"));
                                filteredProducts.addAll(getStore(products, "Irma"));
                                filteredProducts.addAll(getStore(products, "Coop 365"));
                                updateView(filteredProducts);
                                break;
                            case "[false, false, false, true, true, true]":
                                filteredProducts.clear();
                                filteredProducts.addAll(getStore(products, "Irma"));
                                filteredProducts.addAll(getStore(products, "Coop 365"));
                                filteredProducts.addAll(getStore(products, "Fakta"));
                                updateView(filteredProducts);
                                break;
                            case "[true, true, true, true, false, false]":
                                filteredProducts.clear();
                                filteredProducts.addAll(getStore(products, "Super Brugsen"));
                                filteredProducts.addAll(getStore(products, "Kvickly"));
                                filteredProducts.addAll(getStore(products, "Dagli' Brugsen"));
                                filteredProducts.addAll(getStore(products, "Irma"));
                                updateView(filteredProducts);
                                break;
                            case "[false, true, true, true, true, false]":
                                filteredProducts.clear();
                                filteredProducts.addAll(getStore(products, "Kvickly"));
                                filteredProducts.addAll(getStore(products, "Dagli' Brugsen"));
                                filteredProducts.addAll(getStore(products, "Irma"));
                                filteredProducts.addAll(getStore(products, "Coop 365"));
                                updateView(filteredProducts);
                                break;
                            case "[false, false, true, true, true, true]":
                                filteredProducts.clear();
                                filteredProducts.addAll(getStore(products, "Dagli' Brugsen"));
                                filteredProducts.addAll(getStore(products, "Irma"));
                                filteredProducts.addAll(getStore(products, "Coop 365"));
                                filteredProducts.addAll(getStore(products, "Fakta"));
                                updateView(filteredProducts);
                                break;
                            case "[true, true, true, true, true, false]":
                                filteredProducts.clear();
                                filteredProducts.addAll(getStore(products, "Super Brugsen"));
                                filteredProducts.addAll(getStore(products, "Kvickly"));
                                filteredProducts.addAll(getStore(products, "Dagli' Brugsen"));
                                filteredProducts.addAll(getStore(products, "Irma"));
                                filteredProducts.addAll(getStore(products, "Coop 365"));
                                updateView(filteredProducts);
                                break;
                            case "[false, true, true, true, true, true]":
                                filteredProducts.clear();
                                filteredProducts.addAll(getStore(products, "Kvickly"));
                                filteredProducts.addAll(getStore(products, "Dagli' Brugsen"));
                                filteredProducts.addAll(getStore(products, "Irma"));
                                filteredProducts.addAll(getStore(products, "Coop 365"));
                                filteredProducts.addAll(getStore(products, "Fakta"));
                                updateView(filteredProducts);
                                break;
                            case "[true, true, true, true, true, true]":
                                filteredProducts.clear();
                                filteredProducts.addAll(getStore(products, "Super Brugsen"));
                                filteredProducts.addAll(getStore(products, "Kvickly"));
                                filteredProducts.addAll(getStore(products, "Dagli' Brugsen"));
                                filteredProducts.addAll(getStore(products, "Irma"));
                                filteredProducts.addAll(getStore(products, "Coop 365"));
                                filteredProducts.addAll(getStore(products, "Fakta"));
                                updateView(filteredProducts);
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
                        // use for loop
                        for (int j = 0; j < selectedStores.length; j++) {
                            // remove all selection
                            selectedStores[j] = false;
                            // clear language list
                            langListStores.clear();
                            // clear text view value
                            textViewStores.setText("");
                        }
                        updateView(products);
                    }
                });
                builder.show();
            }
        });

        /////////////////// DISTANCE

        final TextView[] textViewDistance = new TextView[1];
        final boolean[] selectedDistance;
        final int[] choice = new int[1];
        ArrayList<Integer> langListDistance = new ArrayList<>();
        String[] langArrayDistance = {"1 km", "2 km", "5 km", "7 km", "10 km"};


        textViewDistance[0] = view.findViewById(R.id.textViewDistance);
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

                        switch (choice[0]){
                            case 0:
                                Log.e("msg", "1");
                                getKm(mainActivity.locationTrack, products, 1);
                                break;
                            case 1:
                                Log.e("msg", "2");
                                getKm(mainActivity.locationTrack, products, 2);
                                break;
                            case 2:
                                Log.e("msg", "5");
                                getKm(mainActivity.locationTrack, products, 5);
                                break;
                            case 3:
                                Log.e("msg", "7");
                                getKm(mainActivity.locationTrack, products, 7);
                                break;
                            case 4:
                                Log.e("msg", "10");
                                getKm(mainActivity.locationTrack, products, 10);
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
                        // use for loop
                            // clear language list
                            langListDistance.clear();
                            // clear text view value
                            textViewDistance[0].setText("");

                        updateView(products);
                    }
                });
                builder.show();
            }
        });





        return view;
    }

    public void updateView(List<CoopProducts> updatedProducts){
        adapter = new RecyclerViewAdapter(getContext(), updatedProducts, this::onButtonClick);
        recyclerView.setAdapter(adapter);
    }


    public void getCheapest(List<CoopProducts> allProducts){
        List<CoopProducts> result = new ArrayList<>();
        for(int i=0; i<allProducts.size(); i++){
            for(int j=allProducts.size()-1; j>0; j--){
                if(allProducts.get(i).ean == allProducts.get(j).ean){
                    if(allProducts.get(i).pris == allProducts.get(j).pris){
                        result.add(allProducts.get(i));
                        Log.i("cheap:", Double.toString(allProducts.get(i).pris));
                        Log.i("expensive: ", Double.toString(allProducts.get(j).pris));
                    }
                }
            }
        }
        updateView(result);
    }

    public void getOko(List<CoopProducts> allProducts){
        List<CoopProducts> result = new ArrayList<>();
        String filter = "øko";
        for (int i=0; i<allProducts.size(); i++){
            if(allProducts.get(i).navn.toLowerCase().contains(filter.toLowerCase()) || allProducts.get(i).navn2.toLowerCase().contains(filter.toLowerCase())){
                result.add(allProducts.get(i));
            }
        }
        updateView(result);
    }

    public void getKm(LocationTrack locationTrack, List<CoopProducts> allProducts, int km){
        List<CoopProducts> result = new ArrayList<>();
        for (int i=0; i<allProducts.size(); i++){
            if(allProducts.get(i).calculateDistanceDouble(locationTrack.loc) <= km){
                result.add(allProducts.get(i));
            }
        }
        Log.e("km", Integer.toString(km));
        if(result!=null){
        updateView(result);}
        else{
            Log.e("empty", "EMPTY");
        }
    }

    public List<CoopProducts> getStore(List<CoopProducts> allProducts, String storeName){
        List<CoopProducts> result = new ArrayList<>();
        for (int i=0; i<allProducts.size(); i++){
            if(allProducts.get(i).store.toLowerCase().contains(storeName.toLowerCase())){
                result.add(allProducts.get(i));
            }
        }
        Log.e("store", storeName);
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


}