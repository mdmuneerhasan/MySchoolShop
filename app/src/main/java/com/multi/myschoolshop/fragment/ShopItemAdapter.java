package com.multi.myschoolshop.fragment;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import com.multi.myschoolshop.R;
import com.multi.myschoolshop.shop.Item;
import com.multi.myschoolshop.utility.SavedData;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ShopItemAdapter extends RecyclerView.Adapter<ShopItemAdapter.Holder> implements Filterable {
    private ArrayList<Item> list;
    private ArrayList<Item> list2;
    private Context context;

    public ShopItemAdapter(ArrayList<Item> list, ArrayList<Item> list2) {
        this.list = list;
        this.list2 = list2;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        context=viewGroup.getContext();
        return new Holder(LayoutInflater.from(context).inflate(R.layout.item_row_main,viewGroup,false));
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int i) {
        Item gridClass=list.get(i);
        holder.tvTitle.setText(gridClass.getName()+"\nRs "+gridClass.getPrice());
        try{        Picasso.get().load(gridClass.getImagesList().get(0)).into(holder.imageView);}catch (Exception e){}
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public Filter getFilter() {
        return filter;
    }
    private Filter filter=new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            if(list.isEmpty()){
                return null;
            }
            ArrayList<Item> filterList=new ArrayList<>();
            if(constraint ==null || constraint.length()<1){
                filterList.addAll(list2);
            }else{
                String pattern=constraint.toString().toLowerCase().trim();
                for(Item item:list2){
                    if(item.getName().toLowerCase().contains(pattern)){
                        filterList.add(item);
                    }
                }
            }

            FilterResults results=new FilterResults();
            results.values=filterList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            try{
                list.clear();
                list.addAll((ArrayList<Item>) results.values);
                notifyDataSetChanged();
            }catch (Exception e){}
        }
    };

    public class Holder extends RecyclerView.ViewHolder{
        public ImageView imageView;
        public TextView tvTitle;
        public Holder(@NonNull View itemView) {
            super(itemView);
            tvTitle=itemView.findViewById(R.id.tvTitle);
            imageView=itemView.findViewById(R.id.imageView);
        }
    }
}
