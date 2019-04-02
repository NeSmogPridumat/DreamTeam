package com.dreamteam.httprequest.Group.Protocols;

import android.graphics.Bitmap;

import com.dreamteam.httprequest.Group.Entity.GroupData.Group;

import java.util.ArrayList;

public interface GroupViewInterface {

    void redrawAdapter(String groupID, Bitmap bitmap);

    void outputGroupsView (ArrayList<Group> groupCollection);
}
