package com.dreamteam.dreamteam.User.Interactor;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.dreamteam.dreamteam.DataStore.HTTP.HTTPConfig;
import com.dreamteam.dreamteam.DataStore.HTTP.HTTPManager;
import com.dreamteam.dreamteam.User.Entity.UserData.User;
import com.dreamteam.dreamteam.User.Protocols.UserPresenterInterface;
import com.dreamteam.dreamteam.User.Protocols.UserHTTPManagerInterface;
import com.google.gson.Gson;

import java.net.SocketTimeoutException;


public class UserInteractor implements UserHTTPManagerInterface {

    private final static String TAG = "UserInteractor";

    //===========================КОНСТАНТЫ ДЛЯ ТИПОВ ЗАПРОСА===================================//
    private final String POST_USER_TYPE = "postUser";
    private final String IMAGE_TYPE = "image";
    private final String GET_USER_TYPE = "getUser";
    private final String _ID_USER = "?id=";

    private HTTPConfig httpConfig = new HTTPConfig();

    private HTTPManager httpManager = HTTPManager.get();

    private UserPresenterInterface delegate;

    public UserInteractor(UserPresenterInterface delegate) {
        this.delegate = delegate;
    }

//----------------------------------Входные функции из presenter ОТПРАВКА В HTTPMANAGER---------------------------------------//

    public void getUser(String id) {//--------------------------------------------------------------отправка запроса на получение User по id
        final String path = httpConfig.serverURL + httpConfig.userPORT + httpConfig.reqUser + _ID_USER + id;
        new Thread(new Runnable() {//---------------------------------------------------------------запуск в фоновом потоке
            @Override
            public void run() {
                httpManager.getRequest(path, GET_USER_TYPE, UserInteractor.this);//----------отправка в HTTPManager
            }
        }).start();
    }

    public void postUser(String name, String surname) {//-------------------------------------------отправка post-запроса на сервер
        final String jsonObject = createUser(name, surname);
        final String path = httpConfig.serverURL + httpConfig.userPORT + httpConfig.reqUser;
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    httpManager.postRequest(path, jsonObject, POST_USER_TYPE, UserInteractor.this);
                } catch (Exception error) {
                    error(error);
                }
            }
        }).start();
    }

    private void getImageRequest(User user) {//-----------------------------------------------------получение картинки
        try {
            String imageUrl = httpConfig.serverURL + httpConfig.userPORT + user.content.mediaData.image;
            httpManager.getRequest(imageUrl, IMAGE_TYPE, UserInteractor.this);
        } catch (Exception ioe) {
            Log.e(TAG, "Error downloading image", ioe);
        }
    }


//----------------------------------Входные функции из presenter ОТПРАВКА В HTTPMANAGER---------------------------------------//


    private User createUserOfBytes(byte[] byteArray){//---------------------------------------------создание User из массива байтов
        Gson gson = new Gson();
        String jsonString = new String(byteArray);
        return gson.fromJson(jsonString, User.class);
    }

    private String objToJSON(Object obj){
        return new Gson().toJson(obj);
    }

    private String createUser(String name, String surname) {//--------------------------------------создание jsonString User по name и surname
        User user = new User();
        user.content.simpleData.name = name;
        user.content.simpleData.surname = surname;

        return objToJSON(user);
    }


//----------------------------------ОБРАБОТКА ДАННЫХ ИЗ HTTP MANAGER---------------------------------------//
    private void prepareGetUserResponse(byte[] byteArray) {//-----------------------------------------------получение json ответа, преобразование его в User и вывод в основной поток
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
                    delegate.answerGetUser(user);
                }
            };
            mainHandler.post(myRunnable);

            getImageRequest(user);
        } catch (Exception error) {
            error(error);
        }
    }

    private void prepareGetImageResponse(byte[] byteArray) {//------------------------------------------------------получение картинки(преобразование в bitmap)
        final Bitmap bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);

        Handler mainHandler = new Handler(Looper.getMainLooper());
        Runnable myRunnable = new Runnable() {
            @Override
            public void run() {
                delegate.answerGetImage(bitmap);
            }
        };
        mainHandler.post(myRunnable);
    }


    //----------------------------------------ПОЛУЧЕНИЕ ДАННЫХ ОТ HTTP MANAGER И ВЫЗОВ ФУНКЦИЙ ДЛЯ ОБРАБОТКИ-----------------------------//

    @Override
    public void response(byte[] byteArray, String type) {//-----------------------------------------получение ответа от HTTPManager и распределение по типу
        if (type.equals(GET_USER_TYPE)) {
            prepareGetUserResponse(byteArray);
        } else if (type.equals(POST_USER_TYPE)) {

        } else if (type.equals(IMAGE_TYPE)) {
            prepareGetImageResponse(byteArray);
        }
    }

    @Override
    public void error(Throwable t) {//--------------------------------------------------------------Обработка ошибки
        String error = null;
        if (t instanceof SocketTimeoutException) {
            error = "Ошибка ожидания сервера";
        }
        if (t instanceof NullPointerException) {
            error = "Объект не найден";
        }
        delegate.error(error);
        Log.e(TAG, "Failed server" + t.toString());
    }

}


