package com.dreamteam.dreamteam.User.Protocols;

import android.graphics.Bitmap;

import com.dreamteam.dreamteam.User.Entity.UserData.User;

public interface PresenterUserInterface {
    void answerGetUser(User user);

    void answerGetImage(Bitmap bitmap);

    void error(String error);
}
