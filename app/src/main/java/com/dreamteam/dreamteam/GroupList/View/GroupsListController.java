package com.dreamteam.dreamteam.GroupList.View;


import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dreamteam.dreamteam.GroupList.Entity.GroupData.Group;
import com.dreamteam.dreamteam.GroupList.Presenter.GroupsPresenter;
import com.dreamteam.dreamteam.GroupList.Protocols.GroupsViewInterface;
import com.dreamteam.dreamteam.R;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class GroupsListController extends Fragment implements GroupsViewInterface {

    private RecyclerView groupsRecyclerView;

    private GroupAdapter adapter;

    public GroupsPresenter groupPresenter = new GroupsPresenter(this);

    public GroupsListController() {

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_groups_list, container, false);
        groupsRecyclerView = view.findViewById(R.id.groups_recycler_view);
        groupsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        ArrayList<Group> groups = new ArrayList<>();
        initAdapter(groups);

        groupPresenter.getGroups("328d21d2-9797-4802-9f5d-0e0b3f204866");

        return view;
    }

    @Override
    public void outputGroupsView(ArrayList<Group> groupCollection) {//отправка полученного списка групп на отображение в адаптере

        adapter.groupCollection = groupCollection;
        adapter.notifyDataSetChanged();
    }

     public void redrawAdapter(String groupID, Bitmap bitmap){//presenter отправляет bitmap/картинку в этот метод, он отправляет их на отображение в адаптере
        adapter.changeItem(groupID, bitmap);
    }

    @Override
    public void onDestroy() {
        groupsRecyclerView.setAdapter(null);//передает адаптеру, null чтобы он не хранил отображение старого адаптера и не забивал память
        super.onDestroy();
    }

    public void initAdapter(ArrayList<Group> groups){//инициализация адаптера
        adapter  = new GroupAdapter(groups);
        groupsRecyclerView.setAdapter(adapter);
    }
}

