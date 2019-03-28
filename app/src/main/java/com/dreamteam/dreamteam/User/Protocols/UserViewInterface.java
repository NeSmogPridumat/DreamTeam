package com.dreamteam.dreamteam.User.Protocols;

import android.graphics.Bitmap;
import com.dreamteam.dreamteam.User.Entity.UserData.User;

public interface UserViewInterface {
    void outputUserView(User user);

    void outputImageView(Bitmap bitmap);

    void  error(String error);
}
