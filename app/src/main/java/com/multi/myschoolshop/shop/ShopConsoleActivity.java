package com.multi.myschoolshop.shop;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.multi.myschoolshop.R;
import com.multi.myschoolshop.utility.GridAdapter;
import com.multi.myschoolshop.utility.GridClass;
import com.multi.myschoolshop.utility.SavedData;

import java.util.ArrayList;

public class ShopConsoleActivity extends AppCompatActivity {
    GridAdapter gridAdapter;
    RecyclerView recyclerView;
    ArrayList<GridClass> list;
    SavedData savedData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.general_recycle);
            savedData=new SavedData(this);
            recyclerView=findViewById(R.id.recycle);
            list=new ArrayList<>();
            gridAdapter=new GridAdapter(list){
                @Override
                public void onBindViewHolder(@NonNull Holder holder, final int i) {
                    super.onBindViewHolder(holder, i);
                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            launch(list.get(i).getActivity());
                        }
                    });
                }
            };
            GridLayoutManager gridLayoutManager=new GridLayoutManager(this,2);
            gridLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            recyclerView.setLayoutManager(gridLayoutManager);
            recyclerView.setAdapter(gridAdapter);
            if(savedData.getValue("deptType").equals("shop")){
                list.add(new GridClass(1,R.drawable.ic_menu_manage,"Edit shop","edit shop detail"));
                list.add(new GridClass(2,R.drawable.ic_add_black_24dp,"Add item","add items to shop"));
                list.add(new GridClass(3,R.drawable.ic_shopping_cart_black_24dp,"New order","recently placed order"));
                list.add(new GridClass(4,R.drawable.ic_done_black_24dp,"Delivered order","Successfully delivered order"));
                list.add(new GridClass(5,R.drawable.ic_remove_shopping_cart_black_24dp,"Rejected order","undelivered order due to some reason"));
            }else{
                list.add(new GridClass(0,R.drawable.ic_add_black_24dp,"Add shop","create a shop account"));
            }
        }

        private void launch(int switchCase) {
            switch (switchCase){
                case 0:
                    startActivity(new Intent(this,CreateShopActivity.class));
                    finish();
                    break;
                case 1:

                    break;
                case 2:
                    startActivity(new Intent(this,AddItemActivity.class));
                    break;
                case 3:
                    startActivity(new Intent(this, NewOrderActivity.class));
                    break;
                case 4:
                    startActivity(new Intent(this, DeliveredOrderActivity.class));
                    break;
                case 5:
                    startActivity(new Intent(this, RejectedOrderActivity.class));
                    break;
            }
        }
    }
