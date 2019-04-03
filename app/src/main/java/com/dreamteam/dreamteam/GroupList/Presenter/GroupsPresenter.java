package com.dreamteam.dreamteam.GroupList.Presenter;

import android.graphics.Bitmap;

import com.dreamteam.dreamteam.GroupList.Entity.GroupData.Group;
import com.dreamteam.dreamteam.GroupList.Interactor.GroupsInteractor;
import com.dreamteam.dreamteam.GroupList.Protocols.GroupsPresenterInterface;
import com.dreamteam.dreamteam.GroupList.Protocols.GroupsViewInterface;

import java.util.ArrayList;

public class GroupsPresenter implements GroupsPresenterInterface {

    private GroupsViewInterface delegate;
    private GroupsInteractor groupsInteractor = new GroupsInteractor(this);

    public GroupsPresenter(GroupsViewInterface delegate){
        this.delegate = delegate;
    }

    @Override
    public void answerGetImageGroups(String groupID, Bitmap bitmap) {
        delegate.redrawAdapter(groupID, bitmap);
    }

    @Override
    public void answerGetGroups(ArrayList<Group> groupCollection) {
        delegate.outputGroupsView(groupCollection);
    }

    @Override
    public void error(String error) {

    }

    public void getGroups(String id) {
        groupsInteractor.getGroups(id);
    }
}
