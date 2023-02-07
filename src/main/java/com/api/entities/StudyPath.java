package com.api.entities;

import com.api.entities.post.Post;

import java.util.List;

public class StudyPath extends Entity{

    private String title;
    private String description;
    private List<Post> posts;

    public StudyPath(){
        super(0);
    }

    public StudyPath(int id, String title, String description, List<Post> posts){
        super(id);
        this.title = title;
        this.description = description;
        this.posts = posts;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Post> getPosts() {
        return posts;
    }

    public void setPosts(List<Post> posts) {
        this.posts = posts;
    }
}
