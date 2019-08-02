package com.multi.myschoolshop.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.multi.myschoolshop.R;
import com.multi.myschoolshop.buy_n_cart.BuyActivity;
import com.multi.myschoolshop.buy_n_cart.Order;
import com.multi.myschoolshop.buy_n_cart.PaymentActivity;
import com.multi.myschoolshop.shop.Item;
import com.multi.myschoolshop.shop.OrderAdapter;
import com.multi.myschoolshop.shop.SpecAdapter;
import com.multi.myschoolshop.shop.Specification;
import com.multi.myschoolshop.utility.Animator;
import com.multi.myschoolshop.utility.Connection;
import com.multi.myschoolshop.utility.SavedData;
import com.multi.myschoolshop.utility.StaticFinalValues;

import java.util.ArrayList;

public class MyOrderFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_cart,container,false);
    }
    RecyclerView recyclerView;
    OrderAdapter adapter;
    ArrayList<Order> orderArrayList;
    Connection connection;
    SavedData savedData;
    Animator animator;
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView=view.findViewById(R.id.recycle);
        orderArrayList =new ArrayList<>();
        animator=new Animator(view);
        adapter =new OrderAdapter(orderArrayList){
            @Override
            public void onBindViewHolder(@NonNull final Holder holder, final int i) {
                super.onBindViewHolder(holder, i);
                final Order order=orderArrayList.get(i);
                if(order.getStatus().equals(StaticFinalValues.STATUS_PREPARING)){
                    holder.btn1.setText("cancel order");
                    holder.btn1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            alertCancellation(order,holder);
                        }
                    });
                }

                else if(order.getStatus().equals(StaticFinalValues.STATUS_ACCEPTED)){
                    holder.btn1.setText("request cancellation");
                    holder.btn1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            requestCancellation(order,holder);
                        }
                    });
                }

                else{
                    holder.btn1.setVisibility(View.GONE);
                }
                holder.btn2.setVisibility(View.GONE);
                holder.btn3.setVisibility(View.GONE);
            }
        };
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
        savedData=new SavedData(getContext());
        connection=new Connection();
        TextView textView=view.findViewById(R.id.text);
        textView.setText("My Orders");
        view.findViewById(R.id.btnCheckout).setVisibility(View.GONE);
    }

    private void alertCancellation(final Order order, final OrderAdapter.Holder holder) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage("Are you sure you want to cancel this order?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        connection.getDbSchool().child(order.getArrayList().get(0).getShopId()).child("order")
                                .child(order.getDateKey()).child(order.getKey()).removeValue();
                        connection.getDbUser().child(savedData.getValue("uid")).child("order")
                                .child(order.getDateKey()).child(order.getKey()).removeValue();
                        holder.btn1.setText("Order cancelled");
                        holder.btn1.setBackgroundColor(Color.GREEN);

                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void requestCancellation(final Order order, final OrderAdapter.Holder holder) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage("Are you sure you want to request cancellation of this order?\n" +
                "we will try our best to cancel this order as it is shipped for delivery")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        order.setStatus(StaticFinalValues.STATUS_REQUEST_CANCEL);
                        connection.getDbSchool().child(order.getArrayList().get(0).getShopId()).child("order")
                                .child(order.getDateKey()).child(order.getKey()).setValue(order);
                        connection.getDbUser().child(savedData.getValue("uid")).child("order")
                                .child(order.getDateKey()).child(order.getKey()).setValue(order);
                        holder.btn1.setText("cancel request send");
                        holder.btn1.setBackgroundColor(Color.GREEN);
                        onStart();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }


    @Override
    public void onStart() {
        super.onStart();
        animator.startAnimation();
        connection.getDbUser().child(savedData.getValue("uid")).child("order")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        orderArrayList.clear();
                        for(DataSnapshot dataSnapshot2:dataSnapshot.getChildren()){
                            for(DataSnapshot dataSnapshot1:dataSnapshot2.getChildren()){
                                Order order=dataSnapshot1.getValue(Order.class);
                                orderArrayList.add(0,order);
                            }
                        }
                        animator.stopAnimation(orderArrayList);
                        adapter.notifyDataSetChanged();
                        connection.getDbUser().child(savedData.getValue("uid")).child("order")
                                .removeEventListener(this);
                        }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });
    }
}
