package com.api.dao;

import com.api.entities.Entity;
import com.api.entities.StudyPath;
import com.api.entities.post.Post;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.ApplicationScope;

import javax.sound.midi.Track;
import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Component
@ApplicationScope
public class StudyPathDAO implements DAO<StudyPath>{

    private final DataSource dataSource;
    private final PostDAO postDAO;

    public StudyPathDAO(DataSource dataSource, PostDAO postDAO){
        this.dataSource = dataSource;
        this.postDAO = postDAO;
    }

    @Override
    public List<StudyPath> findAll() throws Exception {
        List<StudyPath> studyPaths = new ArrayList<>();
        try(Connection connection = dataSource.getConnection(); ResultSet resultSet = connection.createStatement().executeQuery("select * from study_path")){
            while (resultSet.next()) studyPaths.add(readEntity(resultSet));
        }
        return studyPaths;
    }

    public List<StudyPath> findAll(int offset, int limit) throws Exception {
        List<StudyPath> studyPaths = new ArrayList<>();
        try(Connection connection = dataSource.getConnection();
            ResultSet resultSet = connection.createStatement().executeQuery("select * from study_path limit " + limit + " offset " + offset)){
            while (resultSet.next()) studyPaths.add(readEntity(resultSet));
        }
        return studyPaths;
    }

    @Override
    public StudyPath find(int id) throws SQLException {
        try(Connection connection = dataSource.getConnection();
            ResultSet resultSet = connection.createStatement().executeQuery("select * from study_path where id " + id)){
            if(resultSet.next()) return readEntity(resultSet);
        }
        return null;
    }

    public List<Post> findStudyPathCourses(int studyPathId) throws SQLException {
        List<Post> posts = new ArrayList<>();
        try(Connection connection = dataSource.getConnection(); ResultSet resultSet = connection.createStatement()
                    .executeQuery("select * from study_path_courses where id_study_path=" + studyPathId)){
            while (resultSet.next()) posts.add(postDAO.find(resultSet.getInt("id_post")));
        }
        return posts;
    }

    @Override
    public StudyPath save(StudyPath entity) throws Exception {
        String query = "insert into study_path (title, description) values (?,?)";
        try(Connection connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)){
            statement.setString(1, entity.getTitle());
            statement.setString(2, entity.getDescription());
            statement.execute();
            ResultSet resultSet = statement.getGeneratedKeys();
            if(!resultSet.next()) return null;
            entity.setId(resultSet.getInt(1));
        }
        return entity;
    }

    public void addStudyPathCourses(int idStudyPath, int idPost, int order) throws SQLException {
        try(Connection connection = dataSource.getConnection();){
            connection.createStatement().
                    execute("insert into study_path_courses (id_study_path, id_post, `order`) values ("+idStudyPath+","+idPost+","+order+")");
        }
    }

    @Override
    public boolean delete(Entity entity) throws SQLException {
        boolean state;
        try(Connection connection = dataSource.getConnection();){
            state = connection.createStatement().execute("delete from study_path where id = " + entity.getId());
        }
        return state;
    }

    @Override
    public StudyPath readEntity(ResultSet resultSet) throws SQLException {
        return new StudyPath(
                resultSet.getInt("id"),
                resultSet.getString("title"),
                resultSet.getString("description"),
                findStudyPathCourses(resultSet.getInt("id"))
        );
    }

    public int countAll() throws SQLException {
        try(Connection connection = dataSource.getConnection(); ResultSet resultSet = connection.createStatement().executeQuery("select count(*) from study_path")){
            if(resultSet.next()) return resultSet.getInt(1);
        }
        return -1;
    }
}
