package com.api.dao;

import com.api.entities.Entity;
import com.api.entities.Tag;
import com.api.entities.post.Post;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.ApplicationScope;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@ApplicationScope
@Component
public class TagDAO implements DAO<Tag> {

    private DataSource dataSource;

    public TagDAO(DataSource dataSource){
        this.dataSource = dataSource;
    }
    @Override
    public List<Tag> findAll() throws SQLException {
        List<Tag> tags = new ArrayList<>();
        try(Connection connection = dataSource.getConnection(); ResultSet resultSet = connection.createStatement().executeQuery("select * from tag")){
            while(resultSet.next()) tags.add(readEntity(resultSet));
        }
        return tags;
    }

    @Override
    public Tag find(int id) throws SQLException {
        Tag tag = null;
        try(Connection connection = dataSource.getConnection();
            ResultSet resultSet = connection.createStatement().executeQuery("select * from tag where id = " + id)){
            if(resultSet.next()) tag = readEntity(resultSet);
        }
        return tag;
    }

    public Tag findByTag(String tagName) throws SQLException {
        Tag tag = null;
        try(Connection connection = dataSource.getConnection(); PreparedStatement statement = connection.prepareStatement("select * from tag where name = ?")){
            statement.setString(1, tagName);
            ResultSet resultSet = statement.executeQuery();
            if(resultSet.next()) tag = readEntity(resultSet);
        }
        return tag;
    }

    public List<Integer> findPostTags(int idPost) throws SQLException {
        List<Integer> tagsId = new ArrayList<>();
        try(Connection connection = dataSource.getConnection();
            ResultSet resultSet = connection.createStatement().executeQuery("select * from post_tag where id_post = " + idPost)){
            while (resultSet.next()) tagsId.add(resultSet.getInt("id_tag"));
        }
        return tagsId;
    }

    public void addPostTag(int idPost, Tag tag) throws SQLException {
        try(Connection connection = dataSource.getConnection()){
            connection.createStatement().execute("insert into post_tag (id_post, id_tag) values ("+ idPost +","+ tag.getId() +")");
        }
    }


    @Override
    public Tag save(Tag entity) throws SQLException {
        String query = "insert into tag (name) values (?)";
        try(Connection connection = dataSource.getConnection(); PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)){
            statement.setString(1, entity.getName());
            statement.execute();
            ResultSet resultSet = statement.getGeneratedKeys();
            resultSet.next();
            entity.setId(resultSet.getInt(1));
        }
        return entity;
    }

    public List<Tag> getPopularTags() throws SQLException {
        List<Tag> tags = new ArrayList<>();
        try(Connection connection = dataSource.getConnection(); ResultSet resultSet = connection.createStatement().executeQuery("select id, name from post_tag join tag on id = id_tag group by id_tag order by count(id_post) desc")){
            while (resultSet.next())
                tags.add(readEntity(resultSet));
        }
        return tags;
    }

    @Override
    public boolean delete(Entity entity) throws SQLException {
        boolean state;
        try(Connection connection = dataSource.getConnection(); Statement statement = connection.createStatement()){
            state = statement.execute("delete * from tag where id = " + entity.getId());
        }
        return state;
    }

    public List<Tag> searchTag(String term, int limit) throws SQLException {
        String query = "select * from tag where name like ? limit " + limit;
        List<Tag> tags = new ArrayList<>();
        try(Connection connection = dataSource.getConnection(); PreparedStatement statement = connection.prepareStatement(query)){
            statement.setString(1, "%" + term + "%");
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) tags.add(readEntity(resultSet));
        }

        return tags;
    }

    @Override
    public Tag readEntity(ResultSet resultSet) throws SQLException {
        return new Tag(resultSet.getInt("id"), resultSet.getString("name"));
    }
}
