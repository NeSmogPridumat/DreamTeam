package com.dreamteam.dreamteam.Group.View;


import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.dreamteam.dreamteam.Group.Presenter.GroupPresenter;
import com.dreamteam.dreamteam.Group.Protocols.GroupViewInterface;
import com.dreamteam.dreamteam.R;

/**
 * A simple {@link Fragment} subclass.
 */
@SuppressLint("ValidFragment")
public class GroupController extends Fragment implements GroupViewInterface {

    private TextView titleTextView, descriptionTextView;
    private ImageView groupImageView;

    GroupPresenter groupPresenter = new GroupPresenter(this);
    String groupID;

    @SuppressLint("ValidFragment")
    public GroupController(String id) {
        groupID = id;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_group_controller, container, false);
        titleTextView = view.findViewById(R.id.group_title_text_view);
        descriptionTextView = view.findViewById(R.id.group_description_text_view);
        groupImageView = view.findViewById(R.id.group_image_view);
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {

        //отправка запроса в Presenter
        groupPresenter.getGroup(groupID);
        super.onCreate(savedInstanceState);
    }

    //получение и показ полученного Bitmap в ImageView
    @Override
    public void outputImageView(Bitmap bitmap) {
        groupImageView.setImageBitmap(bitmap);
    }

    //Получение и передача данных по User во View
    @Override
    public void outputGroupView(String title, String description) {
        titleTextView.setText(title);
        descriptionTextView.setText(description);
    }

    //Действия при ошибке
    @Override
    public void error(String error) {

    }
}
