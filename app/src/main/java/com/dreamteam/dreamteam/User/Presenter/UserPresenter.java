package com.dreamteam.dreamteam.User.Presenter;

import android.graphics.Bitmap;
import android.util.Log;

import com.dreamteam.dreamteam.User.Entity.UserData.User;
import com.dreamteam.dreamteam.User.Interactor.UserInteractor;
import com.dreamteam.dreamteam.User.Protocols.PresenterUserInterface;
import com.dreamteam.dreamteam.User.Protocols.ViewUserInterface;

public class UserPresenter implements PresenterUserInterface {

    public ViewUserInterface delegate;

    UserInteractor userInteractor = new UserInteractor(this);

    public UserPresenter(ViewUserInterface delegate){
        this.delegate = delegate;
    }

    @Override
    public void answerGetUser(User user) {
        delegate.View(user);
    }

    public void answerGetImage (Bitmap bitmap){
        delegate.ViewImage(bitmap);
    }

    @Override
    public void error(String error) {
        delegate.error(error);
    }

    public void getUser(String id){
        userInteractor.getUser(id);
    }

    public void postUser(String name, String surname){
        userInteractor.postUser(name, surname);
    }
}
