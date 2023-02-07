package com.api.dao;

import com.api.entities.Entity;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public interface DAO<T extends Entity>{
    List<T> findAll() throws Exception;
    T find(int id) throws SQLException;
    T save(T entity) throws Exception;
    boolean delete(Entity entity) throws SQLException;
    T readEntity(ResultSet resultSet) throws SQLException;
}
