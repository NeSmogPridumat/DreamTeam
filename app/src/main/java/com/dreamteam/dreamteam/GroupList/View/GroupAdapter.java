package com.dreamteam.dreamteam.GroupList.View;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dreamteam.dreamteam.Group.Entity.GroupData.Group;
import com.dreamteam.dreamteam.R;

import java.util.ArrayList;

public class GroupAdapter extends RecyclerView.Adapter<GroupHolder>{
    public ArrayList<Group> groupCollection;


    public GroupAdapter(ArrayList<Group> groupCollection){
        this.groupCollection = groupCollection;
    }

    //распределение полученных картинок по группам через ID
    public  void changeItem(String groupID, Bitmap bitmap){
        for (int i = 0; i < groupCollection.size(); i ++){
            Group group = groupCollection.get(i);
            if (group.id.equals(groupID)){
                group.content.mediaData.imageData = bitmap;
            }
        }
        this.notifyItemRangeChanged(0, groupCollection.size());
    }

    //====================================стандартный блок Adapter========================

    //постройка holder
    @NonNull
    @Override
    public GroupHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, final int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        View view = layoutInflater.inflate(R.layout.list_item_group, viewGroup, false);
        return new GroupHolder(view);
    }

    //распределение данных по holder'ам
    @Override
    public void onBindViewHolder(@NonNull GroupHolder groupsHolder, int i) {
        Group group = groupCollection.get(i);
        groupsHolder.bindGroup(group);
    }

    //полный размер массива
    @Override
    public int getItemCount() {
        return groupCollection.size();
    }
}

