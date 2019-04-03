package com.dreamteam.dreamteam.GroupList.View;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.dreamteam.dreamteam.GroupList.Entity.GroupData.Group;
import com.dreamteam.dreamteam.R;

class GroupHolder extends RecyclerView.ViewHolder{
    private TextView titleTextView, descriptionTextView;
    private ImageView imageView;

    GroupHolder(View item) {
        super(item);

        titleTextView = item.findViewById(R.id.list_item_group_title_text_view);
        descriptionTextView = item.findViewById(R.id.list_item_group_description_text_view);

        imageView = item.findViewById(R.id.group_image_view);
    }

    void bindGroup(Group group){//распределение данных по view holder
        titleTextView.setText(group.content.simpleData.title);
        descriptionTextView.setText(group.content.simpleData.description);

        imageView.setImageBitmap(group.content.mediaData.imageData);
    }
}
