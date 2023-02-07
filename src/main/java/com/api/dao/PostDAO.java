package com.api.dao;

import com.api.entities.Entity;
import com.api.entities.Tag;
import com.api.entities.post.Post;
import com.api.services.TagService;
import org.checkerframework.checker.units.qual.C;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.ApplicationScope;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Stack;

@Component
@ApplicationScope
public class PostDAO implements DAO<Post> {

    private final DataSource dataSource;
    private final UserDAO userDAO;
    private final SubjectDAO subjectDAO;
    private final TagService tagService;

    public PostDAO(DataSource dataSource, UserDAO userDAO, SubjectDAO subjectDAO, TagService tagService) {
        this.dataSource = dataSource;
        this.userDAO = userDAO;
        this.subjectDAO = subjectDAO;
        this.tagService = tagService;
    }

    @Override
    public List<Post> findAll() throws SQLException {
        return findAll(-1, -1);
    }

    public List<Post> findAll(int offset, int limit) throws SQLException {
        List<Post> posts = new ArrayList<>();
        String query;
        if (offset == -1 || limit == -1) query = "select * from post";
        else query = "select * from post offset " + offset + " limit " + limit;
        try (Connection connection = dataSource.getConnection();
             ResultSet resultSet = connection.createStatement().executeQuery(query)) {
            while (resultSet.next()) posts.add(readEntity(resultSet));
        }
        return posts;
    }

    @Override
    public Post find(int id) throws SQLException {
        Post post = null;
        try (Connection connection = dataSource.getConnection();
             ResultSet resultSet = connection.createStatement().executeQuery("select * from post where id = " + id)) {
            if (resultSet.next()) post = readEntity(resultSet);
        }
        return post;
    }

    public List<Post> findUserFavorites(int idUser) throws SQLException {
        String query = "select * from likes l join post p on p.id = l.id_post where l.id_user = " + idUser;
        List<Post> posts = new ArrayList<>();
        try (Connection connection = dataSource.getConnection(); ResultSet resultSet = connection.createStatement().executeQuery(query)) {
            while (resultSet.next()) posts.add(readEntity(resultSet));
        }
        return posts;
    }

    public boolean isLiked(int idPost, int idUser) throws SQLException {
        boolean isLiked;
        try(Connection connection = dataSource.getConnection();){
            ResultSet resultSet = connection.createStatement().executeQuery("select * from likes where id_post = " + idPost + " and id_user = " + idUser);
            isLiked = resultSet.next();
        }
        return isLiked;
    }

