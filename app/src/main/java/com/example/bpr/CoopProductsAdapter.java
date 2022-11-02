package com.example.bpr;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CoopProductsAdapter extends RecyclerView.Adapter<CoopProductsAdapter.CoopProductsHolder> {

    class CoopProductsHolder extends RecyclerView.ViewHolder {
        private TextView textViewName;
        private TextView textViewName2;
        private TextView textViewPrice;
        public CoopProductsHolder(View itemView)
        {
            super(itemView);
        }
    }
    @NonNull
    @Override
    public CoopProductsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull CoopProductsHolder holder, int position) {

    }


    @Override
    public int getItemCount() {
        return 0;
    }
}
