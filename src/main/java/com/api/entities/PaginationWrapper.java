package com.api.entities;

import java.util.List;

public class PaginationWrapper<T extends Entity> {

    private int count;

    private int offset;

    private int limit;

    private List<T> entities;

    public PaginationWrapper(int count, int offset, int limit, List<T> entities){
        this.count = count;
        this.offset = offset;
        this.limit = limit;
        this.entities = entities;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public List<T> getEntities() {
        return entities;
    }

    public void setEntities(List<T> entities) {
        this.entities = entities;
    }

}
