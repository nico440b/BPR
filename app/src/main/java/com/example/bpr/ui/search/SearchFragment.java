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
import com.google.android.material.button.MaterialButton;

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
    ShoppingCart shoppingCart = new ShoppingCart();
    FavoriteList favoriteList = new FavoriteList();
    List<CoopProducts> filteredProductsAfterStore = new ArrayList<>();
    List<CoopProducts> filteredProductsAfterOptions = new ArrayList<>();
    List<CoopProducts> filteredProductsAfterDistance = new ArrayList<>();

    TextView textViewStores;
    boolean[] selectedStores;
    ArrayList<Integer> langListStores = new ArrayList<>();
    String[] langArrayStores = {"Super Brugsen", "Kvickly", "Dagli' Brugsen", "Irma", "Coop365", "Fakta"};

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

        MainActivity mainActivity = (MainActivity) getActivity();

        textViewStores = view.findViewById(R.id.textViewStores);
        selectedStores = new boolean[langArrayStores.length];

        textViewOptions = view.findViewById(R.id.textViewOptions);
        selectedOptions = new boolean[langArray.length];

        textViewDistance[0] = view.findViewById(R.id.textViewDistance);

        coopProductsViewModel = ViewModelProviders.of(this).get(CoopProductsViewModel.class);
        shoppingCart.coopProducts = coopProductsViewModel.getProducts();
        favoriteList.coopProducts = coopProductsViewModel.getProducts();
        coopProductsViewModel.getProducts().observe(getViewLifecycleOwner(), new Observer<List<CoopProducts>>() {
            @Override
            public void onChanged(List<CoopProducts> coopProducts) {
                Toast.makeText(view.getContext(), "Changed", Toast.LENGTH_SHORT).show();
                products = coopProducts;
                Log.e("Rest Respone", products.get(0).navn);
                updateView(products);
            }
        });



        recyclerView = view.findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));

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
                                filteredProductsAfterStore.addAll(getStore(products, "Super Brugsen"));
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
                                filteredProductsAfterStore.addAll(getStore(products, "Fakta"));
                                updateView(filteredProductsAfterStore);
                                Log.e("size:", Integer.toString(filteredProductsAfterStore.size()));
                                break;
                            case "[true, true, false, false, false, false]":
                                filteredProductsAfterStore.clear();
                                filteredProductsAfterStore.addAll(getStore(products, "Super Brugsen"));
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
                                filteredProductsAfterStore.addAll(getStore(products, "Fakta"));
                                updateView(filteredProductsAfterStore);
                                Log.e("size:", Integer.toString(filteredProductsAfterStore.size()));
                                break;
                            case "[true, true, true, false, false, false]":
                                filteredProductsAfterStore.clear();
                                filteredProductsAfterStore.addAll(getStore(products, "Super Brugsen"));
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
                                filteredProductsAfterStore.addAll(getStore(products, "Fakta"));
                                updateView(filteredProductsAfterStore);
                                break;
                            case "[true, true, true, true, false, false]":
                                filteredProductsAfterStore.clear();
                                filteredProductsAfterStore.addAll(getStore(products, "Super Brugsen"));
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
                                filteredProductsAfterStore.addAll(getStore(products, "Fakta"));
                                updateView(filteredProductsAfterStore);
                                break;
                            case "[true, true, true, true, true, false]":
                                filteredProductsAfterStore.clear();
                                filteredProductsAfterStore.addAll(getStore(products, "Super Brugsen"));
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
                                filteredProductsAfterStore.addAll(getStore(products, "Fakta"));
                                updateView(filteredProductsAfterStore);
                                break;
                            case "[true, true, true, true, true, true]":
                                filteredProductsAfterStore.clear();
                                filteredProductsAfterStore.addAll(getStore(products, "Super Brugsen"));
                                filteredProductsAfterStore.addAll(getStore(products, "Kvickly"));
                                filteredProductsAfterStore.addAll(getStore(products, "Dagli' Brugsen"));
                                filteredProductsAfterStore.addAll(getStore(products, "Irma"));
                                filteredProductsAfterStore.addAll(getStore(products, "Coop 365"));
                                filteredProductsAfterStore.addAll(getStore(products, "Fakta"));
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
                        // use for loop
                        for (int j = 0; j < selectedStores.length; j++) {
                            // remove all selection
                            selectedStores[j] = false;
                            // clear language list
                            langListStores.clear();
                            // clear text view value
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

                        if(filteredProductsAfterOptions.size()==0){
                            if(filteredProductsAfterStore.size()==0){
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
                                filteredProductsAfterDistance.addAll(getKm(mainActivity.locationTrack, filteredProductsAfterOptions, 1));
                                updateView(filteredProductsAfterDistance);
                                break;
                            case 1:
                                filteredProductsAfterDistance.clear();
                                textViewDistance[0].setText("2 km");
                                filteredProductsAfterDistance.addAll(getKm(mainActivity.locationTrack, filteredProductsAfterOptions, 2));
                                updateView(filteredProductsAfterDistance);
                                break;
                            case 2:
                                filteredProductsAfterDistance.clear();
                                textViewDistance[0].setText("5 km");
                                filteredProductsAfterDistance.addAll(getKm(mainActivity.locationTrack, filteredProductsAfterOptions, 5));
                                updateView(filteredProductsAfterDistance);
                                break;
                            case 3:
                                filteredProductsAfterDistance.clear();
                                textViewDistance[0].setText("7 km");
                                filteredProductsAfterDistance.addAll(getKm(mainActivity.locationTrack, filteredProductsAfterOptions, 7));
                                updateView(filteredProductsAfterDistance);
                                break;
                            case 4:
                                filteredProductsAfterDistance.clear();
                                textViewDistance[0].setText("10 km");
                                filteredProductsAfterDistance.addAll(getKm(mainActivity.locationTrack, filteredProductsAfterOptions, 1000));
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
                if(allProducts.get(i).navn ==allProducts.get(j).navn){
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

    public List<CoopProducts> getKm(LocationTrack locationTrack, List<CoopProducts> allProducts, int km){
        List<CoopProducts> result = new ArrayList<>();
        for (int i=0; i<allProducts.size(); i++){
            if(allProducts.get(i).calculateDistanceDouble(locationTrack.loc) <= km){
                result.add(allProducts.get(i));
            }
        }

        return result;
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
        shoppingCart.coopProducts.getValue().add(products.get(position));
        Log.e("added product to cart:", products.get(position).navn);
    }

    @Override
    public void onFavButtonClick(int position){
        favoriteList.coopProducts.getValue().add(products.get(position));
        Log.e("added product to fav:", products.get(position).navn);

    }


}