package com.multi.myschoolshop.utility;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.multi.myschoolshop.BuildConfig;
import com.squareup.picasso.Callback;

public class SavedData extends AppCompatActivity {
    private static final int MY_PERMISSIONS_REQUEST_CODE = 123;
    SharedPreferences  sharedPreferences;
    SharedPreferences.Editor editor;
    Context context;
    ProgressDialog progressDialog;
    Callback callback;
    public SavedData(Context context) {
        this.context = context;
        sharedPreferences=context.getSharedPreferences("store",Context.MODE_PRIVATE);
        editor=sharedPreferences.edit();
        progressDialog=new ProgressDialog(context);

    }

    public String getValue(String key) {
        return sharedPreferences.getString(key,"Schooly");
    }

    public void setValue(String key,String value) {
        editor.putString(key,value);
        editor.commit();
    }


    public void removeAlert() {
        progressDialog.dismiss();
    }

    public void showAlert(String message) {
        try{
            progressDialog.setMessage(message);
            progressDialog.show();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public boolean needHelp() {
        if(getValue("help")!=null && getValue("help").equals("no")){
            return false;
        }
        return true;
    }

    public void toast(String message) {
        Toast.makeText(context,message,Toast.LENGTH_SHORT).show();
    }

    public String getType() {
        if(getValue("deptType")==null){
            return "schoolName";
        }else if(getValue("deptType").equals("schoolName")){
            return "schoolName";
        }else {
            return getValue("uid");
        }
    }

    public void test(String message) {
        if(BuildConfig.DEBUG){
            Toast.makeText(context,message,Toast.LENGTH_LONG).show();
        }
    }

    public void clear() {
        editor.clear();
        editor.commit();
    }


    public boolean haveValue(String key) {
        return !getValue(key).equals(getValue("default"));
    }

    public void log(String message) {
        Log.e(context.getClass().getSimpleName(),message);
    }

    public boolean hasPermission(String permission, Activity activity, Callback callback) {
        this.callback=callback;
        if (ContextCompat.checkSelfPermission(context,permission )
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity,
                    new String[]{permission},
                    MY_PERMISSIONS_REQUEST_CODE);
           return false;
        }else{
            return true;
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_CODE: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    callback.onSuccess();
                } else {
                    Exception e=new Exception(){
                        @Override
                        public String getMessage() {
                            return "permission not granted";
                        }
                    };
                    callback.onError(e);
                toast("please provide the required permission");
                }
                return;
            }
        }
    }

}
