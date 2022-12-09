package com.example.bpr.Adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bpr.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ProfileAdapter extends RecyclerView.Adapter<ProfileAdapter.ViewHolder>{
    private ArrayList<String> data;
    Context mContext;
    private OnProfileListener mOnProfileListener;


    public ProfileAdapter(Context context, ArrayList<String> data, OnProfileListener onProfileListener){
        this.data = data;
        this.mContext = context;
        this.mOnProfileListener = onProfileListener;

    }
    @NonNull
    @NotNull
    @Override
    public ProfileAdapter.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.profile_selector_item, parent, false);
        return new ViewHolder(view,this,mOnProfileListener);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ProfileAdapter.ViewHolder holder, int position) {
        holder.name.setText(data.get(position));
        String profile = data.get(position);

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        ProfileAdapter adapter;
        TextView name;
        OnProfileListener listener;

        public ViewHolder(@NonNull @NotNull View itemView, ProfileAdapter adapter, OnProfileListener listener) {
            super(itemView);
            this.adapter = adapter;
            this.listener = listener;
            name = itemView.findViewById(R.id.profile_name);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            listener.onProfileClick(getAdapterPosition());
        }
    }

    public interface OnProfileListener{
        void onProfileClick(int position);
    }

}

