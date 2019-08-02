package com.multi.myschoolshop.shop;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.multi.myschoolshop.R;
import com.multi.myschoolshop.buy_n_cart.Order;
import com.multi.myschoolshop.utility.Connection;
import com.multi.myschoolshop.utility.SavedData;
import com.multi.myschoolshop.utility.StaticFinalValues;

import java.util.ArrayList;

import static com.multi.myschoolshop.utility.StaticFinalValues.STATUS_ACCEPTED;
import static com.multi.myschoolshop.utility.StaticFinalValues.STATUS_REQUEST_CANCEL;

public class NewOrderActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    ArrayList<Order> orderArrayList;
    Connection connection;
    SavedData savedData;
    OrderAdapter orderAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_order);

            recyclerView=findViewById(R.id.recycle);
            orderArrayList =new ArrayList<>();
            orderAdapter=new OrderAdapter(orderArrayList){
                @Override
                public void onBindViewHolder(@NonNull final Holder holder, int i) {
                    super.onBindViewHolder(holder, i);
                    final Order order=orderArrayList.get(i);
                    if(order.getStatus().equals(STATUS_ACCEPTED)){
                        holder.btn1.setVisibility(View.GONE);
                    }else if(order.getStatus().equals(STATUS_REQUEST_CANCEL)){
                        holder.btn1.setText("cancel order");
                    }

                    holder.btn1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            holder.btn1.setText("Done");
                            holder.btn1.setBackgroundColor(Color.GREEN);
                            holder.btn2.setClickable(false);
                            holder.btn3.setClickable(false);
                            if(order.getStatus().equals(STATUS_REQUEST_CANCEL)){
                                connection.getDbUser().child(order.getUid()).child("order")
                                        .child(order.getDateKey()).child(order.getKey()).removeValue();
                                connection.getDbSchool().child(savedData.getValue("shopId"))
                                        .child("order").child(order.getDateKey()).child(order.getKey()).removeValue();
                                holder.btn1.setText("order cancelled");
                            }else{
                                order.setStatus(STATUS_ACCEPTED);
                                connection.getDbUser().child(order.getUid()).child("order")
                                        .child(order.getDateKey()).child(order.getKey()).setValue(order);
                                connection.getDbSchool().child(savedData.getValue("shopId"))
                                        .child("order").child(order.getDateKey()).child(order.getKey()).setValue(order);
                            }
                        }
                    });
                    holder.btn2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            holder.btn2.setText("Done");
                            holder.btn2.setBackgroundColor(Color.GREEN);
                            holder.btn1.setClickable(false);
                            holder.btn3.setClickable(false);
                            order.setStatus(StaticFinalValues.STATUS_DELIVERED);
                            connection.getDbUser().child(order.getUid()).child("order")
                                    .child(order.getDateKey()).child(order.getKey()).setValue(order);
                            connection.getDbSchool().child(savedData.getValue("shopId"))
                                    .child("delivered").child(order.getDateKey()).child(order.getKey()).setValue(order);
                            connection.getDbSchool().child(savedData.getValue("shopId"))
                                    .child("order").child(order.getDateKey()).removeValue();
                        }
                    });
                    holder.btn3.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            holder.btn3.setText("Done");
                            holder.btn3.setBackgroundColor(Color.GREEN);
                            holder.btn1.setClickable(false);
                            holder.btn2.setClickable(false);
                            order.setStatus(StaticFinalValues.STATUS_REJECTED);
                            connection.getDbUser().child(order.getUid()).child("order")
                                    .child(order.getDateKey()).child(order.getKey()).setValue(order);
                            connection.getDbSchool().child(savedData.getValue("shopId"))
                                    .child("rejected").child(order.getDateKey()).child(order.getKey()).setValue(order);
                            connection.getDbSchool().child(savedData.getValue("shopId"))
                                    .child("order").child(order.getDateKey()).removeValue();
                        }
                    });

                }
            };
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setAdapter(orderAdapter);
            savedData=new SavedData(this);
            connection=new Connection();
          }



        @Override
        public void onStart() {
            super.onStart();
            connection.getDbSchool().child(savedData.getValue("shopId")).child("order").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    orderArrayList.clear();
                    for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                        for(DataSnapshot dataSnapshot2:dataSnapshot1.getChildren()){
                            Order order=dataSnapshot2.getValue(Order.class);
                            orderArrayList.add(order);
                        }
                       }
                    orderAdapter.notifyDataSetChanged();
                    connection.getDbSchool().child(savedData.getValue("shopId")).child("order")
                            .removeEventListener(this);
                    }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
    }
}
