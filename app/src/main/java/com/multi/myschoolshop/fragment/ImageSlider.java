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
import android.widget.TextView;

import com.multi.myschoolshop.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class ImageSlider extends PagerAdapter {

    private ArrayList<String> imageModelArrayList;
    private LayoutInflater inflater;
    private Context context;
    ViewPager mPager;

    public ImageSlider(ArrayList<String> imageModelArrayList, ViewPager mPager) {
        this.imageModelArrayList = imageModelArrayList;
        this.mPager = mPager;
        context=mPager.getContext();
        inflater=LayoutInflater.from(context);

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
        Picasso.get().load(imageModelArrayList.get(position)).into(imageView);
        view.addView(imageLayout, 0);
        TextView textView=imageLayout.findViewById(R.id.tvText);
        textView.setText((position+1)+"/"+getCount());
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
