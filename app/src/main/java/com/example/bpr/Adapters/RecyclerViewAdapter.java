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
    private OnProductListener mOnProductListener;

    public RecyclerViewAdapter(Context context, List<CoopProducts> data, OnProductListener onProductListener){
        this._data = data;
        this.mOnProductListener = onProductListener;
        this.mContext = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.recyclerview_row, parent, false);

        MaterialButton addToCart;
        addToCart = view.findViewById(R.id.addToCartButton);

        MaterialButton favorite;
        favorite = view.findViewById(R.id.favoriteButton);
        favorite.setBackgroundResource(R.drawable.ic_baseline_favorite_border_24);

        addToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(view.getContext(), "Product added to shopping cart", Toast.LENGTH_SHORT).show();
            }
        });

        favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isPlay){
                    view.setBackgroundResource(R.drawable.ic_baseline_favorite_border_24);
                }
                else {
                    view.setBackgroundResource(R.drawable.ic_baseline_favorite_24);
                }
                isPlay = !isPlay;
                Toast.makeText(view.getContext(), "Added to fav", Toast.LENGTH_SHORT).show();
            }
        });

        return new ViewHolder(view, this, mOnProductListener);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position){
        CoopProducts current = _data.get(position);
        holder.name.setText(_data.get(position).navn.substring(0, 1).toUpperCase() + _data.get(position).navn.substring(1).toLowerCase());
        holder.name2.setText(_data.get(position).navn2.substring(0, 1).toUpperCase() + _data.get(position).navn2.substring(1).toLowerCase());
        holder.price.setText(Double.toString(_data.get(position).pris) + " kr");
    }

    @Override
    public int getItemCount() {
        return _data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        RecyclerViewAdapter adapter;
        TextView name;
        TextView price;
        TextView name2;
        Button btn;
        OnProductListener listener;

        ViewHolder(View itemView, RecyclerViewAdapter adapter, OnProductListener listener){
            super(itemView);
            this.adapter = adapter;
            this.listener = listener;
            name = itemView.findViewById(R.id.productNameText);
            name2 = itemView.findViewById(R.id.productName2Text);
            price = itemView.findViewById(R.id.price);
            btn = itemView.findViewById(R.id.addToCartButton);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v){
            listener.onProductClick(getAdapterPosition());
        }
    }

    public interface OnProductListener{
        void onProductClick(int position);
    }

}
