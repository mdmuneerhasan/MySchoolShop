package com.multi.myschoolshop.activity;

import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.multi.myschoolshop.R;
import com.multi.myschoolshop.buy_n_cart.BuyActivity;
import com.multi.myschoolshop.fragment.ImageSlider;
import com.multi.myschoolshop.fragment.SetUpFragment;
import com.multi.myschoolshop.fragment.ShopItemAdapter;
import com.multi.myschoolshop.shop.Item;
import com.multi.myschoolshop.shop.SpecAdapter;
import com.multi.myschoolshop.shop.Specification;
import com.multi.myschoolshop.utility.Animator;
import com.multi.myschoolshop.utility.Connection;
import com.multi.myschoolshop.utility.SavedData;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {
    private SpecAdapter specAdapter;
    RecyclerView recyclerView;
    ShopItemAdapter shopItemAdapter;
    ArrayList<Item> itemArrayList,itemArrayList2;
    Connection connection;
    SavedData savedData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        recyclerView=findViewById(R.id.recycle);
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
            }

    private Context getContext() {
        return  this;
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
        final EditText edtQuantity=dialogView.findViewById(R.id.tvPrice);
        TextView tvDescription=dialogView.findViewById(R.id.tvDescription);
                RecyclerView recyclerView=dialogView.findViewById(R.id.recycle);
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
                        String quantity = edtQuantity.getText().toString();
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
                mPager.setAdapter(new ImageSlider(imagesList,mPager));
            }


            @Override
            public void onStart() {
                super.onStart();
                    fetch(connection.getDbSchool().child(savedData.getValue("schoolId")));
            }

    private void fetch(final DatabaseReference schoolId) {
        schoolId.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                final String shopId=dataSnapshot.child("shopId").getValue(String.class);
                if(shopId==null){
                    FragmentTransaction ft =getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.container, new SetUpFragment());
                    ft.commit();
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
                                shopItemAdapter.notifyDataSetChanged();
                                query();
                                connection.getDbSchool().child(shopId).child("items")
                                        .removeEventListener(this);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                schoolId.removeEventListener(this);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    private void query() {
        try {
            String query=getIntent().getStringExtra("query");
            shopItemAdapter.getFilter().filter(query);
        }catch (Exception e){
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        // Associate searchable configuration with the SearchView
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.search)
                .getActionView();
        searchView.setSearchableInfo(searchManager
                .getSearchableInfo(getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);

        // listening to search query text change
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                startActivity(new Intent(getBaseContext(),SearchActivity.class)
                        .putExtra("query",query));
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                shopItemAdapter.getFilter().filter(query);
                return false;
            }
        });
        return true;
    }


}
