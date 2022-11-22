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
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bpr.Adapters.RecyclerViewAdapter;
import com.example.bpr.MVVM.CoopProductsViewModel;
import com.example.bpr.MainActivity;
import com.example.bpr.MyAdapter;
import com.example.bpr.NetworkImpl;
import com.example.bpr.Objects.CoopProducts;
import com.example.bpr.R;
import com.example.bpr.SpinnerStateV0;
import com.example.bpr.VolleyCallBack;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class SearchFragment extends Fragment {
    private RecyclerView recyclerView;
    private RecyclerViewAdapter adapter;
    private NetworkImpl networkImpl = new NetworkImpl();
    private List<CoopProducts> products = new ArrayList<>();
    private CoopProductsViewModel coopProductsViewModel;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        coopProductsViewModel = ViewModelProviders.of(this).get(CoopProductsViewModel.class);
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

        TextView textViewOptions;
        boolean[] selectedOptions;
        ArrayList<Integer> langList = new ArrayList<>();
        String[] langArray = {"Closest", "Cheapest", "Øko"};
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
                            case "[false, false, true]":
                                Log.e("msg", "in oko");
                                getOko(products);
                                break;
                            case "[false, true, false]":
                                Log.e("msg", "in cheapest");
                                getCheapest(products);
                                break;
                            case "[true, false, false]":
                                Log.e("msg", "in closest");
                                getClosest();
                                break;
                            case "[true, true, false]":
                                getCheapest(products);
                                getClosest();
                                Log.e("msg", "in cheapest");
                                Log.e("msg", "in closest");
                                break;
                            case "[true, false, true]":
                                getClosest();
                                getOko(products);
                                Log.e("msg", "in closest");
                                Log.e("msg", "in oko");
                                break;
                            case "[false, true, true]":
                                getCheapest(products);
                                getOko(products);
                                Log.e("msg", "in cheapest");
                                Log.e("msg", "in oko");
                                break;
                            case "[true, true, true]":
                                getCheapest(products);
                                getOko(products);
                                getClosest();
                                Log.e("msg", "in closest");
                                Log.e("msg", "in cheapest");
                                Log.e("msg", "in oko");
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
                    }
                });
                builder.show();
            }
        });

        /////////////////// DISTANCE

        TextView textViewDistance;
        boolean[] selectedDistance;
        ArrayList<Integer> langListDistance = new ArrayList<>();
        String[] langArrayDistance = {"1 km", "2 km", "5 km", "7 km", "10 km"};

        textViewDistance = view.findViewById(R.id.textViewDistance);
        selectedDistance = new boolean[langArrayDistance.length];
        textViewDistance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Distance");
                builder.setCancelable(false);

                builder.setMultiChoiceItems(langArrayDistance, selectedDistance, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i, boolean b) {
                        if(b){
                            langListDistance.add(i);
                            Collections.sort(langListDistance);
                        }
                        else{
                            langListDistance.remove(Integer.valueOf(i));
                        }
                    }
                });

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i){
                        StringBuilder stringBuilder = new StringBuilder();
                        for (int j = 0; j < langListDistance.size(); j++) {
                            // concat array value
                            stringBuilder.append(langArrayDistance[langListDistance.get(j)]);
                            // check condition
                            if (j != langListDistance.size() - 1) {
                                // When j value  not equal
                                // to lang list size - 1
                                // add comma
                                stringBuilder.append(", ");
                            }
                        }
                        textViewDistance.setText(stringBuilder.toString());

                        switch (Arrays.toString(selectedDistance)){
                            case "[true, false, false, false, false]":
                                Log.e("msg", "1");
                                break;
                            case "[false, true, false, false, false]":
                                Log.e("msg", "2");
                                break;
                            case "[false, false, true, false, false]":
                                Log.e("msg", "5");
                                break;
                            case "[false, false, false, true, false]":
                                Log.e("msg", "7");
                                break;
                            case "[false, false, false, false, true]":
                                Log.e("msg", "10");
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
                        for (int j = 0; j < selectedDistance.length; j++) {
                            // remove all selection
                            selectedDistance[j] = false;
                            // clear language list
                            langListDistance.clear();
                            // clear text view value
                            textViewDistance.setText("");
                        }
                        updateView(products);
                    }
                });
                builder.show();
            }
        });





        return view;
    }

    public void updateView(List<CoopProducts> updatedProducts){
        adapter = new RecyclerViewAdapter(updatedProducts);
        recyclerView.setAdapter(adapter);
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

    public List<CoopProducts> getClosest(){
        for(int i=0; i<products.size(); i++){
        Log.i("msg", Integer.toString(products.get(i).vareHierakiId));}
        return products;
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