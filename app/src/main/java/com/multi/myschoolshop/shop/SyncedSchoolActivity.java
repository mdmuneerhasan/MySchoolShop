package com.multi.myschoolshop.shop;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.multi.myschoolshop.R;
import com.multi.myschoolshop.utility.Animator;

public class SyncedSchoolActivity extends AppCompatActivity {
    Animator animator;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_synced_school);
        animator=new Animator(new View(this));

        animator.startAnimation();


    }
}
