package com.dreamteam.httprequest.Group.View;


import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dreamteam.httprequest.Group.Entity.GroupData.Group;
import com.dreamteam.httprequest.Group.Presenter.GroupPresenter;
import com.dreamteam.httprequest.Group.Protocols.GroupViewInterface;
import com.dreamteam.httprequest.R;
import com.dreamteam.httprequest.UserFragment;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class GroupsListFragment extends Fragment implements GroupViewInterface {

    private RecyclerView groupsRecyclerView;
    private ArrayList<Group> groups;

    private GroupAdapter adapter = new GroupAdapter(groups);


    private String groupID;
    private Bitmap bitmap;

    private GroupHolder groupHolder;

    public GroupPresenter groupPresenter = new GroupPresenter(this);

    public GroupsListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_groups_list, container, false);

        groupsRecyclerView = view.findViewById(R.id.groups_recycler_view);
        groupsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        groupPresenter.getGroups("328d21d2-9797-4802-9f5d-0e0b3f204866");


        return view;
    }

    @Override
    public void outputGroupsView(ArrayList<Group> groupCollection) {
        groups = groupCollection;
        groupsRecyclerView.setAdapter(adapter);
        updateUI();
        int nbRunning = 0;
        for (Thread t : Thread.getAllStackTraces().keySet()) {
            if (t.getState()==Thread.State.RUNNABLE) nbRunning++;
        }
        String threads = String.valueOf(nbRunning);
        Log.i("================================", threads);
    }

    private void updateUI(){

        //TODO: не создавать новый адаптер
        adapter.groupCollection = groups;
        //TODO: вынести в ииницализацию
        adapter.notifyDataSetChanged();
    }

     public void redrawAdapter(String groupID, Bitmap bitmap){
        adapter.changeItem(groupID, bitmap);
    }

    @Override
    public void onDestroy() {
        groupsRecyclerView.setAdapter(null);
        super.onDestroy();
    }
}

