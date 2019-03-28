package com.dreamteam.dreamteam.User.Protocols;

import com.dreamteam.dreamteam.DataStore.HTTP.OutputHTTPManagerInterface;

public interface UserFromHTTPManagerInterface extends OutputHTTPManagerInterface {

    void response(byte[] byteArray, String type);

    void error(Throwable t);
}
