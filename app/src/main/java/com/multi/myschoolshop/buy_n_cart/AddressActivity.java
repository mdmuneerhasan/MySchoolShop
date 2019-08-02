package com.multi.myschoolshop.buy_n_cart;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.multi.myschoolshop.R;
import com.multi.myschoolshop.shop.SpecAdapter;
import com.multi.myschoolshop.shop.Specification;
import com.multi.myschoolshop.utility.Connection;
import com.multi.myschoolshop.utility.SavedData;
import com.multi.myschoolshop.utility.SetDefaultAdapter;
import com.multi.myschoolshop.utility.SetDefaultClass;
import com.multi.myschoolshop.utility.Storage;
import com.multi.myschoolshop.utility.TextAdapter;
import com.multi.myschoolshop.utility.TextClass;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AddressActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ArrayList<TextClass> arrayList;
    TextAdapter textAdapter;
    SavedData savedData;
    Connection connection;
    Storage storage;
    Bundle saved;
    int i=1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);
            saved=savedInstanceState;
            recyclerView=findViewById(R.id.recycle);
            arrayList=new ArrayList<>();
            textAdapter=new TextAdapter(arrayList){
                @Override
                public void onBindViewHolder(@NonNull final Holder holder, int i) {
                    super.onBindViewHolder(holder, i);
                    holder.checkBox.setVisibility(View.VISIBLE);
                    if(savedData.getValue("myAddress").equals(arrayList.get(holder.getAdapterPosition()).getTitle())){
                        holder.checkBox.setChecked(true);
                        holder.tvHeading.setText("Address "+String.valueOf(i++));
                        holder.itemView.setBackgroundColor(Color.GREEN);
                    }else{
                        holder.checkBox.setChecked(false);
                        holder.itemView.setBackgroundColor(Color.WHITE);
                    }
                    holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            savedData.setValue("myAddress",arrayList.get(holder.getAdapterPosition()).getTitle());
                            onStart();
                        }
                    });
                }
            };
            recyclerView.setAdapter(textAdapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            storage=new Storage();
            connection=new Connection();
            savedData=new SavedData(this);
    }

    public void popUp(View view) {
        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.alert_address, null);
        dialogBuilder.setView(dialogView);
        final AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();
        final EditText edtName=dialogView.findViewById(R.id.edtName);
        final EditText edtNumber=dialogView.findViewById(R.id.edtNumber);
        final EditText edtPin=dialogView.findViewById(R.id.edtPin);
        final EditText edtFlat=dialogView.findViewById(R.id.edtFlat);
        final EditText edtColony=dialogView.findViewById(R.id.edtColony);
        final EditText edtLandmark=dialogView.findViewById(R.id.edtLandMark);
        final EditText edtCity=dialogView.findViewById(R.id.edtCity);
        edtName.setText(savedData.getValue("name"));
        dialogView.findViewById(R.id.btnAdd).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StringBuilder address=new StringBuilder();
                if (edtName.getText().toString().trim().length()<1){
                    edtName.setError("name can't empty");
                    return;
                } if (edtNumber.getText().toString().trim().length()<1) {
                    edtNumber.setError("number can't empty");
                    return;
                }
                address.append(edtName.getText().toString()+"-");
                address.append(edtNumber.getText().toString()+"\n");
                address.append(edtFlat.getText().toString()+" ");
                address.append(edtLandmark.getText().toString()+" ");
                address.append(edtColony.getText().toString()+" ");
                address.append(edtCity.getText().toString()+"\n");
                address.append(edtPin.getText().toString());
                connection.getDbUser().child(savedData.getValue("uid")).child("address")
                        .push().child("address").setValue(address.toString());
                alertDialog.cancel();
                onStart();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        connection.getDbUser().child(savedData.getValue("uid")).child("address")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        arrayList.clear();
                        for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                            TextClass textClass=new TextClass();
                            textClass.setTitle(dataSnapshot1.child("address").getValue(String.class));
                            textClass.setRes(R.drawable.ic_home_black_24dp);
                            arrayList.add(0,textClass);
                        }
                        connection.getDbUser().child(savedData.getValue("uid")).child("address")
                                .removeEventListener(this);
                        textAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }
}
