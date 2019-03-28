package com.dreamteam.dreamteam.User.Presenter;

import android.graphics.Bitmap;

import com.dreamteam.dreamteam.User.Entity.UserData.User;
import com.dreamteam.dreamteam.User.Interactor.UserInteractor;
import com.dreamteam.dreamteam.User.Protocols.UserPresenterInterface;
import com.dreamteam.dreamteam.User.Protocols.UserViewInterface;

public class UserPresenter implements UserPresenterInterface {

    private UserViewInterface delegate;
    private UserInteractor userInteractor = new UserInteractor(this);

    public UserPresenter(UserViewInterface delegate) {
        this.delegate = delegate;
}

    //==================================ОТПРАВКА В INTERACTOR==================================//
    public void getUser(String id) {
        userInteractor.getUser(id);
    }

    public void postUser(String name, String surname) {
        userInteractor.postUser(name, surname);
    }

    //========================вывод данных в UserViewController===============================//
    @Override
    public void answerGetUser(User user) {
        delegate.outputUserView(user);
    }

    public void answerGetImage(Bitmap bitmap) {
        delegate.outputImageView(bitmap);
    }
    @Override
    public void error(String error) {
        delegate.error(error);
    }

}

