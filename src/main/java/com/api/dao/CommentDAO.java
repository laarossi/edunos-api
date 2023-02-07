package com.api.dao;

import com.api.entities.Comment;
import com.api.entities.Entity;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.ApplicationScope;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Component
@ApplicationScope
public class CommentDAO implements DAO<Comment> {

    private final DataSource datasource;

    private final UserDAO userDAO;

    public CommentDAO(DataSource datasource, UserDAO userDAO){
        this.datasource = datasource;
        this.userDAO = userDAO;
    }

    @Override
    public List<Comment> findAll() throws Exception {
        return null;
    }

    public List<Comment> findPostComments(int idPost, int offset, int limit) throws SQLException {
        List<Comment> comments = new ArrayList<>();
        String query;
        if(offset == -1 || limit == -1) query = "select * from comments where id_post = " + idPost;
        else query = "select * from comments where id_post = " + idPost + " limit " + limit + " offset " + offset;
        try(Connection connection = datasource.getConnection(); ResultSet resultSet = connection.createStatement().executeQuery(query)){
            while (resultSet.next())
                comments.add(readEntity(resultSet));
        }
        return comments;
    }

    @Override
    public Comment find(int id) throws SQLException {
        Comment comment = null;
        String query = "";
        try(Connection connection = datasource.getConnection(); ResultSet resultSet = connection.createStatement().executeQuery(query)){
            if(resultSet.next())
                comment = readEntity(resultSet);
        }
        return comment;
    }

    @Override
    public Comment save(Comment entity) throws Exception {
        String query = "insert into comments (id_user,id_post,comment,publish_date) values (?,?,?,now())";
        try(Connection connection = datasource.getConnection(); PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)){
            statement.setInt(1, entity.getUser().getId());
            statement.setInt(2, entity.getIdPost());
            statement.setString(3, entity.getComment());
            statement.execute();
            ResultSet resultSet = statement.getGeneratedKeys();
            resultSet.next();
            entity.setId(resultSet.getInt(1));
        }
        return entity;
    }

    public List<Comment> findCommentReplies(int idComment) throws SQLException {
        List<Comment> comments = new ArrayList<>();
        try(Connection connection = datasource.getConnection(); ResultSet resultSet = connection.createStatement().executeQuery("select * from comments where id_parent_comment = " + idComment)){
            while (resultSet.next())
                comments.add(readEntity(resultSet));
        }
        return comments;
    }

    @Override
    public boolean delete(Entity entity) throws SQLException {
        return false;
    }

    public int getCommentsCount(int idPost) throws SQLException {
        int count = -1;
        try(Connection connection = datasource.getConnection(); ResultSet resultSet = connection.createStatement().executeQuery("select count(*) as count_comments from comments where id_post = " + idPost)){
            if(resultSet.next())
                count = resultSet.getInt("count_comments");
        }
        return count;
    }

    @Override
    public Comment readEntity(ResultSet resultSet) throws SQLException {
        return new Comment(resultSet.getInt("id"), resultSet.getInt("id_post"), resultSet.getInt("id_parent_comment"), userDAO.find(resultSet.getInt("id_user")),
                resultSet.getString("comment"), findCommentReplies(resultSet.getInt("id")),
                resultSet.getDate("publish_date"));
    }

}
