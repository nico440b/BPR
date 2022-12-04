package com.example.bpr.Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
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
import com.example.bpr.Objects.ShoppingCart;
import com.example.bpr.R;
import com.google.android.material.button.MaterialButton;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class RecyclerViewListAdapter extends RecyclerView.Adapter<RecyclerViewListAdapter.ViewHolder> {
    private List<CoopProducts> _data;
    private boolean isPlay = false;
    private RecyclerViewAdapter.OnButtonListener mOnButtonListener;
    private ShoppingCart shoppingCart;
    Context mContext;

    public RecyclerViewListAdapter(Context context, List<CoopProducts> data, RecyclerViewAdapter.OnButtonListener onButtonListener) {
        this._data = data;
        this.mContext = context;
        this.mOnButtonListener = onButtonListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.shopping_cart_recyclerview, parent, false);

        Spinner spinner = (Spinner) view.findViewById(R.id.spinnerAmount);
        List<String> values = new ArrayList<String>();
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(view.getContext(), android.R.layout.simple_spinner_item, view.getResources().getStringArray(R.array.values));
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position){
        holder.name.setText(_data.get(position).navn.substring(0, 1).toUpperCase() + _data.get(position).navn.substring(1).toLowerCase());
        holder.name2.setText(_data.get(position).navn2.substring(0, 1).toUpperCase() + _data.get(position).navn2.substring(1).toLowerCase());
        holder.price.setText(Double.toString(_data.get(position).pris) + " kr");
        holder.store.setText(_data.get(position).store.substring(0, 1).toUpperCase() + _data.get(position).store.substring(1).toLowerCase());
        holder.addedBy.setText("added by");
        holder.deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mOnButtonListener.onButtonClick(holder.getAdapterPosition());
            }
        });

    }


    @Override
    public int getItemCount() {
        if(_data != null){
            return _data.size();
        }
        else
            return 0;

    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView name;
        TextView price;
        TextView name2;
        Button deleteBtn;
        TextView store;
        TextView addedBy;

        ViewHolder(View itemView){
            super(itemView);
            name = itemView.findViewById(R.id.productNameText);
            name2 = itemView.findViewById(R.id.productName2Text);
            price = itemView.findViewById(R.id.price);
            deleteBtn = itemView.findViewById(R.id.deleteButton);
            store = itemView.findViewById(R.id.productStore);
            addedBy = itemView.findViewById(R.id.productAddedBy);
        }

    }

    public interface OnButtonListener{
        void onButtonClick(int position);
    }


}
