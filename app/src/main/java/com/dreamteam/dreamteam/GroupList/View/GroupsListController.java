package com.dreamteam.dreamteam.GroupList.View;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dreamteam.dreamteam.Group.Entity.GroupData.Group;
import com.dreamteam.dreamteam.GroupList.Presenter.GroupsPresenter;
import com.dreamteam.dreamteam.GroupList.Protocols.GroupsViewInterface;
import com.dreamteam.dreamteam.MainActivity;
import com.dreamteam.dreamteam.R;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
@SuppressLint("ValidFragment")
public class GroupsListController extends Fragment implements GroupsViewInterface {

    private RecyclerView groupsRecyclerView;

    private GroupAdapter adapter;

    public GroupsPresenter groupsPresenter = new GroupsPresenter(this);

    ArrayList<Group> groups = new ArrayList<>();

    public GroupsListController() {

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_groups_list, container, false);
        groupsRecyclerView = view.findViewById(R.id.groups_recycler_view);
        groupsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        groupsPresenter.getGroups("328d21d2-9797-4802-9f5d-0e0b3f204866");//здесь ID User'а
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    //отправка полученного списка групп на отображение в адаптере
    @Override
    public void outputGroupsView(final ArrayList<Group> groupCollection) {

        //инициализация адаптера
        initAdapter(groupCollection);

        //передача нового массива в адаптер
        adapter.groupCollection = groupCollection;

        //обновление адаптера
        adapter.notifyDataSetChanged();


        final Context context = getContext();

        //слушатель на действия по пунктам списка
        groupsRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getContext(), groupsRecyclerView, new RecyclerItemClickListener.OnItemClickListener() {

            //клики
            @Override
            public void onItemClick(View view, int position) {
                MainActivity activityAction;
                activityAction = (MainActivity)context;
                activityAction.getGroup(groupCollection.get(position).id);
            }

            //долгое нажатие
            @Override
            public void onLongItemClick(View view, int position) {

            }

        }));
    }

    //presenter отправляет bitmap/картинку в этот метод, он отправляет их на отображение в адаптере
    public void redrawAdapter(String groupID, Bitmap bitmap){
        if (bitmap != null) {
            adapter.changeItem(groupID, bitmap);
        }
    }

    @Override
    public void onStop() {
        adapter = null;
        super.onStop();
    }

    //Инициализация адаптера
    public void initAdapter(ArrayList<Group> groups){
        adapter  = new GroupAdapter(groups);
        groupsRecyclerView.setAdapter(adapter);
    }

    @Override
    public void onDestroy() {
        groupsPresenter = null;
        groups = null;
        super.onDestroy();
    }

}