    public List<Post> searchByTitle(String title) throws SQLException {
        String query = "select * from post where title like ?";
        List<Post> posts = new ArrayList<>();
        try(Connection connection = dataSource.getConnection(); PreparedStatement statement = connection.prepareStatement(query)){
            statement.setString(1, "%" + title + "%");
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) posts.add(readEntity(resultSet));
        }
        return posts;
    }
    public List<Post> searchByTitle(String title, int offset, int limit) throws SQLException {
        List<Post> posts = new ArrayList<>();
        String query = "select * from post where title like ? or title like ? or title like ? limit " + limit + " offset " + offset;
        try(Connection connection = dataSource.getConnection(); PreparedStatement statement = connection.prepareStatement(query)){
            statement.setString(1, "%"+title+"%");
            statement.setString(2, title+"%");
            statement.setString(3, "%"+title);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) posts.add(readEntity(resultSet));
        }
        return posts;
    }

    public int searchByTitleCount(String title) throws SQLException {
        String query = "select count(*) from post where title like ?";
        try(Connection connection = dataSource.getConnection(); PreparedStatement statement = connection.prepareStatement(query)){
            statement.setString(1, "%" + title + "%");
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) return resultSet.getInt(1);
        }
        return -1;
    }


    public void addLike(int idPost, int idUser) throws SQLException {
        try(Connection connection = dataSource.getConnection()){
            Statement statement = connection.createStatement();
            statement.execute("insert into likes (id_post, id_user) value ("+idPost+","+idUser+")");
        }
    }

    public void deleteLike(int idPost, int idUser) throws SQLException {
        try(Connection connection = dataSource.getConnection()){
            Statement statement = connection.createStatement();
            statement.execute("delete from likes where id_user = " + idUser + " and id_post = " + idPost);
        }
    }

    public long count() throws SQLException {
        long count = 0;
        try (Connection connection = dataSource.getConnection(); ResultSet resultSet = connection.createStatement().executeQuery("select count(*) as nb from post")) {
            if (!resultSet.next()) return -1;
            count = resultSet.getLong("nb");
        }
        return count;
    }

    @Override
    public Post save(Post entity) throws SQLException {
        String query = "insert into post (id_user, id_subject, title, publish_date, data) values (?,?,?,now(),?)";
        try (Connection connection = dataSource.getConnection(); PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            statement.setInt(1, entity.getUser().getId());
            statement.setInt(2, entity.getSubject().getId());
            statement.setString(3, entity.getTitle());
            statement.setString(4, entity.getData());
            statement.execute();
            ResultSet genKeys = statement.getGeneratedKeys();
            if (genKeys.next()) entity.setId(genKeys.getInt(1));
        }
        return entity;
    }

    @Override
    public boolean delete(Entity entity) throws SQLException {
        boolean state;
        try (Connection connection = dataSource.getConnection(); Statement statement = connection.createStatement()) {
            state = statement.execute("delete from post where  id = " + entity.getId());
        }
        return state;
    }


    public boolean findLike(int idPost, int idUser) throws SQLException {
        boolean found;
        try(Connection connection = dataSource.getConnection();){
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("select * from likes where id_user = " + idUser + " and id_post = " + idPost);
            found = resultSet.next();
        }
        return found;
    }

    public List<Post> findPostsWithTag(int idTag, int limit, int offset) throws SQLException {
        List<Post> posts = new ArrayList<>();
        String query = "select * from post_tag join post on id = id_post where id_tag = " + idTag + " limit " + limit + " offset " + offset;
        try(Connection connection = dataSource.getConnection(); ResultSet resultSet = connection.createStatement().executeQuery(query)){
            while (resultSet.next()) posts.add(readEntity(resultSet));
        }
        return posts;
    }

    public int getPostsWithTagCount(int idTag) throws SQLException {
        List<Post> posts = new ArrayList<>();
        String query = "select count(*) as nb from post_tag join post on id = id_post where id_tag = " + idTag;
        try(Connection connection = dataSource.getConnection(); ResultSet resultSet = connection.createStatement().executeQuery(query)){
            if(resultSet.next())
                return resultSet.getInt("nb");
        }
        return -1;
    }

    public List<Post> findSimilarPostsWithTags(List<Tag> tags, int offset, int limit) throws SQLException {
        List<Post> posts = new ArrayList<>();
        try(Connection connection = dataSource.getConnection()){
            PreparedStatement preparedStatement;
            for(Tag tag : tags){
                preparedStatement = connection.prepareStatement("" +
                        "select post.* from post_tag pt inner join tag t on pt.id_tag = t.id join post on post.id = pt.id_post " +
                        "where pt.id_tag = " + tag.getId() + " or t.name like ? or t.name like ? or t.name like ? " +
                        "order by rand() limit " + limit + " offset " + offset);
                preparedStatement.setString(1, "%"+tag.getName()+"%");
                preparedStatement.setString(2, tag.getName()+"%");
                preparedStatement.setString(3, "%"+tag.getName());
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next())
                    posts.add(readEntity(resultSet));
            }
        }
        return posts;
    }

    @Override
    public Post readEntity(ResultSet resultSet) throws SQLException {
        return new Post(resultSet.getInt("id"), userDAO.find(resultSet.getInt("id_user")), resultSet.getString("title"),
                resultSet.getString("data"), subjectDAO.find(resultSet.getInt("id_subject")),
                tagService.getPostTags(resultSet.getInt("id")), resultSet.getDate("publish_date"));
    }


}
