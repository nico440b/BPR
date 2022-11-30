package com.example.bpr.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
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

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    private List<CoopProducts> _data;
    private boolean isPlay = false;
    Context mContext;
    private ShoppingCart shoppingCart;
    private OnButtonListener mOnButtonListener;

    public RecyclerViewAdapter(Context context, List<CoopProducts> data, OnButtonListener onButtonListener){
        this._data = data;
        this.mContext = context;
        this.mOnButtonListener = onButtonListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.recyclerview_row, parent, false);

        return new ViewHolder(view, this);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position){
        CoopProducts current = _data.get(position);
        holder.name.setText(_data.get(position).navn.substring(0, 1).toUpperCase() + _data.get(position).navn.substring(1).toLowerCase());
        holder.name2.setText(_data.get(position).navn2.substring(0, 1).toUpperCase() + _data.get(position).navn2.substring(1).toLowerCase());
        holder.price.setText(Double.toString(_data.get(position).pris) + " kr");
        holder.btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                mOnButtonListener.onButtonClick(holder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return _data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        RecyclerViewAdapter adapter;
        TextView name;
        TextView price;
        TextView name2;
        Button btn;

        ViewHolder(View itemView, RecyclerViewAdapter adapter){
            super(itemView);
            this.adapter = adapter;
            name = itemView.findViewById(R.id.productNameText);
            name2 = itemView.findViewById(R.id.productName2Text);
            price = itemView.findViewById(R.id.price);
            btn = itemView.findViewById(R.id.addToCartButton);
        }
    }

    public interface OnButtonListener{
        void onButtonClick(int position);
    }

}
