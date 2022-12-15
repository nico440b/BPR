package com.example.bpr.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bpr.Objects.CoopProducts;
import com.example.bpr.R;

import java.util.List;

//The implementation of adapters is inspired by our Sep4 Project https://github.com/BorislavAleksiev/ReExam_SEP4_Android/tree/master/app/src/main/java/adapter

public class RecyclerViewFavoriteAdapter extends RecyclerView.Adapter<RecyclerViewFavoriteAdapter.ViewHolder> {
    private List<CoopProducts> _data;
    private OnButtonListener mOnButtonListener;
    private OnAddCartListener mOnAddButtonListener;
    Context mContext;

    public RecyclerViewFavoriteAdapter(Context context, List<CoopProducts> data, OnButtonListener onButtonListener, OnAddCartListener onAddCartListener) {
        this._data = data;
        this.mContext = context;
        this.mOnButtonListener = onButtonListener;
        this.mOnAddButtonListener = onAddCartListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.favorite_list_recyclerview, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position){
        holder.name.setText(_data.get(position).navn.substring(0, 1).toUpperCase() + _data.get(position).navn.substring(1).toLowerCase());
        if (_data.get(position).navn2.length()!=0) {
            holder.name2.setText(_data.get(position).navn2.substring(0, 1).toUpperCase() + _data.get(position).navn2.substring(1).toLowerCase());
        }        holder.price.setText(Double.toString(_data.get(position).pris) + " kr");
        holder.store.setText(_data.get(position).store.substring(0, 1).toUpperCase() + _data.get(position).store.substring(1).toLowerCase());

        holder.deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mOnButtonListener.onButtonClick(holder.getAdapterPosition());
            }
        });

        holder.addToCartBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                mOnAddButtonListener.onAddButtonClick(holder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        if(_data != null){
            return _data.size();
        }
        else {
            return 0;
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView name;
        TextView price;
        TextView name2;
        ImageButton deleteBtn;
        ImageButton addToCartBtn;
        TextView store;

        ViewHolder(View itemView){
            super(itemView);
            name = itemView.findViewById(R.id.productNameText);
            name2 = itemView.findViewById(R.id.productName2Text);
            price = itemView.findViewById(R.id.price);
            addToCartBtn = itemView.findViewById(R.id.addToCartButton);
            deleteBtn = itemView.findViewById(R.id.deleteButton);
            store = itemView.findViewById(R.id.productStore);
        }
    }

    public interface OnButtonListener{
        void onButtonClick(int position);
    }

    public interface OnAddCartListener{
        void onAddButtonClick(int position);
    }

}
