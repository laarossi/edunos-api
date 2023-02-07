package com.api.entities;

public class Subject extends Entity{

    private String name;

    public Subject(){
        super(0);
    }
    public Subject(int id, String name) {
        super(id);
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
