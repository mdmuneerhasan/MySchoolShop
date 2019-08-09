package com.multi.myschoolshop.fragment;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ImageView;

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
import com.multi.myschoolshop.utility.Animator;
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

import static android.app.Activity.RESULT_OK;

public class MyProfileFragment extends Fragment implements View.OnClickListener {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile,container,false);
    }
    private static final int PICK_REQUEST = 1234;
    private Uri parentUri;
    private StorageTask uploadTask;
    RecyclerView recyclerView,recyclerView2;
    ImageView imageView,btnPic;
    ArrayList<TextClass> arrayList;
    ArrayList<SetDefaultClass> defaultList;
    TextAdapter textAdapter;
    SetDefaultAdapter setDefaultAdapter;
    SavedData savedData;
    Connection connection;
    Storage storage;
    View view;Bundle saved;
    Animator animator;
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.view=view;saved=savedInstanceState;
        imageView=view.findViewById(R.id.imageView);
        animator=new Animator(view);
        recyclerView=view.findViewById(R.id.recycle);
        recyclerView2=view.findViewById(R.id.recycle2);
        btnPic=view.findViewById(R.id.btnPic);
        arrayList=new ArrayList<>();
        defaultList =new ArrayList<>();
        setDefaultAdapter =new SetDefaultAdapter(defaultList){
            @Override
            public void onBindViewHolder(@NonNull final Holder holder, final int i) {
                super.onBindViewHolder(holder, i);
                final SetDefaultClass setDefaultClass=defaultList.get(i);
                holder.checkBox.setVisibility(View.VISIBLE);
                holder.checkBox.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onStart();
                        if(holder.checkBox.isChecked()){
                            savedData.setValue("schoolId",setDefaultClass.getSchoolId());
                            savedData.setValue("deptType",setDefaultClass.getDeptType());
                            savedData.setValue("stClass",setDefaultClass.getStClass());
                            savedData.setValue("position",setDefaultClass.getPosition());
                            setShop(setDefaultClass.getSchoolId());
                        }
                    }
                });
                if(setDefaultClass.getSchoolId()!=null && setDefaultClass.getSchoolId().equals(savedData.getValue("schoolId"))){
                    holder.checkBox.setChecked(true);
                    holder.itemView.setBackgroundColor(Color.GREEN);
                }else{
                    holder.itemView.setBackgroundColor(Color.WHITE);
                    holder.checkBox.setChecked(false);
                }
            }
        };
        recyclerView2.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView2.setAdapter(setDefaultAdapter);
        textAdapter=new TextAdapter(arrayList){
            @Override
            public void onBindViewHolder(@NonNull Holder holder, final int i) {
                super.onBindViewHolder(holder, i);
                holder.btnEdit.setVisibility(View.VISIBLE);
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        launch(arrayList.get(i).getSwitchCase());
                    }
                });
                holder.btnEdit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        editName();
                    }
                });
            }
        };
        recyclerView.setAdapter(textAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        storage=new Storage();
        connection=new Connection();
        savedData=new SavedData(getContext());
        arrayList.add(new TextClass(0,R.drawable.ic_account_circle_black_24dp,"Name",savedData.getValue("name"),
                "This is not your username or pin. This name will be visible to your friends"));
        btnPic.setOnClickListener(this);
        Picasso.get().load(savedData.getValue("photoUrl")).into(imageView);
        btnPic.setOnClickListener(this);
    }

    private void setShop(String schoolId) {
        connection.getDbSchool().child(schoolId).child("shopId")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String shopId=dataSnapshot.getValue(String.class);
                        savedData.setValue("shopId",shopId);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    private void launch(int switchCase) {
        switch (switchCase){
            case 0:
                editName();
                break;

        }
    }

    private void editName() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Edit your name");
        final EditText input = new EditText(getContext());
        input.setText(savedData.getValue("name"));
        builder.setView(input);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String name = input.getText().toString();
                savedData.setValue("name",name);
                connection.getDbUser().child(savedData.getValue("uid")).child("name").setValue(name).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        onStart();
                        onViewCreated(view,saved);
                    }
                });
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnPic:
                picK();
                break;
        }
    }

    private void picK() {

        if(savedData.hasPermission(Manifest.permission.READ_EXTERNAL_STORAGE, getActivity(), new Callback() {
            @Override
            public void onSuccess() {
                Intent intent=new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent,PICK_REQUEST);
            }

            @Override
            public void onError(Exception e) {

            }
        })){
            Intent intent=new Intent(Intent.ACTION_PICK,
                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent,PICK_REQUEST);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==PICK_REQUEST && resultCode==RESULT_OK&&data!=null && data.getData()!=null){
            parentUri =data.getData();
            imageView.setPadding(0,0,0,0);
            Picasso.get().load(data.getData()).into(imageView);
            update();
        }
    }

    private void update() {
        if(uploadTask!=null && uploadTask.isInProgress()){
            savedData.toast("uploading your file!!");
            return;
        }
        if (parentUri !=null){
            savedData.toast("Uploading your pic");
            uploadTask=storage.getSchoolStorage().child("profilePics")
                    .child(savedData.getValue("uid")+System.currentTimeMillis()+"."+getExtension(parentUri)).putFile(parentUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            final Task<Uri> firebaseUri = taskSnapshot.getStorage().getDownloadUrl();
                            firebaseUri.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String url = uri.toString();
                                    savedData.toast("Upload Successful");
                                    connection.getDbUser().child(savedData.getValue("uid")).child("photoUrl").setValue(url);
                                    savedData.setValue("photoUrl",url);
                                    parentUri=null;
                                    savedData.setValue("change","yes");
                                }
                            });

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            savedData.toast("Upload Failed");
                        }
                    }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                        }
                    });
        }else{

        }
    }


    private String getExtension(Uri uri) {
        return MimeTypeMap.getSingleton().getExtensionFromMimeType(getContext().getContentResolver().getType(uri));
    }

    @Override
    public void onStart() {
        super.onStart();
        animator.startAnimation();
        connection.getDbUser().child(savedData.getValue("uid")).child("permission").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                defaultList.clear();
                for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                    for(DataSnapshot dataSnapshot2:dataSnapshot1.getChildren()){
                        defaultList.add(dataSnapshot2.getValue(SetDefaultClass.class));
                    }
                }
                animator.stopAnimation(defaultList);
                setDefaultAdapter.notifyDataSetChanged();
                connection.getDbUser().child(savedData.getValue("uid")).child("permission").removeEventListener(this);
                }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
