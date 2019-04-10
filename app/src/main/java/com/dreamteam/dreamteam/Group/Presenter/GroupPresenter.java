package com.dreamteam.dreamteam.Group.Presenter;

import android.graphics.Bitmap;

import com.dreamteam.dreamteam.Group.Interactor.GroupInteractor;
import com.dreamteam.dreamteam.Group.Protocols.GroupPresenterInterface;
import com.dreamteam.dreamteam.Group.Protocols.GroupViewInterface;

public class GroupPresenter implements GroupPresenterInterface {
    private GroupViewInterface delegate;
    private GroupInteractor groupInteractor = new GroupInteractor(this);


    public GroupPresenter(GroupViewInterface delegate){
        this.delegate = delegate;
    }

    //============================ОТПРАВКА В INTERACTOR==========================================//

    public void getGroup(String id){
        groupInteractor.getGroup(id);
    }

    //=============================ОБРАБОТКА ОТВЕТОВ=============================================//
    @Override
    public void error(String error) {
        delegate.error(error);
    }

    //действие при получении User
    @Override
    public void answerGetGroup(String title, String description) {
        delegate.outputGroupView(title, description);
    }

    //действие при получении картинки
    @Override
    public void answerGetImage(Bitmap bitmap) {
        delegate.outputImageView(bitmap);
    }

//    public void openGroup(Group group, Router myRouter, Context context) {
//        myRouter.getGroup(group, context);
//    }
}
