package com.example.bpr.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
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

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> implements Filterable {
    private List<CoopProducts> _data;
    private List<CoopProducts> _dataFull;
    private boolean isPlay = false;
    Context mContext;
    private ShoppingCart shoppingCart;
    private OnButtonListener mOnButtonListener;
    private OnFavButtonListener mOnFavButtonListener;


    public RecyclerViewAdapter(Context context, List<CoopProducts> data, OnButtonListener onButtonListener, OnFavButtonListener onFavButtonListener){
        this._data = data;
        this.mContext = context;
        this.mOnButtonListener = onButtonListener;
        this.mOnFavButtonListener = onFavButtonListener;
        _dataFull = new ArrayList<>(_data);
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
        if (_data.get(position).navn2.length()!=0) {
            holder.name2.setText(_data.get(position).navn2.substring(0, 1).toUpperCase() + _data.get(position).navn2.substring(1).toLowerCase());
        }
        holder.price.setText(Double.toString(_data.get(position).pris) + " kr");
        holder.btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                mOnButtonListener.onButtonClick(holder.getAdapterPosition());
            }
        });

        holder.favBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                mOnFavButtonListener.onFavButtonClick(holder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return _data.size();
    }

    @Override
    public Filter getFilter() {
        return searchFilter;
    }

    private Filter searchFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<CoopProducts> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0){
                filteredList.addAll(_dataFull);
            } else{
                String filterPat = constraint.toString().toLowerCase().trim();
                for (CoopProducts item : _dataFull){
                    if (item.navn.toLowerCase().contains(filterPat)){
                        filteredList.add(item);
                    }

                }
            }
            FilterResults result = new FilterResults();
            result.values = filteredList;
            return result;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            _data.clear();
            _data.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };

    public class ViewHolder extends RecyclerView.ViewHolder{
        RecyclerViewAdapter adapter;
        TextView name;
        TextView price;
        TextView name2;
        Button btn;
        Button favBtn;

        ViewHolder(View itemView, RecyclerViewAdapter adapter){
            super(itemView);
            this.adapter = adapter;
            name = itemView.findViewById(R.id.productNameText);
            name2 = itemView.findViewById(R.id.productName2Text);
            price = itemView.findViewById(R.id.price);
            btn = itemView.findViewById(R.id.addToCartButton);
            favBtn = itemView.findViewById(R.id.favoriteButton);
        }
    }

    public interface OnFavButtonListener{
        void onFavButtonClick(int position);
    }

    public interface OnButtonListener{
        void onButtonClick(int position);
    }

}
