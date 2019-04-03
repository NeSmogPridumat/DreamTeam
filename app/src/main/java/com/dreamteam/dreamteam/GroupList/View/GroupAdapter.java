package com.dreamteam.dreamteam.GroupList.View;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dreamteam.dreamteam.GroupList.Entity.GroupData.Group;
import com.dreamteam.dreamteam.R;

import java.util.ArrayList;

public class GroupAdapter extends RecyclerView.Adapter<GroupHolder>{
    public ArrayList<Group> groupCollection;

    GroupAdapter(ArrayList<Group> groupCollection){
        this.groupCollection = groupCollection;
    }

    public void changeItem(String groupID, Bitmap bitmap){//сравнивает id который пришел с картинкой и id групп из списка и выставляет подходящей группе эту каритнку
        for (int i = 0; i < groupCollection.size(); i ++){
            Group group = groupCollection.get(i);
            if (group.id.equals(groupID)){
                group.content.mediaData.imageData = bitmap;
            }
            this.notifyItemChanged(i);//обновление позиции
        }
    }

    @NonNull
    @Override
    public GroupHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        View view = layoutInflater.inflate(R.layout.list_item_group, viewGroup, false);
        return new GroupHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GroupHolder groupsHolder, int i) {//передача холдерам списка групп по позициям
        Group group = groupCollection.get(i);
        groupsHolder.bindGroup(group);

    }

    @Override
    public int getItemCount() {
        return groupCollection.size();
    }//передается размер массива
}

