package com.multi.myschoolshop.utility;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.multi.myschoolshop.R;

import java.util.ArrayList;

public class GridAdapter extends RecyclerView.Adapter<GridAdapter.Holder> {
    ArrayList<GridClass> list;
    Context context;
    public GridAdapter(ArrayList<GridClass> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        context=viewGroup.getContext();
        return new Holder(LayoutInflater.from(context).inflate(R.layout.item_row_grid,viewGroup,false));
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int i) {
        GridClass gridClass=list.get(i);
        holder.tvTitle.setText(gridClass.getTitle());
        holder.tvDescription.setText(gridClass.getDescription());
        holder.imageView.setImageDrawable(context.getResources().getDrawable(context
        .getResources().getIdentifier(String.valueOf(gridClass.getRes()),null,context.getPackageName())));
        holder.imageView.setBackgroundColor(getColor(holder.getAdapterPosition()));
    }

    private int getColor(int adapterPosition) {
        int color[]=new int[]{R.drawable.circle_red,R.drawable.circle_green,R.drawable.circle_yellow,R.drawable.circle_red};
        return color[adapterPosition%4];
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class Holder extends RecyclerView.ViewHolder{
        ImageView imageView;
        TextView tvTitle,tvDescription;
        public Holder(@NonNull View itemView) {
            super(itemView);
            tvTitle=itemView.findViewById(R.id.tvTitle);
            tvDescription=itemView.findViewById(R.id.tvDescription);
            imageView=itemView.findViewById(R.id.imageView);
        }
    }
}
