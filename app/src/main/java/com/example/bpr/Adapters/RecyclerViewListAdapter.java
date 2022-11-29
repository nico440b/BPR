package com.example.bpr.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bpr.NetworkImpl;
import com.example.bpr.Objects.CoopProducts;
import com.example.bpr.R;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewListAdapter extends RecyclerView.Adapter<RecyclerViewListAdapter.ViewHolder> {
    private LiveData<List<CoopProducts>> _data;
    private boolean isPlay = false;

    public RecyclerViewListAdapter(LiveData<List<CoopProducts>> data){
        this._data = data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.shopping_cart_recyclerview, parent, false);

        ImageButton delete;
        delete = view.findViewById(R.id.deleteButton);

        CheckBox checkBox;
        checkBox=view.findViewById(R.id.checkBox);

        Spinner spinner = (Spinner) view.findViewById(R.id.spinnerAmount);
        List<String> values = new ArrayList<String>();
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(view.getContext(), android.R.layout.simple_spinner_item, view.getResources().getStringArray(R.array.values));
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // functionality
                Toast.makeText(view.getContext(), "Deleted", Toast.LENGTH_SHORT).show();
            }
        });

        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // functionality
                Toast.makeText(view.getContext(), "Checkbox clicked", Toast.LENGTH_SHORT).show();
            }
        });


        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position){
        holder.name.setText(_data.getValue().get(position).navn.substring(0, 1).toUpperCase() + _data.getValue().get(position).navn.substring(1).toLowerCase());
        holder.name2.setText(_data.getValue().get(position).navn2.substring(0, 1).toUpperCase() + _data.getValue().get(position).navn2.substring(1).toLowerCase());
        holder.price.setText(Double.toString(_data.getValue().get(position).pris) + " kr");
    }

    @Override
    public int getItemCount() {
        return _data.getValue().size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView name;
        TextView price;
        TextView name2;

        ViewHolder(View itemView){
            super(itemView);
            name = itemView.findViewById(R.id.productNameText);
            name2 = itemView.findViewById(R.id.productName2Text);
            price = itemView.findViewById(R.id.price);
        }
    }

    public CoopProducts getProduct(int id){
        return _data.getValue().get(id);
    }


}
