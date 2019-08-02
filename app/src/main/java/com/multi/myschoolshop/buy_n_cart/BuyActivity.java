package com.multi.myschoolshop.buy_n_cart;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.multi.myschoolshop.R;
import com.multi.myschoolshop.shop.Item;
import com.multi.myschoolshop.shop.SpecAdapter;
import com.multi.myschoolshop.shop.Specification;
import com.multi.myschoolshop.utility.Connection;
import com.multi.myschoolshop.utility.SavedData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BuyActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    SpecAdapter shopItemAdapter;
    ArrayList<Specification> itemArrayList;
    Connection connection;
    RadioButton radioButton1,radioButton;
    SavedData savedData;
    float amount =0;
    TextView textView;
    CheckBox checkBox;
    RadioGroup radioGroup;
    String address;
    LinearLayout ll1,ll2;
    private ArrayList<Item> itemArrayList2;
    private int q=1;
    Map<String ,String> map;
    EditText edtNumber;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy);
        map=new HashMap<>();
        checkBox=findViewById(R.id.checkbox);
        radioButton = findViewById(R.id.btnSchool);
        radioButton1 = findViewById(R.id.btnAddress);
        radioGroup=findViewById(R.id.btnRadio);
        recyclerView = findViewById(R.id.recycle);
        ll1=findViewById(R.id.ll1);
        textView = findViewById(R.id.tvAddress);
        edtNumber = findViewById(R.id.edtNumber);
        ll2=findViewById(R.id.ll2);
        itemArrayList = new ArrayList<>();
        itemArrayList2 = new ArrayList<>();
        shopItemAdapter = new SpecAdapter(itemArrayList) ;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(shopItemAdapter);
        savedData = new SavedData(getContext());
        connection = new Connection();
        if(savedData.haveValue("number")){
            edtNumber.setText(savedData.getValue("number"));
        }
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                radioButton1.setError(null);
                radioButton.setError(null);
                checkBox.setError(null);
                address=null;
                switch (checkedId){
                    case R.id.btnSchool:
                        if(savedData.haveValue("schoolName")){
                            address=savedData.getValue("schoolName");
                            ll1.animate().translationY(0).setDuration(500);
                            ll2.animate().translationY(0).setDuration(500);
                        }
                        break;
                    case R.id.btnAddress:
                        if(savedData.haveValue("myAddress")){
                            ll1.animate().translationY(0).setDuration(500);
                            ll2.animate().translationY(0).setDuration(500);
                            address=savedData.getValue("myAddress");
                        }else{
                            savedData.toast("please select a address");
                            startActivity(new Intent(getContext(),AddressActivity.class));
                        }
                        break;
                }
                textView.setText(address);
            }
        });
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                checkBox.setError(null);
            }
        });
    }

    private Context getContext() {
        return this;
    }

    @Override
    public void onStart() {
        super.onStart();
        amount =0;
        itemArrayList2.clear();
        itemArrayList.clear();
        try {
            String key=getIntent().getStringExtra("key");
            String shopId=getIntent().getStringExtra("shopId");
            map.put(key,getIntent().getStringExtra("quantity"));
            fetch(key,shopId);
        } catch (Exception e) {
            connection.getDbUser().child(savedData.getValue("uid")).child("cart").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    itemArrayList.clear();
                    itemArrayList2.clear();
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                        String shopId = dataSnapshot1.child("shopId").getValue(String.class);
                        String key = dataSnapshot1.child("key").getValue(String.class);
                        String quantity=dataSnapshot1.child("quantity").getValue(String.class);
                        map.put(key,quantity);
                        fetch(key,shopId);
                    }
                    connection.getDbUser().child(savedData.getValue("uid")).child("cart")
                            .removeEventListener(this);
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }

    private void fetch(final String key, final String shopId) {
        connection.getDbSchool().child(shopId).child("items").child(key)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Item item = dataSnapshot.getValue(Item.class);
                        item.setKey(dataSnapshot.getKey());
                        String quantity=map.get(item.getKey());
                        if(quantity !=null){
                            item.setQuantity(quantity);
                        }
                        itemArrayList2.add(item);
                        q=1;
                        if(item.getQuantity()!=null){
                            q=Integer.parseInt(item.getQuantity());
                            itemArrayList.add(new Specification(item.getName(),"Rs "+item.getPrice()+"*"+q+" = "+(Float.parseFloat(item.getPrice())*Integer.parseInt(item.getQuantity()))));
                        }else{
                            itemArrayList.add(new Specification(item.getName(),"Rs "+item.getPrice()));

                        }
                        shopItemAdapter.notifyDataSetChanged();
                        amount +=Float.parseFloat(item.getPrice())*q;
                        TextView tvTotal=findViewById(R.id.tvTotal);
                        tvTotal.setText("Total: Rs "+ amount);
                        connection.getDbSchool().child(shopId).child("items").child(key)
                                .removeEventListener(this);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    public void termsAndCondition(View view) {
        startActivity(new Intent(this,TermsAndCondition.class));
    }

    public void place(View view) {
        if(address==null){
            radioButton.setError("please select one of address");
            radioButton1.setError("please select one of address");
            savedData.toast("please select one of address");
            return;
        }
        if(!checkBox.isChecked()){
            checkBox.setError("please accept term and conditions");
            savedData.toast("please accept term and conditions");
            return;
        }
        if(edtNumber.getText().toString().trim().length()<1){
            edtNumber.setError("please enter your mobile number");
            return;
        }else{
            savedData.setValue("number",edtNumber.getText().toString());
            address=address+"\nAlternate number"+edtNumber.getText().toString();
        }
        MakeOrder makeOrder=MakeOrder.getInstance();
        makeOrder.setArrayList(itemArrayList2);
        makeOrder.setAddress(address);
        makeOrder.setAmount(String.valueOf(amount));
        startActivity(new Intent(this,PaymentActivity.class).putExtra("amount", amount));
    }

    @Override
    protected void onResume() {
        super.onResume();
        textView.setText(savedData.getValue("myAddress"));
        if(!savedData.haveValue("myAddress")){
            radioButton.setChecked(true);
        }
    }

    public void address(View view) {
        startActivity(new Intent(this,AddressActivity.class));
    }
}

