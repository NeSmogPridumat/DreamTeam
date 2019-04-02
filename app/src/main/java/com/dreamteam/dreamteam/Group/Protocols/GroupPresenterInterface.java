package com.dreamteam.dreamteam.Group.Protocols;

import android.graphics.Bitmap;

import com.dreamteam.dreamteam.Group.Entity.GroupData.Group;

import java.util.ArrayList;

public interface GroupPresenterInterface {

    void answerGetImageGroups(String groupID, Bitmap bitmap);

    void answerGetGroups(ArrayList<Group> groupCollection);

    void error(String error);
}
