package com.dreamteam.dreamteam.EditProfile.Protocols;

import android.graphics.Bitmap;

import com.dreamteam.dreamteam.User.Entity.UserData.User;

public interface PresenterEditProfileInterface {
    void error(String error);

    void answerPutUser(User user);

    void answerPutImage(Bitmap bitmap);
}
