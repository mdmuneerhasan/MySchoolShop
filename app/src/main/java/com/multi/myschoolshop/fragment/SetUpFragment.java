package com.multi.myschoolshop.fragment;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.CardView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.multi.myschoolshop.R;
import com.multi.myschoolshop.utility.Connection;
import com.multi.myschoolshop.utility.SavedData;
import com.multi.myschoolshop.utility.Storage;
import com.multi.myschoolshop.utility.User;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SetUpFragment extends Fragment implements View.OnClickListener {
    LinearLayout  btnFollow , linearLayout1;
    CardView cv1, cv2;
    ScrollView scrollView;
    TextView tvHint,tvName,tvExtra;
    AutoCompleteTextView edtValue;
    Button btnConnect;
    ImageView avatar;
    Storage storage;

    SavedData savedData;
    Connection connection;
    int choice;
    ArrayList<String> list;
    Map<String,SetUpFragmentHelperClass> map;
    ArrayAdapter<String> adapter;
    int lastPopped=10;
    private SetUpFragmentHelperClass helperClass3;
    private boolean block =false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_set_up, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        connection=new Connection();
        savedData=new SavedData(getContext());
        storage=new Storage();
        list=new ArrayList<>();
        map=new HashMap<>();
        adapter = new ArrayAdapter<>(getContext(),android.R.layout.simple_dropdown_item_1line,list);
        tvName=view.findViewById(R.id.tvName);
        tvExtra=view.findViewById(R.id.tvExtra);
        avatar=view.findViewById(R.id.imgAvatar);
        btnConnect=view.findViewById(R.id.btnConnect);
        scrollView=view.findViewById(R.id.scrollView);
        edtValue=view.findViewById(R.id.edtValue);
        tvHint=view.findViewById(R.id.tvHint);
        btnFollow=view.findViewById(R.id.tvFollow);
        linearLayout1=view.findViewById(R.id.lowerLayout1);
        cv1=view.findViewById(R.id.cv1);
        cv2=view.findViewById(R.id.cv2);
        btnFollow.setOnClickListener(this);
        btnConnect.setOnClickListener(this);
        if(savedData.haveValue("schoolId")){
//            FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
//            ft.replace(R.id.container, new MainFragment());
//            ft.commit();
            savedData.toast("you are already following a institute");
        }
        edtValue.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                helperClass3=map.get(edtValue.getText().toString());
                avatar.setPadding(0,0,0,0);
                Picasso.get().load(helperClass3.getUrl()).into(avatar);
                tvName.setText(helperClass3.getName());
                btnConnect.setText(helperClass3.getButton());
                popUp(3);
            }
        });
    }

    private void set(int i) {
        choice=i;
        btnFollow.setBackgroundColor(Color.parseColor("#ffffff"));
        scrollView.animate().translationY(0).setDuration(500);
        switch (i%4){
            case 3:
                tvHint.setText("Enter school / college name or id");
                btnFollow.setBackgroundColor(Color.parseColor("#cccccc"));
                break;
        }
    }

    @Override
    public void onClick(View v) {
        avatar.setPadding(10,10,10,10);
        popDown();
        switch (v.getId()){
            case R.id.tvFollow:
                set(3);
                act3();
                if(lastPopped==3){
                    avatar.setPadding(0,0,0,0);
                    popUp(3);
                }
                break;
            case R.id.btnConnect:
                connect(lastPopped);
                break;
        }
    }

    private void connect(int lastPopped) {
        popUp(lastPopped);
        switch (lastPopped){
            case 3 :
                btnConnect.setText("following");
                savedData.toast("now you are following "+helperClass3.getName());
                connection.getDbSchool().child(helperClass3.getId()).child("followers")
                        .child(savedData.getValue("uid")).child("name").setValue(savedData.getValue("name"));
                connection.getDbUser().child(savedData.getValue("uid")).child("following")
                        .child(helperClass3.getId()).setValue(helperClass3);
                connection.getDbUser().child(savedData.getValue("uid")).child("schoolId")
                        .setValue(helperClass3.getId());
                helperClass3.setButton("following");
                savedData.setValue("schoolId",helperClass3.getId());
                savedData.setValue("schoolName",helperClass3.getName());
                savedData.setValue("deptType","school");
                savedData.setValue("position","2");
                setMainFragment();
                break;
        }
    }

    private void setMainFragment() {
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.container, new MainFragment());
        ft.commit();
    }

    private void act3() {
        connection.getDbSchool().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list.clear();
                for (DataSnapshot datasnap:dataSnapshot.getChildren() ) {
                    if(datasnap!=null){
                        list.add(datasnap.child("name").getValue(String.class)+" "+datasnap.child("id").getValue(String.class));
                        SetUpFragmentHelperClass setUpFragmentHelperClass=datasnap.getValue(SetUpFragmentHelperClass.class);
                        setUpFragmentHelperClass.setUrl(datasnap.child("schoolPhoto").getValue(String.class));
                        if(datasnap.child("followers").child(savedData.getValue("uid")).child("name").getValue(String.class)!=null){
                            setUpFragmentHelperClass.setButton("following");
                        }else{
                            setUpFragmentHelperClass.setButton("follow");
                        }
                        map.put(datasnap.child("name").getValue(String.class)+" "+datasnap.child("id").getValue(String.class),setUpFragmentHelperClass);
                    }
                }
                adapter.notifyDataSetChanged();
                edtValue.setAdapter(adapter);
                connection.getDbSchool().removeEventListener(this);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private void popDown() {
        if(block){
            block=false;
            return;
        }
        cv2.animate().translationX(1024).setDuration(500);
        linearLayout1. animate().translationY(-300).setDuration(500);
    }private void popUp(int i) {
        lastPopped=i;
        cv2.animate().translationY(0).translationX(0).setDuration(500);
        linearLayout1.animate().translationY(0).setDuration(500);
    }
}
