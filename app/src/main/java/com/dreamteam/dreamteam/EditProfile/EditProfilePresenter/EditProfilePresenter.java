package com.dreamteam.dreamteam.EditProfile.EditProfilePresenter;

import android.graphics.Bitmap;

import com.dreamteam.dreamteam.EditProfile.EditProfileInteractor.EditProfileInteractor;
import com.dreamteam.dreamteam.EditProfile.Protocols.PresenterEditProfileInterface;
import com.dreamteam.dreamteam.EditProfile.Protocols.ViewEditProfileInterface;
import com.dreamteam.dreamteam.User.Entity.UserData.User;

public class EditProfilePresenter implements PresenterEditProfileInterface {

    private ViewEditProfileInterface delegate;

    private EditProfileInteractor editProfileInteractor = new EditProfileInteractor(this);

    public EditProfilePresenter(ViewEditProfileInterface delegate){
        this.delegate = delegate;
    }

    //проброс команды Interactor'у
    public void putUser(User user, Bitmap bitmap){
        editProfileInteractor.putUser(user, bitmap);
    }

    @Override
    public void error(String error) {

    }

    @Override
    public void answerPutUser(User user) {

    }

    @Override
    public void answerPutImage(Bitmap bitmap) {
        delegate.answerPutRequest();
    }
}
