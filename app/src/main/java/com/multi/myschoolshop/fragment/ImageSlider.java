package com.multi.myschoolshop.fragment;

import android.content.Context;
import android.os.Handler;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.multi.myschoolshop.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class ImageSlider extends PagerAdapter {

    private final Timer swipeTimer;
    int num_pages = 0;
    private ArrayList<String> imageModelArrayList;
    private LayoutInflater inflater;
    private Context context;
    ViewPager mPager;
    private int currentPage=0;

    public ImageSlider(ArrayList<String> imageModelArrayList, ViewPager mPager) {
        this.imageModelArrayList = imageModelArrayList;
        this.mPager = mPager;
        swipeTimer = new Timer();
        context=mPager.getContext();
        inflater=LayoutInflater.from(context);

    }

    private void init() {

    num_pages =imageModelArrayList.size();

        // Auto start of viewpager
        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (currentPage == num_pages) {
                    currentPage = 0;
                }else {
                    currentPage=mPager.getCurrentItem()+1;
                }
                mPager.setCurrentItem(currentPage++, true);
            }
        };
        swipeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(Update);
            }
        }, 3000, 3000);


    }




    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return imageModelArrayList.size();
    }

    @Override
    public Object instantiateItem(ViewGroup view, int position) {
        View imageLayout = inflater.inflate(R.layout.slidingimages_layout, view, false);
        assert imageLayout != null;
        final ImageView imageView = imageLayout.findViewById(R.id.image);
        Picasso.get().load(imageModelArrayList.get(position)).into(imageView, new Callback() {
            @Override
            public void onSuccess() {
                init();
            }

            @Override
            public void onError(Exception e) {
            }
        });
        view.addView(imageLayout, 0);
        return imageLayout;
    }

    @Override
    public void restoreState(Parcelable state, ClassLoader loader) {
    }

    @Override
    public Parcelable saveState() {
        return null;
    }
}
