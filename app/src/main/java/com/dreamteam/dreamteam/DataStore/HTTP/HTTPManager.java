package com.dreamteam.dreamteam.DataStore.HTTP;

import android.util.Log;

import com.dreamteam.dreamteam.DataStore.HTTP.OutputHTTPManagerInterface;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class HTTPManager {

    private static HTTPManager httpManager;

    public static HTTPManager get(){
        if (httpManager == null){
            httpManager = new HTTPManager();
        }
        return httpManager;
    }

    private HTTPManager(){

    }

    private static final String TAG = "HTTPManager";

    //---------------------------------------------------------- REQUESTS ------------------------//
    public void getRequest(String urlPath, String type, OutputHTTPManagerInterface delegate){//-----возвращает объект
        HttpURLConnection urlConnection = createUrlConnection(urlPath);

        settingResponseGeneral(urlConnection);
        settingResponseGet(urlConnection);

        prepareResponce(urlConnection, type, delegate);
    }

    //---------------------------------------------------------------------------POST-------------//
    public void postRequest (String urlPath, String object, String type, OutputHTTPManagerInterface delegate) throws IOException {// -----------post запрос
        HttpURLConnection urlConnection = createUrlConnection(urlPath);

        settingResponseGeneral(urlConnection);
        settingResponsePost(urlConnection);

        setBodyRequest(object, urlConnection);
        prepareResponce(urlConnection, type, delegate);

    }

//------------------------------------------ SUPPORT FUNCTION FOR REQUESTS------------------------//
    private HttpURLConnection createUrlConnection(String urlPath) {//-------------------------------создание HttpURLConnection
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(urlPath);
            //TODO: возможно нужно вынести коннект
            urlConnection = (HttpURLConnection) url.openConnection();
        } catch (IOException error){
            Log.e(TAG,  error.getMessage());
        }
        return urlConnection;
    }



    public ByteArrayOutputStream readDataFromRequestStream (InputStream inputStream) throws IOException {//получение информации с сервера
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int bytesRead = 0;
        byte[] buffer = new byte[1024];
        while ((bytesRead = inputStream.read(buffer)) > 0) {//--------------------------------------программа многократно вызывает read, пока в подключении не кончатся данные
            out.write(buffer, 0, bytesRead);
        }
        out.close();
        return out;
    }

    private boolean isUrlConnect (HttpURLConnection urlConnection){//-------------------------------проверка ответа сервера
        boolean result = false;
        try{
            if (urlConnection.getResponseCode() == 200){
                result = true;
            } else {
                String message = urlConnection.getResponseMessage();
                int responseCode = urlConnection.getResponseCode();
                Log.i("RESPONSE ERROR", "URL - " + urlConnection.getURL() +
                        "\nERROR CODE - " + responseCode + " RESPONSE MESSAGE : " + message);
            }
        } catch (Exception error){
            Log.e(TAG,  error.getMessage());
        }
        return result;
    }

    private void closeUrlConnect(HttpURLConnection urlConnection){//--------------------------------закрытие URLConnection
        if (urlConnection != null){
            urlConnection.disconnect();
        }
    }

    //---------------------------------------RESPONCE------------------------------------------------//

    private void prepareResponce(HttpURLConnection httpURLConnection, String type, OutputHTTPManagerInterface delegate){
        byte [] byteArray = null;
        Exception error = null;
        if (isUrlConnect(httpURLConnection)) {
            try {
                byteArray = readDataFromRequestStream(httpURLConnection.getInputStream()).toByteArray();
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
                error = e;
            }
        }
        //TODO: возможно нужно вынести
        closeUrlConnect(httpURLConnection);
        sendDelegate(byteArray, error, type, delegate);
    }


    public void sendDelegate(byte [] byteArray,Exception error, String type, OutputHTTPManagerInterface delegate){
        if (error != null){
            delegate.error(error);
        }
        delegate.response(byteArray, type);
    }

    //---------------------------------------SETTING------------------------------------------------//

    private void settingResponseGeneral(HttpURLConnection httpURLConnection){
        httpURLConnection.setUseCaches(false);//если true, то соединению разоешается использовать любой доступный кэш. Если false, кэши должны игнорироваться. По умолчанию стоит true
        //httpURLConnection.setRequestProperty("Authorization", "Auth:test");
    }

    private void settingResponseGet(HttpURLConnection httpURLConnection){
        //httpURLConnection.setRequestProperty();
    }

    private void settingResponsePost(HttpURLConnection httpURLConnection){
        //TODO: оставить только что  необходимо
        httpURLConnection.setDoOutput(true);//true если надо использовать соединеие URL для вывода, и false, если нет. По умолчанию false
        httpURLConnection.setChunkedStreamingMode(0);//если неизвестна длина тела вызывается метод setChunkedStreamingMode(0), противном случае HTTPUrlConnection будет вынужден буферизовать полное тело увеличивая задержку
        httpURLConnection.setRequestProperty("Content-Type", "application/json");//устанавливает общее свойство запроса <Ключ(String) - ключевое слово, по которому известен запрос, значение(String) - значение, связанное с ключом>


        //httpURLConnection.setDoInput(true);//true если надо соединение для ввода, и false, если нет. По умолчанию true
    }

    public void setBodyRequest(String object, HttpURLConnection httpURLConnection) throws IOException {
        DataOutputStream out = new DataOutputStream(httpURLConnection.getOutputStream());
        out.writeBytes(object);
        out.flush();
        out.close();
    }

}
