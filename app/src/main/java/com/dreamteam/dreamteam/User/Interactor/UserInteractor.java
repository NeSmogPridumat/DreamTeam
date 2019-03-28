package com.dreamteam.dreamteam.User.Interactor;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.dreamteam.dreamteam.DataStore.HTTP.HTTPManager;
import com.dreamteam.dreamteam.User.Entity.UserData.User;
import com.dreamteam.dreamteam.User.Protocols.PresenterUserInterface;
import com.dreamteam.dreamteam.User.Protocols.UserFromHTTPManagerInterface;
import com.google.gson.Gson;

import java.net.SocketTimeoutException;


public class UserInteractor implements UserFromHTTPManagerInterface {

    public final static String TAG = "UserInteractor";

    HTTPManager httpManager = HTTPManager.get();

    private PresenterUserInterface delegate;

    public UserInteractor(PresenterUserInterface delegate) {
        this.delegate = delegate;
    }

//----------------------------------ОТПРАВКА В HTTPMANAGER---------------------------------------//

    public void getUser(String id) {//----------------------------------отправка запроса на получение User по id
        final String path = "http://192.168.0.100:8888/user?id=" + id;
        new Thread(new Runnable() {//---------------------------------------------------------------запуск в фоновом потоке
            @Override
            public void run() {
                httpManager.getRequest(path, "user", UserInteractor.this);//----------отправка в HTTPManager
            }
        }).start();
    }


    public void postUser(String name, String surname) {//--------------отправка post-запроса на сервер
        final String type = "postUser";
        final User user = createUser(name, surname);
        Gson gson = new Gson();
        final String jsonObject = gson.toJson(user);
        final String path = "http://192.168.0.100:8888/user";
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    httpManager.postRequest(path, jsonObject, type, UserInteractor.this);
                } catch (Exception error) {
                    error(error);
                }
            }
        }).start();
    }


    public void getAfterPostUser(byte[] byteArray) {
        final User user = createUserOfBytes(byteArray);
        String path = "http://192.168.0.100:8888/user?id=" + user.id;
        httpManager.getRequest(path, "user", UserInteractor.this);
    }

//----------------------------------------ПОЛУЧЕНИЕ И ОБРАБОТКА ДАННЫХ-----------------------------//

    @Override
    public void response(byte[] byteArray, String type) {//-----------------------------------------получение ответа от HTTPManager и распределение по типу
        if (type == "user") {
            getUserResponse(byteArray);
        } else if (type == "postUser") {
            //TODO:возможно вынести в отдельный метод (либо совместить с getUser?)
            getAfterPostUser(byteArray);
        } else if (type == "image") {
            getImage(byteArray);
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


    public void getUserResponse(byte[] byteArray) {//-----------------------------------------------получение json ответа, преобразование его в User и вывод в основной поток
        Log.i("UserInteractor", "jsonString");
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

           // Log.i(TAG, "Поток " + Thread.currentThread().getName());
            getImageResponse(user);

        } catch (Exception error) {
            error(error);
        }
    }


    public void getImageResponse(User user) {//-----------------------------------------------------получение картинки
        try {
            String type = "image";
            String imageUrl = "http://192.168.0.100:8888" + user.content.mediaData.image;
            //Log.i(TAG, "Поток " + Thread.currentThread().getName());
            httpManager.getRequest(imageUrl, type, UserInteractor.this);
        } catch (Exception ioe) {
            Log.e(TAG, "Error downloading image", ioe);
        }
    }


    public void getImage(byte[] byteArray) {//------------------------------------------------------получение картинки(преобразование в bitmap)
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

    private User createUser(String name, String surname) {//----------------------------------------создание User по name и surname
        User user = new User();
        user.content.simpleData.name = name;
        user.content.simpleData.surname = surname;
        return user;
    }

    private User createUserOfBytes(byte[] byteArray){//---------------------------------------------создание User из массива байтов
        Gson gson = new Gson();
        String jsonString = new String(byteArray);
        final User user = gson.fromJson(jsonString, User.class);
        return user;
    }
}


