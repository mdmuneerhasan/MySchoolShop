package com.multi.myschoolshop.fragment;

import android.app.AlertDialog;
import android.content.Intent;
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
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.multi.myschoolshop.R;
import com.multi.myschoolshop.buy_n_cart.BuyActivity;
import com.multi.myschoolshop.buy_n_cart.CartItemAdapter;
import com.multi.myschoolshop.shop.Item;
import com.multi.myschoolshop.shop.SpecAdapter;
import com.multi.myschoolshop.shop.Specification;
import com.multi.myschoolshop.utility.Animator;
import com.multi.myschoolshop.utility.Connection;
import com.multi.myschoolshop.utility.SavedData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CartFragment extends Fragment {

    private EditText edtQuantity;
    private String quantity="1";
    Map<String ,String>map;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_cart,container,false);
    }
    private SpecAdapter specAdapter;
    RecyclerView recyclerView;
    CartItemAdapter shopItemAdapter;
    ArrayList<Item> itemArrayList;
    Connection connection;
    SavedData savedData;
    Animator animator;
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        animator=new Animator(view);
        recyclerView=view.findViewById(R.id.recycle);
        itemArrayList=new ArrayList<>();
        map=new HashMap<>();
        shopItemAdapter=new CartItemAdapter(itemArrayList){
            @Override
            public void onBindViewHolder(@NonNull final Holder holder, final int i) {
                super.onBindViewHolder(holder, i);
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try{                        popUp(itemArrayList.get(holder.getAdapterPosition()));}catch (Exception e){}
                    }
                });
            }
        };
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(shopItemAdapter);
        savedData=new SavedData(getContext());
        connection=new Connection();
        view.findViewById(R.id.btnCheckout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(),BuyActivity.class));
            }
        });
    }

    private void popUp(final Item item) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.alert_main, null);
        dialogBuilder.setView(dialogView);
        final AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();
        TextView tvName=dialogView.findViewById(R.id.tvName);
        TextView tvPrice=dialogView.findViewById(R.id.tvPrice);
        TextView tvDescription=dialogView.findViewById(R.id.tvDescription);
        RecyclerView recyclerView=dialogView.findViewById(R.id.recycle);
        edtQuantity =dialogView.findViewById(R.id.edtQuantity);
        if(item.getArrayList()!=null){
            specAdapter=new SpecAdapter(item.getArrayList());
        }else{
            specAdapter=new SpecAdapter(new ArrayList<Specification>());
        }
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(specAdapter);
        tvName.setText(item.getName());
        tvDescription.setText(item.getDescription());
        tvPrice.setText(item.getPrice());
        setSlider(item.getImagesList(), (ViewPager) dialogView.findViewById(R.id.pager));
        dialogView.findViewById(R.id.btnBuy).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quantity=edtQuantity.getText().toString();
                if(quantity.trim().equals("")){
                    quantity="1";
                }else if(Integer.parseInt(quantity)>10){
                    edtQuantity.setError("Max quantity exceeded");
                    return;
                }
                item.setQuantity(quantity);
                startActivity(new Intent(getContext(), BuyActivity.class)
                .putExtra("key",item.getKey()).putExtra("shopId",item.getShopId()));
                alertDialog.cancel();
            }
        });
        Button btnCart=dialogView.findViewById(R.id.btnCart);
        btnCart.setText("remove from cart");
        btnCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                connection.getDbUser().child(savedData.getValue("uid")).child("cart")
                        .child(item.getKey()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        savedData.toast("item removed");
                        onStart();
                        itemArrayList.remove(item);
                        shopItemAdapter.notifyDataSetChanged();
                    }
                });
                alertDialog.cancel();
            }
        });
    }

    private void setSlider(ArrayList<String> imagesList, ViewPager mPager) {
        mPager.setAdapter(new ImageSlider(imagesList,mPager));
    }


    @Override
    public void onStart() {
        super.onStart();
        animator.startAnimation();
        connection.getDbUser().child(savedData.getValue("uid")).child("cart").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        itemArrayList.clear();
                        for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                            String shopId=dataSnapshot1.child("shopId").getValue(String.class);
                            String key=dataSnapshot1.child("key").getValue(String.class);
                            String quantity=dataSnapshot1.child("quantity").getValue(String.class);
                            map.put(key,quantity);
                            connection.getDbSchool().child(shopId).child("items").child(key)
                                    .addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            Item item=dataSnapshot.getValue(Item.class);
                                            item.setKey(dataSnapshot.getKey());
                                            item.setQuantity(map.get(item.getKey()));
                                            itemArrayList.add(item);
                                            shopItemAdapter.notifyDataSetChanged();
                                            animator.stopAnimation(itemArrayList);
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });
                            connection.getDbSchool().child(shopId).child("items").child(key)
                                    .removeEventListener(this);
                        }
                        animator.stopAnimation(itemArrayList);
                        connection.getDbUser().child(savedData.getValue("uid")).child("cart")
                                .removeEventListener(this);
                        }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }
}
