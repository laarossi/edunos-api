package com.api.entities.post;

import com.api.entities.Entity;
import com.api.entities.Subject;
import com.api.entities.Tag;
import com.api.entities.User;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.sql.Date;
import java.util.List;

public class Post extends Entity {

    private User user;
    private String title;
    private String data;
    private Subject subject;
    private List<Tag> tags;
    private boolean liked = false;
    private Date publishedDate;

    public Post(){
        super(0);
    }

    public Post(int id, User user, String title, String data, Subject subject, List<Tag> tags, Date publishedDate) {
        super(id);
        this.user = user;
        this.title = title;
        this.data = data;
        this.subject = subject;
        this.tags = tags;
        this.publishedDate = publishedDate;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

    public Date getPublishedDate() {
        return publishedDate;
    }

    public void setPublishedDate(Date publishedDate ) {
        this.publishedDate = publishedDate;
    }

    public boolean isLiked() {
        return liked;
    }

    public void setLiked(boolean liked) {
        this.liked = liked;
    }
}
