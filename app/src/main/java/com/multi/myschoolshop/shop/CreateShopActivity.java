package com.multi.myschoolshop.shop;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
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
import com.multi.myschoolshop.utility.Connection;
import com.multi.myschoolshop.utility.SavedData;
import com.multi.myschoolshop.utility.SetDefaultClass;
import com.multi.myschoolshop.utility.Storage;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.sql.Date;
import java.text.SimpleDateFormat;

public class CreateShopActivity extends AppCompatActivity {
    private static final int REQUEST_SCHOOL = 123;
    private Uri parentUri;
    ImageView imgSchool;
    private String name;
    EditText edtSchoolName;
    SavedData savedData;
    private StorageTask uploadTask;
    Storage storage;
    Connection connection;
    private String id;
    Date date;
    SimpleDateFormat simpleDateFormat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_shop);
        imgSchool=findViewById(R.id.imgSchool);
        edtSchoolName=findViewById(R.id.edtSchoolName);
        savedData=new SavedData(this);
        connection=new Connection();
        storage=new Storage();
        date=new Date(System.currentTimeMillis());
        simpleDateFormat=new SimpleDateFormat("yy");



    }

    public void pick(View view) {
        if (savedData.hasPermission(Manifest.permission.READ_EXTERNAL_STORAGE,this,new Callback(){
            @Override
            public void onSuccess() {
                Intent intent=new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, REQUEST_SCHOOL);
            }

            @Override
            public void onError(Exception e) {
            }
        })) {
            Intent intent=new Intent(Intent.ACTION_PICK,
                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, REQUEST_SCHOOL);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK&&data!=null && data.getData()!=null){
            parentUri =data.getData();
            switch (requestCode){
                case REQUEST_SCHOOL:
                    imgSchool.setPadding(0,0,0,0);
                    Picasso.get().load(data.getData()).into(imgSchool);
                    break;
            }
        }
    }


    public void create(View view) {
        name =edtSchoolName.getText().toString();
        if(name.trim().length()<1){
            edtSchoolName.setError("Please enter shop Name");
            return;
        }
        createAccount(REQUEST_SCHOOL);
    }

    private void createAccount(int requestSchool) {
        savedData.showAlert("creating account...");
        if(uploadTask!=null && uploadTask.isInProgress()){
            savedData.toast("uploading your file!!");
            return;
        }
        if (parentUri !=null){
            uploadTask=storage.getSchoolStorage().child("store")
                    .child(savedData.getValue("uid")+System.currentTimeMillis()+"."+getExtension(parentUri)).putFile(parentUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Task<Uri> downloadUrl = taskSnapshot.getStorage().getDownloadUrl();
                            downloadUrl.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String url = uri.toString();
                                    savedData.toast("Upload Successful");
                                    createAccount2(url);

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
                            //       savedData.showAlert(String.valueOf(100*taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount()));
                        }
                    });
        }else{
            createAccount2(null);
        }
    }

    private void createAccount2(final String url) {

        connection.getDbSchool().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot!=null){
                    id="c"+simpleDateFormat.format(date)+String.valueOf(dataSnapshot.getChildrenCount());
                }else{
                    id="c"+simpleDateFormat.format(date)+"0";
                }
                        connection.getDbSchool().child(id).child("deptType").setValue("shop");
                SetDefaultClass setDefaultClass=new SetDefaultClass(name,"7",null,id);
                setDefaultClass.setPermission(true);
                setDefaultClass.setDeptType("shop");
                setDefaultClass.setRes(R.drawable.ic_menu_gallery);

                connection.getDbUser().child(savedData.getValue("uid")).child("permission")
                        .child(id).child("7").setValue(setDefaultClass);
                connection.getDbSchool().child(id).child("schoolPhoto").setValue(url);
                connection.getDbSchool().child(id).child("shopId").setValue(id);
                connection.getDbSchool().child(id).child("name").setValue(name);
                connection.getDbSchool().child(id).child("id").setValue(id);
                connection.getDbSchool().child(id).child("principal").setValue(savedData.getValue("uid"))
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                savedData.setValue("schoolId",id);
                                savedData.setValue("shopId",id);
                                savedData.setValue("deptType","shop");
                                savedData.setValue("stClass","class 11");
                                savedData.setValue("position","7");
                                savedData.removeAlert();
                                parentUri=null;
                                savedData.removeAlert();
                                finish();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                            }
                        });
                connection.getDbSchool().removeEventListener(this);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    private String getExtension(Uri uri) {
        return MimeTypeMap.getSingleton().getExtensionFromMimeType(getContentResolver().getType(uri));
    }

}
