package com.multi.myschoolshop.shop;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
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

import java.util.ArrayList;

public class DeliveredOrderActivity extends AppCompatActivity {
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
                    holder.btn1.setVisibility(View.GONE);
                    holder.btn2.setVisibility(View.GONE);
                    holder.btn3.setVisibility(View.GONE);

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
            connection.getDbSchool().child(savedData.getValue("shopId")).child("delivered").addValueEventListener(new ValueEventListener() {
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
