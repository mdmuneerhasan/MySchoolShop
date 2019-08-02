package com.multi.myschoolshop.fragment;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.multi.myschoolshop.R;
import com.multi.myschoolshop.buy_n_cart.BuyActivity;
import com.multi.myschoolshop.shop.Item;
import com.multi.myschoolshop.shop.SpecAdapter;
import com.multi.myschoolshop.shop.Specification;
import com.multi.myschoolshop.utility.Animator;
import com.multi.myschoolshop.utility.Connection;
import com.multi.myschoolshop.utility.SavedData;

import java.util.ArrayList;

public class MainFragment extends Fragment {

    private String quantity;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main,container,false);
    }
    private SpecAdapter specAdapter;
    RecyclerView recyclerView;
    ShopItemAdapter shopItemAdapter;
    ArrayList<Item> itemArrayList,itemArrayList2;
    Connection connection;
    SavedData savedData;
    TextView textView1,textView2,textView3,textView4;
    Animator animator;
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView=view.findViewById(R.id.recycle);
        textView1=view.findViewById(R.id.text1);
        textView2=view.findViewById(R.id.text2);
        textView3=view.findViewById(R.id.text3);
        textView4=view.findViewById(R.id.text4);
        itemArrayList=new ArrayList<>();
        itemArrayList2=new ArrayList<>();
        shopItemAdapter=new ShopItemAdapter(itemArrayList,itemArrayList2){
            @Override
            public void onBindViewHolder(@NonNull final Holder holder, final int i) {
                super.onBindViewHolder(holder, i);
                holder.imageView.setBackgroundColor(Color.WHITE);
                holder.imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popUp(itemArrayList.get(i));
                    }
                });
            }
        };
        GridLayoutManager gridLayoutManager=new GridLayoutManager(getContext(),2);
        gridLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(shopItemAdapter);
        savedData=new SavedData(getContext());
        connection=new Connection();
        animator=new Animator(view);
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
        final EditText edtQuantity=dialogView.findViewById(R.id.edtQuantity);
        if(item.getArrayList()!=null){
            specAdapter=new SpecAdapter(item.getArrayList());
        }else{
            specAdapter=new SpecAdapter(new ArrayList<Specification>());
        }
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(specAdapter);
        tvName.setText(item.getName());
        tvDescription.setText(item.getDescription());
        tvPrice.setText("Rs "+item.getPrice());
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

                startActivity(new Intent(getContext(), BuyActivity.class)
                        .putExtra("key",item.getKey()).putExtra("shopId",item.getShopId())
                .putExtra("quantity",quantity));
                alertDialog.cancel();
            }
        });
        dialogView.findViewById(R.id.btnCart).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Item item1=new Item(item.getKey(),item.getShopId());
                quantity=edtQuantity.getText().toString();
                if(quantity.trim().equals("")){
                    quantity="1";
                }else if(Integer.parseInt(quantity)>10){
                    edtQuantity.setError("Max quantity exceeded");
                    return;
                }
                item1.setQuantity(quantity);
                connection.getDbUser().child(savedData.getValue("uid")).child("cart")
                        .child(item.getKey()).setValue(item1).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        savedData.toast("added to cart");
                    }
                });
                alertDialog.cancel();
            }
        });
    }

    private void setSlider(ArrayList<String> imagesList, ViewPager mPager) {
        if(imagesList!=null){
            mPager.setAdapter(new ImageSlider(imagesList,mPager));
        }else{
            mPager.setVisibility(View.GONE);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        animator.startAnimation();
        connection.getDbSchool().child(savedData.getValue("schoolId")).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        final String shopId=dataSnapshot.child("shopId").getValue(String.class);
                        if(shopId==null){
                            addDummy();
                            return;
                        }
                        connection.getDbSchool().child(shopId).child("items")
                                .addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        itemArrayList.clear();
                                        itemArrayList2.clear();
                                        for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                                            Item item=dataSnapshot1.getValue(Item.class);
                                            item.setKey(dataSnapshot1.getKey());
                                            itemArrayList.add(item);
                                            itemArrayList2.add(item);
                                        }
                                        animator.stopAnimation(itemArrayList);
                                        if(itemArrayList.size()==0){
                                            addDummy();
                                        }
                                        shopItemAdapter.notifyDataSetChanged();
                                        connection.getDbSchool().child(shopId).child("items")
                                                .removeEventListener(this);
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });
                        connection.getDbSchool().child(savedData.getValue("schoolId"))
                                .removeEventListener(this);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });
    }

    private void addDummy() {
        for (int i = 0; i < 10; i++) {
            ArrayList<String> image=new ArrayList<>();
            image.add("https://firebasestorage.googleapis.com/v0/b/schooldiary-a2138.appspot.com/o/debug%2Fschool%2Fstore%2FwmMcuC7Af1RjiQrqMSqoAnM465O21564639952069.jpg?alt=media&token=2f86a99f-5564-43d3-8967-048bd571c2b6");
            image.add("https://firebasestorage.googleapis.com/v0/b/schooldiary-a2138.appspot.com/o/debug%2Fschool%2Fstore%2FwmMcuC7Af1RjiQrqMSqoAnM465O21564639957065.jpg?alt=media&token=92c5e0db-5888-4eab-9a71-a71c5b01a830");
            itemArrayList.add(new Item("school copies and register","this item is just for test","0",image,new ArrayList<Specification>()));
        }
        animator.stopAnimation(itemArrayList);
        itemArrayList2=new ArrayList<>(itemArrayList);
        shopItemAdapter.notifyDataSetChanged();
    }


    public void filter(String query) {
        recyclerView.scrollToPosition(0);
        shopItemAdapter.getFilter().filter(query);
    }
}
