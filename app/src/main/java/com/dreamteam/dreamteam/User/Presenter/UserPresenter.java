package com.dreamteam.dreamteam.User.Presenter;

import android.graphics.Bitmap;
import android.util.Log;

import com.dreamteam.dreamteam.User.Entity.UserData.User;
import com.dreamteam.dreamteam.User.Interactor.UserInteractor;
import com.dreamteam.dreamteam.User.Protocols.UserPresenterInterface;
import com.dreamteam.dreamteam.User.Protocols.UserViewInterface;

public class UserPresenter implements UserPresenterInterface {

    public UserViewInterface delegate;

    UserInteractor userInteractor = new UserInteractor(this);

    public UserPresenter(UserViewInterface delegate){
        this.delegate = delegate;
    }

    @Override
    public void answerGetUser(User user) {
        long threadId = Thread.currentThread().getId();
        Log.i("","Thread # " + threadId + " is doing this task");
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

    public void getUsers (){

    }
}

