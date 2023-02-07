package com.api.entities;

import com.api.entities.post.Post;

import java.sql.Date;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class Comment extends Entity {

    private int idPost;
    private int idParentComment;
    private User user;
    private String comment;

    private List<Comment> commentRepliesList;
    private Date publishedDate;

    public Comment(){
        super(0);
    }

    public Comment(int id, int idPost, int idParentComment, User user, String comment, List<Comment> commentRepliesList, Date publishedDate) {
        super(id);
        this.idPost = idPost;
        this.user = user;
        this.comment = comment;
        this.commentRepliesList = commentRepliesList;
        this.publishedDate = publishedDate;
        this.idParentComment = idParentComment;
    }

    public int getIdPost() {
        return idPost;
    }

    public void setIdPost(int idPost) {
        this.idPost = idPost;
    }

    public int getIdParentComment() {
        return idParentComment;
    }

    public void setIdParentComment(int idParentComment) {
        this.idParentComment = idParentComment;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Date getPublishedDate() {
        return publishedDate;
    }

    public void setPublishedDate(Date publishedDate) {
        this.publishedDate = publishedDate;
    }

    public List<Comment> getCommentRepliesList() {
        return commentRepliesList;
    }

    public void setCommentRepliesList(List<Comment> commentRepliesList) {
        this.commentRepliesList = commentRepliesList;
    }
}
