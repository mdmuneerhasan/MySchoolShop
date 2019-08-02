package com.multi.myschoolshop.school;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.multi.myschoolshop.R;
import com.multi.myschoolshop.shop.CreateShopActivity;
import com.multi.myschoolshop.utility.GridAdapter;
import com.multi.myschoolshop.utility.GridClass;
import com.multi.myschoolshop.utility.SavedData;

import java.util.ArrayList;

public class SchoolConsoleActivity extends AppCompatActivity {
    GridAdapter gridAdapter;
    RecyclerView recyclerView;
    ArrayList<GridClass> list;
    SavedData savedData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.general_recycle);
        recyclerView = findViewById(R.id.recycle);
        list = new ArrayList<>();
        savedData=new SavedData(this);
        gridAdapter = new GridAdapter(list) {
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
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        gridLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(gridAdapter);

        if(savedData.getValue("deptType").equals("school")){
            list.add(new GridClass(1,R.drawable.ic_monetization_on_black_24dp,"Revenue","Earning so far"));
        }else{
            list.add(new GridClass(0, R.drawable.ic_add_black_24dp, "Add School", "create a school account"));
        }



    }

    private void launch(int switchCase) {
        switch (switchCase) {
            case 0:
                startActivity(new Intent(this, CreateSchoolActivity.class));
                break;
            case 1:
                startActivity(new Intent(this, SchoolEarningActivity.class));
                break;
        }
    }
}
