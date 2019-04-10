package com.dreamteam.dreamteam.Group.Protocols;

import android.graphics.Bitmap;

public interface GroupViewInterface {
    void outputImageView(Bitmap bitmap);

    void outputGroupView(String title, String description);

    void error(String error);
}
