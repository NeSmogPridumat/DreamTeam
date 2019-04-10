package com.dreamteam.dreamteam.Group.Interactor;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;

import com.dreamteam.dreamteam.Group.Entity.GroupData.Group;
import com.dreamteam.dreamteam.Group.Protocols.GroupPresenterInterface;
import com.dreamteam.dreamteam.DataStore.HTTP.HTTPConfig;
import com.dreamteam.dreamteam.DataStore.HTTP.HTTPManager;
import com.dreamteam.dreamteam.Group.Protocols.GroupHTTPMangerInterface;
import com.google.gson.Gson;

public class GroupInteractor implements GroupHTTPMangerInterface {

    private final static String TAG = "UserInteractor";

    //===========================КОНСТАНТЫ ДЛЯ ТИПОВ ЗАПРОСА===================================//
    private final String GET_GROUP_TYPE = "getUser";
    private final String IMAGE_TYPE = "image";

    private GroupPresenterInterface delegate;
    private HTTPManager httpManager = HTTPManager.get();
    private HTTPConfig httpConfig = new HTTPConfig();

    public GroupInteractor(GroupPresenterInterface delegate) {
        this.delegate = delegate;
    }

    //отправка GET запроса в httpManager
    public void getGroup(String id) {
        //создание ссылки
        final String path = httpConfig.serverURL + httpConfig.groupPORT + httpConfig.reqGroup + "?id=" + id;
        //запуск фонового потока
        new Thread(new Runnable() {//---------------------------------------------------------------запуск в фоновом потоке
            @Override
            public void run() {
                httpManager.getRequest(path, GET_GROUP_TYPE, GroupInteractor.this);//----------отправка в HTTPManager
            }
        }).start();
    }

    //получение json ответа, преобразование его в User и вывод в основной поток
    private void prepareGetGroupResponse(byte[] byteArray) {
        try {

            //получение Group из массива данных
            final Group group = createGroupOfBytes(byteArray);

            if (group == null) {
                String error = "Объект не существует";
                delegate.error(error);
            }
            //отправка title и description группы в основном потоке
            Handler mainHandler = new Handler(Looper.getMainLooper());
            Runnable myRunnable = new Runnable() {
                @Override
                public void run() {
                    delegate.answerGetGroup(group.content.simpleData.title, group.content.simpleData.description);
                }
            };
            mainHandler.post(myRunnable);

            //отправка запроса на получение картинки(продолжает работу в фоновом потоке)
            getImageRequest(group);

            Thread.currentThread().interrupted();
        } catch (Exception error) {
            error(error);
        }
    }

    //создание слабой ссылки и отправка запроса в httpManager
    private void getImageRequest(Group group) {//-----------------------------------------------------получение картинки

        ThreadLocal tl = new ThreadLocal();
        try {
            tl.set(System.nanoTime());
            String imageUrl = httpConfig.serverURL + httpConfig.groupPORT + group.content.mediaData.image;
            httpManager.getRequest(imageUrl, IMAGE_TYPE, GroupInteractor.this);
        }
        finally {
            tl.remove();
        }
    }

    //создание Group из массива байтов
    private Group createGroupOfBytes(byte[] byteArray) {
        Gson gson = new Gson();
        String jsonString = new String(byteArray);
        return gson.fromJson(jsonString, Group.class);
    }

    //получение картинки(преобразование в bitmap) отправка ответа в основном потоке
    private void prepareGetImageResponse(byte[] byteArray) {
        final Bitmap bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
        Handler mainHandler = new Handler(Looper.getMainLooper());
        Runnable myRunnable = new Runnable() {
            @Override
            public void run() {
                delegate.answerGetImage(bitmap);
            }
        };
        mainHandler.post(myRunnable);
        Thread.currentThread().interrupted();
    }


    //----------------------------------------ПОЛУЧЕНИЕ ДАННЫХ ОТ HTTP MANAGER И ВЫЗОВ ФУНКЦИЙ ДЛЯ ОБРАБОТКИ-----------------------------//
    @Override
    public void response(byte[] byteArray, String type) {
        if (type.equals(GET_GROUP_TYPE)) {
            prepareGetGroupResponse(byteArray);
    }else if (type.equals(IMAGE_TYPE)) {
            prepareGetImageResponse(byteArray);
        }
    }

    @Override
    public void error(Throwable t) {

    }
}
