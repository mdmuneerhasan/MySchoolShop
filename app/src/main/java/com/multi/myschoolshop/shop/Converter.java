package com.multi.myschoolshop.shop;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static android.content.ContentValues.TAG;


public class Converter extends AsyncTask<Uri, Integer, byte[]>{

    Activity activity;
    Context context;

    public Converter(Context context) {
        this.activity= (Activity) context;
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        Toast.makeText(getActivity(), "compressing image", Toast.LENGTH_SHORT).show();
    }

    private Context getActivity() {
        return activity;
    }

    @Override
    protected byte[] doInBackground(Uri... params) {
        Log.d(TAG, "doInBackground: started.");
        Bitmap mBitmap = null;
        try {
            mBitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), params[0]);
        } catch (IOException e) {
            e.printStackTrace();
        }
        byte[] bytes = null;
        Log.d(TAG, "doInBackground: megabytes before compression: " + mBitmap.getByteCount() / 1000000 );
        bytes = getBytesFromBitmap(mBitmap, 20);
        Log.d(TAG, "doInBackground: megabytes before compression: " + bytes.length / 1000000 );
        return bytes;
    }

    public static byte[] getBytesFromBitmap(Bitmap bitmap, int quality){
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, quality,stream);
        return stream.toByteArray();
    }

    @Override
    protected void onPostExecute(byte[] bytes) {
        super.onPostExecute(bytes);

    }
}
