package com.api.entities;

import java.sql.ResultSet;

public class Tag extends Entity{
    private String name;

    public Tag(){
        super(0);
    }
    public Tag(int id, String name) {
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
