package com.dreamteam.dreamteam.User.Protocols;

import android.graphics.Bitmap;
import com.dreamteam.dreamteam.User.Entity.UserData.User;

public interface UserViewInterface {
    void View(User user);

    void ViewImage(Bitmap bitmap);

    void  error (String error);
}
