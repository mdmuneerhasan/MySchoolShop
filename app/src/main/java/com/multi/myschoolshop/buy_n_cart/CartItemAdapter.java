package com.multi.myschoolshop.buy_n_cart;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.multi.myschoolshop.R;
import com.multi.myschoolshop.shop.Item;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CartItemAdapter extends RecyclerView.Adapter<CartItemAdapter.Holder> {
    ArrayList<Item> list;
    Context context;
    public CartItemAdapter(ArrayList<Item> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        context=viewGroup.getContext();
        return new Holder(LayoutInflater.from(context).inflate(R.layout.item_row_cart,viewGroup,false));
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int i) {
        Item gridClass=list.get(i);
        holder.tvTitle.setText(gridClass.getName());
        holder.tvDescription.setText("Quantity : "+gridClass.getQuantity()+"\n"+gridClass.getDescription());
        holder.tvPrice.setText("Rs "+gridClass.getPrice());
        Picasso.get().load(gridClass.getImagesList().get(0)).into(holder.viewPager);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class Holder extends RecyclerView.ViewHolder{
        ImageView viewPager;
        TextView tvTitle,tvDescription,tvPrice;
        public Holder(@NonNull View itemView) {
            super(itemView);
            tvTitle=itemView.findViewById(R.id.tvTitle);
            tvDescription=itemView.findViewById(R.id.tvDescription);
            tvPrice=itemView.findViewById(R.id.tvPrice);
            viewPager=itemView.findViewById(R.id.pager);
        }
    }
}
