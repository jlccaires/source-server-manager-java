package com.jlccaires.sourcemanager.domain;

import java.io.Serializable;

/**
 * @author jlccaires - Julio Cesar Caires | 08/11/2016 - 13:01
 */
public class ServerDetails implements Serializable {
    public int port;
    public String rconPassword;

    public ServerDetails(final int port, final String rconPassword) {
        this.port = port;
        this.rconPassword = rconPassword;
    }
}
