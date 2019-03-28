package com.dreamteam.dreamteam.DataStore.HTTP;

public interface OutputHTTPManagerInterface {
    void response(byte[] byteArray, String type);
    void error(Throwable t);
}
