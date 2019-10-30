package com.dreamteam.dreamteam.EditProfile.EditProfileInteractor;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;
import android.util.Base64;
import android.util.Log;

import com.dreamteam.dreamteam.EditProfile.Protocols.PresenterEditProfileInterface;
import com.dreamteam.dreamteam.DataStore.HTTP.HTTPConfig;
import com.dreamteam.dreamteam.DataStore.HTTP.HTTPManager;
import com.dreamteam.dreamteam.EditProfile.Protocols.EditProfileFromHTTPManagerInterface;
import com.dreamteam.dreamteam.User.Entity.UserData.User;
import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;

public class EditProfileInteractor implements EditProfileFromHTTPManagerInterface {

    private final String PUT_USER = "putUser";
    private final String IMAGE_TYPE = "image";
    private final String PREFIX = "data:image/jpeg;base64,";

    private HTTPConfig config = new HTTPConfig();

    private HTTPManager httpManager = HTTPManager.get();

    private PresenterEditProfileInterface delegate;

    public EditProfileInteractor (PresenterEditProfileInterface delegate){
        this.delegate = delegate;
    }

    //Отправка User
    public void putUser(final User user, final Bitmap bitmap){
        final String urlPath = config.serverURL + config.userPORT + config.reqUser;
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Gson gson = new Gson();
                    if(bitmap != null){//-----------------------------------------------------------если bitmap не равен null кодируем его в Base64
                        user.content.mediaData.image = decodeBitmapInBase64(bitmap);
                    }
                    final String jsonObject = gson.toJson(user);
                    httpManager.putRequest(urlPath, jsonObject, PUT_USER, EditProfileInteractor.this);//оправка данных для put запроса в httpManager
                } catch (Exception error) {
                    error(error);
                }
            }
        }).start();
    }

    //декодирование Bitmap в Base64
    private String decodeBitmapInBase64 (Bitmap bitmap){
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        // Получаем изображение из потока в виде байтов
        byte[] bytes = byteArrayOutputStream.toByteArray();
        return PREFIX + Base64.encodeToString(bytes, Base64.NO_WRAP);
    }

    //распределение получиных ответов с сервера
    @Override
    public void response(byte[] byteArray, String type) {
        if (type.equals(PUT_USER)){
            putUserResponse(byteArray);
        }else if (type.equals(IMAGE_TYPE)) {
            getImage(byteArray);
        }
    }

    //обработка полученных данных
    private void putUserResponse(byte[] byteArray) {
        try {
            final User user = createUserOfBytes(byteArray);
            if (user == null) {
                String error = "Объект не существует";
                delegate.error(error);
            }

            Handler mainHandler = new Handler(Looper.getMainLooper());
            Runnable myRunnable = new Runnable() {
                @Override
                public void run() {

                    //выполнение дальнейшего кода в основном потоке
                    delegate.answerPutUser(user);
                }
            };
            mainHandler.post(myRunnable);

            //отправка запроса на получение картинки
            getImageResponse(user);

            //закрытие текущего потока
            Thread.currentThread().interrupted();
        } catch (Exception error) {
            error(error);
        }
    }

    @Override
    public void error(Throwable t) {

    }

    //Получение User из массива данных
    private User createUserOfBytes(byte[] byteArray) {
        Gson gson = new Gson();
        String jsonString = new String(byteArray);
        return gson.fromJson(jsonString, User.class);
    }

    //получение картинки
    private void getImageResponse(User user) {
        try {
            String imageUrl = config.serverURL + config.userPORT + user.content.mediaData.image;
            httpManager.getRequest(imageUrl, IMAGE_TYPE, EditProfileInteractor.this);
        } catch (Exception ioe) {
            Log.e("EditProfileInteractor", "Error downloading image", ioe);
        }
    }

    //получение картинки(преобразование в bitmap)
    private void getImage(byte[] byteArray) {
        final Bitmap bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);

        Handler mainHandler = new Handler(Looper.getMainLooper());
        Runnable myRunnable = new Runnable() {
            @Override
            public void run() {
                delegate.answerPutImage(bitmap);
            }
        };
        mainHandler.post(myRunnable);
        Thread.currentThread().interrupted();
    }
}
