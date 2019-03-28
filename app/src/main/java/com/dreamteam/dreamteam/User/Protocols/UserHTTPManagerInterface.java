package com.dreamteam.dreamteam.User.Protocols;

import com.dreamteam.dreamteam.DataStore.HTTP.OutputHTTPManagerInterface;

public interface UserHTTPManagerInterface extends OutputHTTPManagerInterface {

    void response(byte[] byteArray, String type);

    void error(Throwable t);
}
