package com.multi.myschoolshop.shop;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.UploadTask;
import com.multi.myschoolshop.R;
import com.multi.myschoolshop.utility.Connection;
import com.multi.myschoolshop.utility.SavedData;
import com.multi.myschoolshop.utility.Storage;
import com.squareup.picasso.Callback;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class AddItemActivity extends AppCompatActivity {
    private static final int REQUEST_SCHOOL = 123;
    private static final int REQUEST_IMAGE_CAPTURE = 122;
    private static final String add_category = "Add a new category";
    RecyclerView recyclerView2,recyclerView;
    ImageAdapter imageAdapter;
    ArrayList<String> imageList;
    SpecAdapter specAdapter;
    SavedData savedData;
    Connection connection;
    Storage storage;
    private Uri parentUri;
    ProgressBar progressBar,progressBar2;
    ArrayList<Specification> specificationArrayList;
    EditText edtName,edtPrice,edtDesc;
    private int selected=0;
    private int upload=0;
    ArrayList<String> categoryList;
    private ArrayAdapter categoryAdapter;
    private Spinner spinner;
    String category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);
        savedData=new SavedData(this);
        connection=new Connection();
        storage=new Storage();
        categoryList =new ArrayList<>();
        imageList=new ArrayList<>();
        recyclerView2 =findViewById(R.id.recycle2);
        spinner =findViewById(R.id.spinner);
        imageAdapter=new ImageAdapter(imageList);
        specificationArrayList=new ArrayList<>();
        recyclerView =findViewById(R.id.recycle);
        specAdapter=new SpecAdapter(specificationArrayList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(specAdapter);
        GridLayoutManager gridLayoutManager=new GridLayoutManager(this,1);
        gridLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView2.setLayoutManager(gridLayoutManager);
        recyclerView2.setAdapter(imageAdapter);
        progressBar=findViewById(R.id.progress);
        progressBar2=findViewById(R.id.progress2);
        edtName=findViewById(R.id.edtName);
        edtPrice=findViewById(R.id.edtPrice);
        edtDesc=findViewById(R.id.edtDescription);
        edtDesc.clearFocus();
        edtName.clearFocus();
        edtPrice.clearFocus();
        categoryAdapter = new ArrayAdapter<>(this,android.R.layout.simple_dropdown_item_1line, categoryList);
        spinner.setAdapter(categoryAdapter);
        categoryList.add("Select category");
        categoryAdapter.notifyDataSetChanged();
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                category=categoryList.get(position);
                if(categoryList.get(position).equals(add_category)){
                    addCategory();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void pick2(View view) {
        if (savedData.hasPermission(Manifest.permission.CAMERA, this, new Callback() {
            @Override
            public void onSuccess() {
                Intent m_intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                parentUri = getImageUri();
                m_intent.putExtra(MediaStore.EXTRA_OUTPUT,parentUri);
                startActivityForResult(m_intent, REQUEST_IMAGE_CAPTURE);
            }

            @Override
            public void onError(Exception e) {
            }
        })) {
            Intent m_intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            parentUri = getImageUri();
            m_intent.putExtra(MediaStore.EXTRA_OUTPUT, parentUri);
            startActivityForResult(m_intent, REQUEST_IMAGE_CAPTURE);
        }

    }

    private Uri getImageUri(){
        Uri m_imgUri = null;
        File m_file;
        try {
            String m_imagePath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + System.currentTimeMillis() + ".jpg";
            m_file = new File(m_imagePath);
            m_imgUri = Uri.fromFile(m_file);
        } catch (Exception p_e) {
        }
        return m_imgUri;
    }
    public void pick(View view) {
        if (savedData.hasPermission(Manifest.permission.READ_EXTERNAL_STORAGE, this, new Callback() {
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
        if(resultCode==RESULT_OK){
            switch (requestCode){
                case REQUEST_SCHOOL:
                    if(data!=null){
                        parentUri =data.getData();
                        progressBar.setVisibility(View.VISIBLE);
                        saveIt();
                    }
                    break;
                case REQUEST_IMAGE_CAPTURE:
                    progressBar2.setVisibility(View.VISIBLE);
                    saveIt();
                    break;
            }
        }
    }

    private void saveIt() {
        selected++;
        @SuppressLint("StaticFieldLeak")
        Converter converter=new Converter(this){
            @Override
            protected void onPostExecute(byte[] bytes) {
                super.onPostExecute(bytes);
                imageAdapter.notifyDataSetChanged();
                if (parentUri !=null){
                    storage.getSchoolStorage().child("store").child(savedData.getValue("shopId"))
                            .child(savedData.getValue("uid")+System.currentTimeMillis()+"."+getExtension(parentUri)).putBytes(bytes)
                            .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    Task<Uri> downloadUrl = taskSnapshot.getStorage().getDownloadUrl();
                                    downloadUrl.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            String url = uri.toString();
                                            savedData.toast("Upload Successful");
                                            progressBar.setVisibility(View.GONE);
                                            progressBar2.setVisibility(View.GONE);
                                            upload++;
                                            imageList.add(url);
                                        }
                                    });

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            savedData.toast("Upload Failed");
                            progressBar.setVisibility(View.GONE);
                            progressBar2.setVisibility(View.GONE);
                        }
                    }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            //       savedData.showAlert(String.valueOf(100*taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount()));
                        }
                    });
                }



            }
        };
        converter.execute(parentUri);
    }

    private String getExtension(Uri uri) {
        return MimeTypeMap.getSingleton().getExtensionFromMimeType(getContentResolver().getType(uri));
    }

    public void addSpec(View view) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.alert_label_editor, null);
        dialogBuilder.setView(dialogView);
        final AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();
        final EditText edtField=dialogView.findViewById(R.id.edtField);
        final EditText edtValue=dialogView.findViewById(R.id.edtValue);
        Button btnOk=dialogView.findViewById(R.id.btnOk);
        Button btnCancel=dialogView.findViewById(R.id.btnCancel);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                String field=edtField.getText().toString();
                String value=edtValue.getText().toString();
                if(field.trim().length()<1){
                    edtField.setError("Field can't be empty");
                    return;
                }
                if(value.trim().length()<1){
                    edtValue.setError("Value can't be empty");
                    return;
                }
                alertDialog.cancel();
                specificationArrayList.add(new Specification(field,value));
                specAdapter.notifyDataSetChanged();
                recyclerView.scrollToPosition(specificationArrayList.size()-1);
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.cancel();
            }
        });
    }
    public void addItem(View view) {
        String name=edtName.getText().toString(),description=edtDesc.getText().toString(),price=edtPrice.getText().toString();
        if(name.trim().length()<1){
            edtName.setError("name too short");
            return;
        } if(description.trim().length()<1){
            edtDesc.setError("Description too short");
            return;
        } if(price.trim().length()<1){
            edtPrice.setError("enter price");
            return;
        }
        if(upload<selected){
            savedData.toast("please wait while photos are getting upload");
            return;
        }
        specificationArrayList.add(new Specification("product no.","p"+new SimpleDateFormat("yyMMddHHmm").format(new Date(System.currentTimeMillis()))));
        Item item=new Item(name,description,price,imageList,specificationArrayList);
        item.setShopId(savedData.getValue("shopId"));
        item.setCategory(category);
        connection.getDbSchool().child(savedData.getValue("shopId")).child("items")
                .push().setValue(item);
        savedData.toast("Item saved");
        finish();
        startActivity(new Intent(this,AddItemActivity.class));
    }

    @Override
    protected void onStart() {
        super.onStart();
        connection.getDbSchool().child(savedData.getValue("shopId"))
                .child("category").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                categoryList.clear();
                categoryList.add("Select category");
                for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                    categoryList.add(dataSnapshot1.getValue(String.class));
                }
                categoryAdapter.notifyDataSetChanged();
                categoryList.add(add_category);
                connection.getDbSchool().child(savedData.getValue("shopId"))
                        .child("category").removeEventListener(this);
                }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private void addCategory() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add a new category");
        final EditText input = new EditText(this);
        builder.setView(input);
        builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                final String m_Text = input.getText().toString();
                connection.getDbSchool().child(savedData.getValue("shopId")).child("category")
                        .push().setValue(m_Text);
                categoryList.add(categoryList.size()-1,m_Text);
                categoryAdapter.notifyDataSetChanged();
                dialog.cancel();
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
    public void onBackPressed() {
        if(selected>0){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Do you want to exit without saving the item");
            builder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    for(String url:imageList){
                        storage.getInsatance().getReferenceFromUrl(url).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                            }
                        });
                    }
                    AddItemActivity.super.onBackPressed();
                    dialog.cancel();
                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            builder.show();
        }else{
            super.onBackPressed();
        }
    }
}
