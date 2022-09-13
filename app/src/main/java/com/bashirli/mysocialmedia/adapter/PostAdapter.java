package com.bashirli.mysocialmedia.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bashirli.mysocialmedia.databinding.RecyclerXmlBinding;
import com.bashirli.mysocialmedia.model.Data;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostHolder> {
    ArrayList<Data> arrayList;

    public PostAdapter(ArrayList<Data> arrayList) {
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public PostHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerXmlBinding recyclerXmlBinding=RecyclerXmlBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);

        return new PostHolder(recyclerXmlBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull PostHolder holder, int position) {
        String oldnickname = arrayList.get(position).user;
        String newnickname = "";
        for (int i = 0; i < oldnickname.length(); i++) {
            if (oldnickname.charAt(i) == '@') {
                break;
            } else {
                newnickname += oldnickname.charAt(i);
            }

            holder.recyclerXmlBinding.recyclernickname.setText(" "+newnickname);
            holder.recyclerXmlBinding.recyclerCom.setText(" "+newnickname+": "+arrayList.get(position).comment);
            Picasso.get().load(arrayList.get(position).downloadurl).into(holder.recyclerXmlBinding.recyclerImage);
        }
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class PostHolder extends RecyclerView.ViewHolder{
private RecyclerXmlBinding recyclerXmlBinding;
        public PostHolder(@NonNull RecyclerXmlBinding recyclerXmlBinding) {
            super(recyclerXmlBinding.getRoot());
            this.recyclerXmlBinding=recyclerXmlBinding;
        }
    }
}
