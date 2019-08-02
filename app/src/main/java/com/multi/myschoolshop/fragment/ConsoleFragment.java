package com.multi.myschoolshop.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.multi.myschoolshop.R;
import com.multi.myschoolshop.activity.MainActivity;
import com.multi.myschoolshop.school.SchoolConsoleActivity;
import com.multi.myschoolshop.shop.ShopConsoleActivity;
import com.multi.myschoolshop.utility.GridAdapter;
import com.multi.myschoolshop.utility.GridClass;
import com.multi.myschoolshop.utility.SavedData;

import java.util.ArrayList;

public class ConsoleFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.general_recycle,container,false);
    }
    GridAdapter gridAdapter;
    RecyclerView recyclerView;
    ArrayList<GridClass> list;
    SavedData savedData;
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView=view.findViewById(R.id.recycle);
        list=new ArrayList<>();
        savedData=new SavedData(getContext());
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
        GridLayoutManager gridLayoutManager=new GridLayoutManager(getContext(),2);
        gridLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(gridAdapter);
        list.add(new GridClass(0,R.drawable.ic_home_black_24dp,"My School","manage a school account"));
        list.add(new GridClass(1,R.drawable.ic_menu_gallery,"My Shop","manage a school account"));
    }
    private void launch(int switchCase) {
        switch (switchCase){
            case 0:
                if(savedData.haveValue("position") && Integer.parseInt(savedData.getValue("position"))<6){
                    savedData.toast("create main page");
                    return;
                }else{
                    startActivity(new Intent(getContext(), SchoolConsoleActivity.class));
                }
                break;
            case 1:
                if(savedData.haveValue("position") && Integer.parseInt(savedData.getValue("position"))<6){
                    FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.container, new MainFragment());
                    ft.commit();
                    return;
                }else{
                    startActivity(new Intent(getContext(), ShopConsoleActivity.class));
                }
                break;
        }
    }
}
