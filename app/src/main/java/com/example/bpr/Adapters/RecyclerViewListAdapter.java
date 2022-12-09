package com.example.bpr.Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Paint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
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

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class RecyclerViewListAdapter extends RecyclerView.Adapter<RecyclerViewListAdapter.ViewHolder> {
    private List<CoopProducts> _data;
    private boolean isPlay = false;
    private RecyclerViewListAdapter.OnButtonListener mOnButtonListener;

    private ShoppingCart shoppingCart;
    Context mContext;

    public RecyclerViewListAdapter(Context context, List<CoopProducts> data, RecyclerViewListAdapter.OnButtonListener onButtonListener) {
        this._data = data;
        this.mContext = context;
        this.mOnButtonListener = onButtonListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.shopping_cart_recyclerview, parent, false);

        //Spinner spinner = (Spinner) view.findViewById(R.id.spinnerAmount);
        List<String> values = new ArrayList<String>();
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(view.getContext(), android.R.layout.simple_spinner_item, view.getResources().getStringArray(R.array.values));
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //spinner.setAdapter(dataAdapter);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position){
        holder.name.setText(_data.get(position).navn.substring(0, 1).toUpperCase() + _data.get(position).navn.substring(1).toLowerCase());
        holder.name2.setText(_data.get(position).navn2.substring(0, 1).toUpperCase() + _data.get(position).navn2.substring(1).toLowerCase());
        holder.price.setText(Double.toString(_data.get(position).pris)+" kr");
        holder.store.setText(_data.get(position).store.substring(0, 1).toUpperCase() + _data.get(position).store.substring(1).toLowerCase());

        holder.amount.setText(Double.toString(_data.get(position).amount));
        holder.add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnButtonListener.addItem(holder.getAdapterPosition());
            }
        });
        holder.sub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnButtonListener.subItem(holder.getAdapterPosition());
            }
        });
        holder.deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mOnButtonListener.removeItem(holder.getAdapterPosition());
            }
        });

        holder.check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mOnButtonListener.checkItem(holder.getAdapterPosition());
                if (buttonView.isChecked()){
                    holder.name.setPaintFlags(holder.name.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                }
                else{
                    holder.name.setPaintFlags(holder.name.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
                }
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
        ImageButton deleteBtn;
        TextView store;
        CheckBox check;
        TextView amount;
        Button add, sub;


        ViewHolder(View itemView){
            super(itemView);
            name = itemView.findViewById(R.id.productNameText);
            name2 = itemView.findViewById(R.id.productName2Text);
            price = itemView.findViewById(R.id.price);
            deleteBtn = itemView.findViewById(R.id.deleteButton);
            store = itemView.findViewById(R.id.productStore);
            check = itemView.findViewById(R.id.checkBox);
            amount = itemView.findViewById(R.id.spinnerAmount);
            add = itemView.findViewById(R.id.addBtn);
            sub = itemView.findViewById(R.id.removeBtn);

        }

    }

    public interface OnButtonListener{
        void removeItem(int position);
        void checkItem(int position);
        void addItem(int position);
        void subItem(int position);
    }






}
