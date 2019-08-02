package com.multi.myschoolshop.shop;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.multi.myschoolshop.R;
import com.multi.myschoolshop.buy_n_cart.Order;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.Holder>{
    ArrayList<Order> orderArrayList;
    Context context;
    public OrderAdapter(ArrayList<Order> arrayList) {
        this.orderArrayList = arrayList;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        context=viewGroup.getContext();
        return new Holder(LayoutInflater.from(context).inflate(R.layout.item_row_order,viewGroup,false));
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int i) {
        Order order= orderArrayList.get(i);
        holder.textView1.setText(order.getAddress()+"\nOrdered by "+order.getName());
        holder.textView2.setText("Rs "+order.getAmount());
        holder.textView3.setText(order.getTime()+"\nStatus: "+order.getStatus());
        holder.linearLayout.removeAllViews();
        if(order.getArrayList().size()>0)
        for(Item item:order.getArrayList()){
            LayoutInflater inflater = LayoutInflater.from(context);
            View dialogView = inflater.inflate(R.layout.item_row_single, null);
            ImageView imageView=dialogView.findViewById(R.id.imageView);
            TextView textView1=dialogView.findViewById(R.id.text2);
            TextView textView2=dialogView.findViewById(R.id.text1);
            textView2.setText(item.getName());
            textView1.setText(item.getDescription()+"\nRs:"+item.getPrice()+"*"+item.getQuantity());
            Picasso.get().load(item.getImagesList().get(0)).into(imageView);
            holder.linearLayout.addView(dialogView);
        }
    }

    @Override
    public int getItemCount() {
        return orderArrayList.size();
    }
    public class Holder extends RecyclerView.ViewHolder{
        LinearLayout linearLayout;
        TextView textView1,textView2,textView3;
        public Button btn1, btn2,btn3;
        public Holder(@NonNull View itemView) {
            super(itemView);
            btn1 =itemView.findViewById(R.id.btn1);
            btn2 =itemView.findViewById(R.id.btn2);
            btn3 =itemView.findViewById(R.id.btn3);
            textView1=itemView.findViewById(R.id.text1);
            textView2=itemView.findViewById(R.id.text2);
            textView3=itemView.findViewById(R.id.text3);
            linearLayout=itemView.findViewById(R.id.recycle);
        }
    }
}
