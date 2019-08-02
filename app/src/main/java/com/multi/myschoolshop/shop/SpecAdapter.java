package com.multi.myschoolshop.shop;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.multi.myschoolshop.R;

import java.util.ArrayList;

public class SpecAdapter extends RecyclerView.Adapter<SpecAdapter.Holder>{
    ArrayList<Specification> arrayList;
    Context context;
    public SpecAdapter(ArrayList<Specification> arrayList) {
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        context=viewGroup.getContext();
        return new Holder(LayoutInflater.from(context).inflate(R.layout.item_row_spec,viewGroup,false));
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int i) {
        Specification specification=arrayList.get(i);
        holder.tvValue.setText(specification.getValue());
        holder.tvField.setText(specification.getField());
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class Holder extends RecyclerView.ViewHolder{
        TextView tvField,tvValue;
        public Holder(@NonNull View itemView) {
            super(itemView);
            tvValue=itemView.findViewById(R.id.tvValue);
            tvField=itemView.findViewById(R.id.tvField);

        }
    }
}
