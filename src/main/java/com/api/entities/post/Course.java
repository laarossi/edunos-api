package com.api.entities.post;

import com.api.entities.Subject;
import com.api.entities.Tag;
import com.api.entities.User;

import java.sql.Date;
import java.sql.ResultSet;
import java.util.List;

public class Course extends Post {

    public Course(int id, User user, String title, String data, Subject subject, List<Tag> tags, Date published_date) {
        super(id, user, title, data, subject, tags, published_date);
    }

}
