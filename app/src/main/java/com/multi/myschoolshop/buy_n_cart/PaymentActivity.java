package com.multi.myschoolshop.buy_n_cart;

import android.annotation.SuppressLint;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.multi.myschoolshop.R;
import com.multi.myschoolshop.fragment.MyOrderFragment;
import com.multi.myschoolshop.shop.Item;
import com.multi.myschoolshop.utility.Connection;
import com.multi.myschoolshop.utility.SavedData;
import com.multi.myschoolshop.utility.StaticFinalValues;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class PaymentActivity extends AppCompatActivity {
    Float amount;
    TextView tvAmount;
    String[] options;
    String payMentMode;
    RadioGroup rdGroup;
    ArrayList<Item> arrayList;
    SavedData savedData;
    Connection connection;
    String date;
    private String address;
    private MakeOrder makeOrder;
    FrameLayout container;
    LinearLayout ll1;
    LinearLayout update;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        tvAmount =findViewById(R.id.tvAmount);
        options=new String[]{"cod"};
        rdGroup=findViewById(R.id.rdGroup);
        update=findViewById(R.id.llUpdate);
        amount=getIntent().getFloatExtra("amount",0);
        tvAmount.setText("Pay: Rs"+String.valueOf(amount));
        savedData=new SavedData(this);
        connection=new Connection();
        date =new SimpleDateFormat("yyyy-MM-dd").format(new Date(System.currentTimeMillis()));
        makeOrder=MakeOrder.getInstance();
        address=makeOrder.getAddress();
        arrayList=makeOrder.getArrayList();
        container=findViewById(R.id.container);
        ll1=findViewById(R.id.ll1);

        rdGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.btnCod:
                        payMentMode=options[0];
                        break;
                }
            }
        });
    }

    @SuppressLint("SimpleDateFormat")
    public void placeOrder(View view) {
        if(payMentMode==null){
            RadioButton radioButton=findViewById(R.id.btnCod);
            radioButton.setError("select payment mode");
            savedData.toast("select payment mode");
            return;
        }else if(arrayList.size()<1){
            savedData.toast("you have no cart item or buying product");
            return;
        }
        savedData.showAlert("Placing your order");
        Order order=new Order(arrayList);
        order.setAddress(address);
        order.setName(savedData.getValue("name"));
        order.setAmount(makeOrder.getAmount());
        order.setUid(savedData.getValue("uid"));
        order.setDateKey(date);
        order.setStatus(StaticFinalValues.STATUS_PREPARING);
        order.setSchoolId(savedData.getValue("schoolId"));
        order.setTime(new SimpleDateFormat("yyyy-MMM-dd\nhh:mm a")
                .format(new Date(System.currentTimeMillis())));
        String key= String.valueOf(System.currentTimeMillis())+savedData.getValue("uid");
        order.setKey(key);
        connection.getDbSchool().child(arrayList.get(0).getShopId()).child("order")
                .child(date).child(key).setValue(order);
        connection.getDbUser().child(savedData.getValue("uid")).child("order")
                .child(date).child(key).setValue(order).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                ll1.setVisibility(View.GONE);
                savedData.toast("Order Placed");
                savedData.removeAlert();
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.container, new MyOrderFragment());
                ft.commit();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        connection.getVersionControl().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String version = "1.0";
                try{               version=dataSnapshot.child("version").getValue(String.class);
                }catch (Exception e){}
                try {
                    PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
                    String myVersion = pInfo.versionName;
                    connection.getVersionControl().child("present").setValue(myVersion);
                    Log.e("version",myVersion);
                    Float.parseFloat(version);
                    if(Float.parseFloat(myVersion)>=Float.parseFloat(version)){
                        // work fine
                        savedData.log("fine");
                        ll1.setVisibility(View.VISIBLE);
                        update.setVisibility(View.GONE);
                    }else{
                        ll1.setVisibility(View.GONE);
                        update.setVisibility(View.VISIBLE);
                        savedData.log("update");
                    }
                }catch(PackageManager.NameNotFoundException e) { }
                connection.getVersionControl().removeEventListener(this);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
