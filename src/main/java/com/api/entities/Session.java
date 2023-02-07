package com.api.entities;

public class Session extends Entity{

    private int idUser;
    private String sessionKey;


    public Session(int idUser, String sessionKey){
        super(-1);
        this.idUser = idUser;
        this.sessionKey = sessionKey;
    }

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public String getSessionKey() {
        return sessionKey;
    }

    public void setSessionKey(String sessionKey) {
        this.sessionKey = sessionKey;
    }
}
