package com.dreamteam.dreamteam.Group.Protocols;

import android.graphics.Bitmap;

import com.dreamteam.dreamteam.Group.Entity.GroupData.Group;

import java.util.ArrayList;

public interface GroupViewInterface {

    void redrawAdapter(String groupID, Bitmap bitmap);

    void outputGroupsView(ArrayList<Group> groupCollection);
}
