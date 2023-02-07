package com.api.dao;

import com.api.entities.Entity;
import com.api.entities.Subject;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.ApplicationScope;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Component
@ApplicationScope
public class SubjectDAO implements DAO<Subject> {

    private final DataSource dataSource;

    public SubjectDAO(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public List<Subject> findAll() throws SQLException {
        List<Subject> subjects = new ArrayList<>();
        String query = "select * from subject";
        try(Connection connection = dataSource.getConnection(); ResultSet resultSet = connection.createStatement().executeQuery(query)){
            while(resultSet.next())
                subjects.add(readEntity(resultSet));
        }
        return subjects;
    }

    @Override
    public Subject find (int id) throws SQLException {
        Subject subject = null;
        String query = "select * from subject where id = " + id;
        try(Connection connection = dataSource.getConnection(); ResultSet resultSet = connection.createStatement().executeQuery(query)){
            if(resultSet.next()) subject = readEntity(resultSet);
        }
        return subject;
    }

    public Subject findByName (String subjectName) throws SQLException {
        Subject subject = null;
        String query = "select * from subject where name = ?";
        try(Connection connection = dataSource.getConnection(); PreparedStatement statement = connection.prepareStatement(query)){
            statement.setString(1, subjectName);
            statement.execute();
            ResultSet resultSet = statement.executeQuery();
            if(resultSet.next()) subject = readEntity(resultSet);
        }
        return subject;
    }

    @Override
    public Subject save(Subject entity) throws SQLException {
        String query = "insert into subject (name) values (?)";
        try(Connection connection = dataSource.getConnection(); PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)){
            statement.setString(1, entity.getName());
            statement.execute();
            ResultSet resultSet = statement.getGeneratedKeys();
            resultSet.next();
            entity.setId(resultSet.getInt(1));
        }
        return entity;
    }

    @Override
    public boolean delete(Entity entity) throws SQLException {
        boolean state;
        try(Connection connection = dataSource.getConnection();){
            state = connection.createStatement().execute("delete * from subject where id = " + entity.getId());
        }
        return state;
    }

    @Override
    public Subject readEntity(ResultSet resultSet) throws SQLException {
        return new Subject(resultSet.getInt("id"), resultSet.getString("name"));
    }
}
