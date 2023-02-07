package com.api.dto;

public abstract class DTO{

    private int id;

    public DTO(int id){
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}