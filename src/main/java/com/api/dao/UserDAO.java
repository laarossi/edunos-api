package com.api.dao;

import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.api.entities.Entity;
import com.api.entities.User;
import com.api.exceptions.EmailUsedException;
import com.api.exceptions.WrongCredentialsException;
import com.api.services.StorageService;
import com.api.utils.SecurityUtil;
import org.apache.catalina.Store;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

import javax.sql.DataSource;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Component
@RequestScope
public class UserDAO implements DAO<User> {

    private final DataSource dataSource;
    private final StorageService storageService;

    public UserDAO(DataSource dataSource, StorageService storageService) {
        this.dataSource = dataSource;
        this.storageService = storageService;
    }

    @Override
    public List<User> findAll() throws SQLException {
        List<User> users = new ArrayList<>();
        try(Connection connection = this.dataSource.getConnection(); ResultSet resultSet = connection.createStatement().executeQuery("select * from user")){
            while (resultSet.next()){
                users.add(readEntity(resultSet));
            }
        }
        return users;
    }

    @Override
    public User find(int id) throws SQLException {
        User user;
        try(Connection connection = this.dataSource.getConnection();
            ResultSet resultSet = connection.createStatement().executeQuery("select * from user where id = " + id)){
            if(!resultSet.next()){
                connection.close();
                return null;
            }
            user = readEntity(resultSet);
        }
        return user;
    }

    public User findByCredentials(String email, String password) throws SQLException, WrongCredentialsException {
        if(!isEmailExists(email)) throw new WrongCredentialsException();
        String query = "select * from user where email = ? and password = ?";
        User user;
        try(Connection connection = dataSource.getConnection(); PreparedStatement statement = connection.prepareStatement(query)){
            statement.setString(1, email);
            statement.setString(2, password);
            ResultSet resultSet = statement.executeQuery();
            if(!resultSet.next()) return null;
            user = readEntity(resultSet);
        }
        return user;
    }

    public User findByEmail(String email) throws SQLException, WrongCredentialsException {
        if(!isEmailExists(email)) throw new WrongCredentialsException();
        String query = "select * from user where email = ?";
        User user;
        try(Connection connection = dataSource.getConnection(); PreparedStatement statement = connection.prepareStatement(query)){
            statement.setString(1, email);
            ResultSet resultSet = statement.executeQuery();
            if(!resultSet.next()) return null;
            user = readEntity(resultSet);
        }
        return user;
    }

    public boolean isEmailExists(String email) throws SQLException {
        String query = "select * from user where email = ?";
        boolean state;
        try(Connection connection = dataSource.getConnection(); PreparedStatement statement = connection.prepareStatement(query)){
            statement.setString(1, email);
            ResultSet resultSet = statement.executeQuery();
            state = resultSet.next();
        }
        return state;
    }

    public boolean isUsernameExists(String username) throws SQLException {
        String query = "select * from user where username = ?";
        boolean state;
        try(Connection connection = dataSource.getConnection(); PreparedStatement statement = connection.prepareStatement(query)){
            statement.setString(1, username);
            ResultSet resultSet = statement.executeQuery();
            state = resultSet.next();
        }
        return state;
    }

    @Override
    public User save(User entity) throws SQLException, EmailUsedException, NoSuchAlgorithmException {
        String query = "insert into user (username, email, location, facebook, twitter, fullname, github, birth_date, registration_date, password) values (?,?,?,?,?,?,?,?,?,?)";
        try(Connection connection = this.dataSource.getConnection(); PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)){
            statement.setString(1, entity.getUsername());
            statement.setString(2, entity.getEmail());
            statement.setString(3, entity.getLocation());
            statement.setString(4, entity.getFacebook());
            statement.setString(5, entity.getTwitter());
            statement.setString(6, entity.getFullName());
            statement.setString(7, entity.getGithub());
            statement.setDate(8, entity.getBirthDate());
            statement.setDate(9, entity.getRegistrationDate());
            statement.setString(10, SecurityUtil.hash(entity.getPassword()));
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
        try(Connection connection = this.dataSource.getConnection();){
            state = connection.createStatement().execute("delete from user where id = " + entity.getId());
        }
        return state;
    }

    public void updateColumn(int userId, String column, String value) throws SQLException {
        String query = "update user set " + column + " = ? where id = " + userId;
        try(Connection connection = dataSource.getConnection(); PreparedStatement statement = connection.prepareStatement(query)){
            statement.setString(1, value);
            statement.execute();
        }
    }

    @Override
    public User readEntity(ResultSet resultSet) throws SQLException {
        return new User(resultSet.getInt("id"),
                false,
                resultSet.getString("username"),
                resultSet.getString("email"),
                resultSet.getString("password"),
                resultSet.getString("fullname"),
                resultSet.getString("location"),
                resultSet.getDate("birth_date"),
                resultSet.getString("facebook"),
                resultSet.getString("twitter"),
                resultSet.getString("github"),
                this.storageService.getPath("e-learning-main-bucket", Region.getRegion(Regions.US_EAST_2), resultSet.getString("picture")),
                resultSet.getDate("registration_date"));
    }
}
