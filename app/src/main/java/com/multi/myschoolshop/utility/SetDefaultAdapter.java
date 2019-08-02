package com.multi.myschoolshop.utility;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.multi.myschoolshop.R;

import java.util.ArrayList;

public class SetDefaultAdapter extends RecyclerView.Adapter<SetDefaultAdapter.Holder> {
    ArrayList<SetDefaultClass> list;
    Context context;
    public SetDefaultAdapter(ArrayList<SetDefaultClass> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        context=viewGroup.getContext();
        return new Holder(LayoutInflater.from(context).inflate(R.layout.item_row_text,viewGroup,false));
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int i) {
        SetDefaultClass gridClass=list.get(i);
        holder.tvHeading.setText(gridClass.getDeptType());
        holder.tvTitle.setText(gridClass.getSchoolName());
        holder.tvDescription.setText(getDes(gridClass));
        try{
            holder.imageView.setImageDrawable(context.getResources().getDrawable(context
                    .getResources().getIdentifier(String.valueOf(gridClass.getRes()),null,context.getPackageName())));
        }catch (Exception e){

        }

    }

    private String getDes(SetDefaultClass gridClass) {
        try{
            if(Integer.parseInt(gridClass.getPosition())==7){
                switch (gridClass.getDeptType()){
                    case "school":
                        return "principal";
                    case "college":
                        return "HOD";
                    case "shop":
                        return "shop keeper";
                }
            }else{
                if(Integer.parseInt(gridClass.getPosition())==5){
                    return "teacher of "+gridClass.getStClass();
                }else
                if(Integer.parseInt(gridClass.getPosition())==2||Integer.parseInt(gridClass.getPosition())==4){
                    return "student of "+gridClass.getStClass();
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class Holder extends RecyclerView.ViewHolder{
        public ImageView imageView,btnEdit;
public         CheckBox checkBox;
       public TextView tvHeading,tvTitle,tvDescription;
        public Holder(@NonNull View itemView) {
            super(itemView);
            tvHeading=itemView.findViewById(R.id.tvHeading);
            tvTitle=itemView.findViewById(R.id.tvTitle);
            tvDescription=itemView.findViewById(R.id.tvDescription);
            imageView=itemView.findViewById(R.id.imageView);
            btnEdit=itemView.findViewById(R.id.btnEdit);
            checkBox=itemView.findViewById(R.id.checkbox);

        }
    }
}
