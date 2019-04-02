package com.dreamteam.httprequest.Group.Presenter;

import android.graphics.Bitmap;

import com.dreamteam.httprequest.Group.Entity.GroupData.Group;
import com.dreamteam.httprequest.Group.Interactor.GroupInteractor;
import com.dreamteam.httprequest.Group.Protocols.GroupPresenterInterface;
import com.dreamteam.httprequest.Group.Protocols.GroupViewInterface;

import java.util.ArrayList;

public class GroupPresenter implements GroupPresenterInterface {

    private GroupViewInterface delegate;
    private GroupInteractor groupInteractor = new GroupInteractor(this);

    public GroupPresenter(GroupViewInterface delegate){
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
        groupInteractor.getGroups(id);
    }
}
