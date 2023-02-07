package com.api.dao;

import com.api.entities.Entity;
import com.api.entities.Session;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.ApplicationScope;
import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Component
@ApplicationScope
public class SessionDAO implements DAO<Session> {

    private final DataSource dataSource;

    public SessionDAO(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public List<Session> findAll() throws SQLException {
        List<Session> sessions = new ArrayList<>();
        try(Connection connection = this.dataSource.getConnection();
            ResultSet resultSet = connection.createStatement().executeQuery("select * from session")){
            while (resultSet.next())
                sessions.add(readEntity(resultSet));
        }
        return sessions;
    }

    @Override
    public Session find(int id) throws SQLException {
        Session session = null;
        try(Connection connection = this.dataSource.getConnection();
            ResultSet resultSet = connection.createStatement().executeQuery("select * from session where id = " + id)){
            while (resultSet.next())
                session = readEntity(resultSet);
        }
        return session;
    }

    @Override
    public Session save(Session entity) throws SQLException {
        String query = "insert into session (id_user, session_key) VALUES (?,?)";
        try(Connection connection = dataSource.getConnection(); PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)){
            statement.setInt(1, entity.getIdUser());
            statement.setString(2, entity.getSessionKey());
            statement.execute();
        }
        return entity;
    }

    public Session findByKey(String sessionKey) throws SQLException {
        Session session = null;
        try(Connection connection = dataSource.getConnection(); PreparedStatement statement = connection.prepareStatement("select * from session where session_key = ?")){
            statement.setString(1, sessionKey);
            ResultSet resultSet = statement.executeQuery();
            if(resultSet.next()) session = readEntity(resultSet);

        }
        return session;
    }

    @Override
    public boolean delete(Entity entity) throws SQLException {
        boolean state;
        try(Connection connection = dataSource.getConnection();){
            state = connection.createStatement().execute("delete from session where id = " + entity.getId());
        }
        return state;
    }

    public boolean delete(int userId) throws SQLException {
        boolean state;
        try(Connection connection = dataSource.getConnection()){
            state = connection.createStatement().execute("delete from session where id_user = " + userId);
        }
        return state;
    }

    @Override
    public Session readEntity(ResultSet resultSet) throws SQLException {
        return new Session(resultSet.getInt("id_user"), resultSet.getString("session_key"));
    }

}
