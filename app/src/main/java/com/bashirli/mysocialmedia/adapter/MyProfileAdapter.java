package com.bashirli.mysocialmedia.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bashirli.mysocialmedia.databinding.RecyclerMyprofileBinding;
import com.bashirli.mysocialmedia.model.Data;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MyProfileAdapter extends RecyclerView.Adapter<MyProfileAdapter.MyHolder> {
    ArrayList<Data> arrayList;

    public MyProfileAdapter(ArrayList<Data> arrayList) {
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerMyprofileBinding recyclerMyprofileBinding=RecyclerMyprofileBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);

        return new MyHolder(recyclerMyprofileBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        if (arrayList.get(position).downloadurl == null) {
holder.recyclerMyprofileBinding.recyclernickname.setVisibility(View.GONE);
holder.recyclerMyprofileBinding.recyclerImage.setVisibility(View.GONE);
            holder.recyclerMyprofileBinding.recyclerCom.setText("Paylaşım yoxdur!");

        } else {

            holder.recyclerMyprofileBinding.recyclernickname.setVisibility(View.VISIBLE);
            holder.recyclerMyprofileBinding.recyclerImage.setVisibility(View.VISIBLE);
            String oldnickname = arrayList.get(position).user;
            String newnickname = "";
            for (int i = 0; i < oldnickname.length(); i++) {
                if (oldnickname.charAt(i) == '@') {
                    break;
                } else {
                    newnickname += oldnickname.charAt(i);
                }

                holder.recyclerMyprofileBinding.recyclernickname.setText(" "+newnickname);
                holder.recyclerMyprofileBinding.recyclerCom.setText(" "+newnickname+": "+arrayList.get(position).comment);
                Picasso.get().load(arrayList.get(position).downloadurl).into(holder.recyclerMyprofileBinding.recyclerImage);
            }
        }
    }
    @Override
    public int getItemCount() {
        if(arrayList.size()==0){
            Data nullData=new Data(null,null,null);
            arrayList.add(nullData);
        }else if(arrayList.size()>1){
            arrayList.remove(0);
        }
        return arrayList.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder{
private RecyclerMyprofileBinding recyclerMyprofileBinding;


        public MyHolder(@NonNull  RecyclerMyprofileBinding recyclerMyprofileBinding) {
            super(recyclerMyprofileBinding.getRoot());
            this.recyclerMyprofileBinding=recyclerMyprofileBinding;
        }
    }
}
