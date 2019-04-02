package com.dreamteam.dreamteam.Group.Interactor;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;

import com.dreamteam.dreamteam.Group.Entity.GroupData.Group;
import com.dreamteam.dreamteam.Group.Protocols.GroupPresenterInterface;
import com.dreamteam.dreamteam.DataStore.HTTP.HTTPConfig;
import com.dreamteam.dreamteam.DataStore.HTTP.HTTPManager;
import com.dreamteam.dreamteam.Group.Protocols.GroupHTTPManagerInterface;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

public class GroupInteractor implements GroupHTTPManagerInterface {

    private final String GET_GROUPS_TYPE = "getGroups";
    private final String GET_IMAGE_GROUP_TYPE = "getImageGroups";

    private HTTPConfig httpConfig = new HTTPConfig();

    private HTTPManager httpManager = HTTPManager.get();

    private GroupPresenterInterface delegate;

    public GroupInteractor(GroupPresenterInterface delegate){
        this.delegate = delegate;
    }

    //-------------------------------Входные функции из Presenter, отправка в HTTP MANAGER---------//

    public void getGroups (String userId){
        final String path = httpConfig.serverURL + httpConfig.groupPORT + httpConfig.reqGroup +
                httpConfig.reqUser + "?userID=" + userId;
        new Thread(new Runnable() {
            @Override
            public void run() {
                httpManager.getRequest(path, GET_GROUPS_TYPE, GroupInteractor.this);
            }
        }).start();
    }

    private void uploadImage(final String groupID, final String pathImage ){
        new Thread(new Runnable() {
            @Override
            public void run() {
                httpManager.getRequest(pathImage, GET_IMAGE_GROUP_TYPE + ":" + groupID, GroupInteractor.this);
            }
        }).start();
    }

    //--------------------------Получение данных из HTTP MANAGER и вызов функций обработки---------//

    @Override
    public void response(byte[] byteArray, String type) {
        if (type.equals(GET_GROUPS_TYPE)){
            prepareGetGroupsResponse(byteArray);
        }else if ((parsingStringType(type).length > 1) && (parsingStringType(type)[0]
                .equals(GET_IMAGE_GROUP_TYPE))){
                prepareGetBitmapOfByte(parsingStringType(type)[1], byteArray);
        }
    }

    @Override
    public void error(Throwable t) {

    }

    //-----------------------Обработка данных из HTTP MANAGER-------------------------------------//

    private void prepareGetBitmapOfByte(final String groupID, byte[] byteArray){
        if (byteArray != null){
            final Bitmap bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
            Handler mainHandler = new Handler(Looper.getMainLooper());
            Runnable myRunnable = new Runnable() {
                @Override
                public void run() {
                    delegate.answerGetImageGroups(groupID, bitmap);
                }
            };
            mainHandler.post(myRunnable);
        }
    }

    private void prepareGetGroupsResponse(byte[] byteArray){
        //TODO: узнать что делает final
        final ArrayList<Group> groupCollection = createGroupsOfBytes(byteArray);
        if (groupCollection == null){
            String error = " ";
            delegate.error(error);
        }

        Handler mainHandler = new Handler(Looper.getMainLooper());
        Runnable myRunnable = new Runnable() {
            @Override
            public void run() {
                delegate.answerGetGroups(groupCollection);
            }
        };
        mainHandler.post(myRunnable);

        getImageRequest(groupCollection);
    }

    //-----------------------------------------------------------------------------------------//

    private void getImageRequest (ArrayList<Group> groupCollection){//-------------------------------отправка запросов на получение картинок для списка групп
        if (groupCollection != null)
        for (int i = 0 ; i<groupCollection.size(); i++){
            Group group = groupCollection.get(i);
            String pathImage = httpConfig.serverURL + httpConfig.groupPORT + group.content.mediaData.image;
            uploadImage(group.id, pathImage);
        }
    }

    private String[] parsingStringType(String string){//--------------------------------------------разбор строки (GET_IMAGE_GROUP_TYPE + ":" + groupID)
        String delimiter = ":";
        return string.split(delimiter);
    }

    private ArrayList<Group> createGroupsOfBytes (byte[] byteArray){//----------------------создание массива групп из массива байтов
        Gson gson = new Gson();
        String jsonString = new String(byteArray);
        return gson.fromJson(jsonString, new TypeToken<ArrayList<Group>>(){}.getType());
    }
}


