package com.dreamteam.dreamteam.Group.Protocols;

import android.graphics.Bitmap;

public interface GroupPresenterInterface {
    void error(String error);

    void answerGetGroup(String title, String description);

    void answerGetImage(Bitmap bitmap);
}
