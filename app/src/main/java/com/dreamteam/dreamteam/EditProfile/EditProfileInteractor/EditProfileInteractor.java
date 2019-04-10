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


    public void putUser(final User user, final Bitmap bitmap){//------------------------------------Отправка User

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

    private String decodeBitmapInBase64 (Bitmap bitmap){//------------------------------------------декодирование Bitmap в Base64
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        // Получаем изображение из потока в виде байтов
        byte[] bytes = byteArrayOutputStream.toByteArray();
        return PREFIX + Base64.encodeToString(bytes, Base64.NO_WRAP);
    }

    @Override
    public void response(byte[] byteArray, String type) {//-----------------------------------------распределение получиных ответов с сервера
        if (type.equals(PUT_USER)){
            putUserResponse(byteArray);
        }else if (type.equals(IMAGE_TYPE)) {
            getImage(byteArray);
        }
    }

    private void putUserResponse(byte[] byteArray) {//----------------------------------------------обработка полученных данных
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
                    delegate.answerPutUser(user);//выполнение дальнейшего кода в основном потоке
                }
            };
            mainHandler.post(myRunnable);

            getImageResponse(user);
            Thread.currentThread().interrupted();//закрытие текущего потока
        } catch (Exception error) {
            error(error);
        }
    }

    @Override
    public void error(Throwable t) {

    }

    private User createUserOfBytes(byte[] byteArray) {//--------------------------------------------получение User из массива данных
        Gson gson = new Gson();
        String jsonString = new String(byteArray);
        return gson.fromJson(jsonString, User.class);
    }

    public void getImageResponse(User user) {//------------------------------------------------------получение картинки
        try {
            String imageUrl = config.serverURL + config.userPORT + user.content.mediaData.image;
            httpManager.getRequest(imageUrl, IMAGE_TYPE, EditProfileInteractor.this);
        } catch (Exception ioe) {
            Log.e("EditProfileInteractor", "Error downloading image", ioe);
        }
    }
    public void getImage(byte[] byteArray) {//-------------------------------------------------------получение картинки(преобразование в bitmap)
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
