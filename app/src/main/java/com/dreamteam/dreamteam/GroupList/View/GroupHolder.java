package com.dreamteam.dreamteam.GroupList.View;

import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.dreamteam.dreamteam.Group.Entity.GroupData.Group;
import com.dreamteam.dreamteam.R;

public class GroupHolder extends RecyclerView.ViewHolder {
    private TextView titleTextView, descriptionTextView;
    public ImageView imageView;

    private Group group;

    public GroupHolder(View item) {
        super(item);
        titleTextView = item.findViewById(R.id.list_item_group_title_text_view);
        descriptionTextView = item.findViewById(R.id.list_item_group_description_text_view);
        imageView = item.findViewById(R.id.group_image_view);
    }

    public void bindGroup(Group group){
        this.group = group;
        titleTextView.setText(group.content.simpleData.title);
        descriptionTextView.setText(group.content.simpleData.description);

        Bitmap imageData = group.content.mediaData.imageData;
        imageView.setImageBitmap(imageData);
    }
}