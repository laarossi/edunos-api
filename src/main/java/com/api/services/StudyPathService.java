package com.api.services;

import com.api.dao.StudyPathDAO;
import com.api.entities.PaginationWrapper;
import com.api.entities.StudyPath;
import com.api.entities.post.Post;
import com.api.exceptions.BadRequestException;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.ArrayList;

@Service
public class StudyPathService {

    private final StudyPathDAO studyPathDAO;
    private final PostService postService;

    public StudyPathService(StudyPathDAO studyPathDAO, PostService postService){
        this.studyPathDAO = studyPathDAO;
        this.postService = postService;
    }

    public StudyPath find(int id) throws SQLException {
        return this.studyPathDAO.find(id);
    }

    public PaginationWrapper<?> findAll(int offset, int limit) throws Exception {
        int count = this.studyPathDAO.countAll();
        if(count == 0 || offset >= count) return new PaginationWrapper<StudyPath>(count, count, 0, new ArrayList<>());
        return new PaginationWrapper<>(count, count, 0, this.studyPathDAO.findAll(offset, limit));
    }

    public StudyPath addStudyPath(StudyPath studyPath) throws Exception {
        studyPath = this.studyPathDAO.save(studyPath);
        System.out.println(studyPath.getPosts());
        for(Post post : studyPath.getPosts()){
            if(postService.find(post.getId()) == null){
                this.studyPathDAO.delete(studyPath);
                throw new BadRequestException("Entity[Post] with id " + post.getId() + " not found");
            }
        }
        return studyPath;
    }

}
