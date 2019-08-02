package com.multi.myschoolshop.utility;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.multi.myschoolshop.R;
import com.multi.myschoolshop.buy_n_cart.Order;

import java.util.ArrayList;

public class Animator {
    TextView textView1,textView2,textView3,textView4;
    LinearLayout ll1,ll2;
    private boolean stop=false;

    public Animator(View view) {
        this.textView1 = view.findViewById(R.id.text1);
        this.textView2 = view.findViewById(R.id.text2);
        this.textView3 = view.findViewById(R.id.text3);
        this.textView4 = view.findViewById(R.id.text4);
        this.ll1=view.findViewById(R.id.ll1);
        this.ll2=view.findViewById(R.id.ll2);
    }

    public void startAnimation() {
        textView1.animate().translationX(1024).setDuration(500).setStartDelay(200);
        textView2.animate().translationX(-1024).setDuration(500).setStartDelay(400);
        textView3.animate().translationX(1024).setDuration(500).setStartDelay(600);
        textView4.animate().translationX(-1024).setDuration(500).setStartDelay(800)
                .withEndAction(new Runnable() {
            @Override
            public void run() {
                if(!stop){
                    reverseAnimation();
                }
            }
        });
    }

    private void reverseAnimation() {
        textView1.animate().translationX(0).setDuration(500);
        textView2.animate().translationX(0).setDuration(500).setStartDelay(200);
        textView3.animate().translationX(0).setDuration(500).setStartDelay(400);
        textView4.animate().translationX(0).setDuration(500).setStartDelay(600)
                .withEndAction(new Runnable() {
            @Override
            public void run() {
                if(!stop){
                    startAnimation();
                }
            }
        });
    }

    public void stopAnimation(ArrayList orderArrayList) {
        stop=true;
        textView1.setVisibility(View.GONE);
        textView2.setVisibility(View.GONE);
        textView3.setVisibility(View.GONE);
        textView4.setVisibility(View.GONE);
        if(ll1!=null){
            if(orderArrayList.size()==0){
                ll1.setVisibility(View.VISIBLE);
            }else{
                ll2.setVisibility(View.GONE);
                ll1.setVisibility(View.GONE);
            }
        }
    }
}
