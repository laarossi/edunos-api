package com.api.services;

import com.api.dao.CommentDAO;
import com.api.dao.PostDAO;
import com.api.entities.Comment;
import com.api.entities.PaginationWrapper;
import com.api.exceptions.BadRequestException;
import com.api.exceptions.ResourceFoundException;
import org.springframework.stereotype.Service;
import java.sql.SQLException;
import java.util.ArrayList;

@Service
public class CommentService {

    private final CommentDAO commentDAO;
    private final PostDAO postDAO;
    private final AuthenticationService authenticationService;

    public CommentService(CommentDAO commentDAO, PostDAO postDAO, AuthenticationService authenticationService){
        this.commentDAO = commentDAO;
        this.postDAO = postDAO;
        this.authenticationService = authenticationService;
    }

    public Comment addComment(Comment comment) throws Exception {
        if(comment.getComment() == null || comment.getComment().isEmpty())
            throw new BadRequestException();
        if(postDAO.find(comment.getIdPost()) == null) throw new ResourceFoundException("Entity[Post] with @" + comment.getIdPost() + " not found");
        comment.setUser(authenticationService.getUser());
        comment = commentDAO.save(comment);
        return comment;
    }

    public PaginationWrapper<?> findComments(int idPost, int offset, int limit) throws SQLException, ResourceFoundException {
        if(postDAO.find(idPost) == null) throw new ResourceFoundException("Entity[Post] with @" + idPost + " not found");
        int count = commentDAO.getCommentsCount(idPost);
        if(count == 0 || count <= offset) return new PaginationWrapper<>(count, count, 0, new ArrayList<>());
        return new PaginationWrapper<>(count, offset + limit, limit, commentDAO.findPostComments(idPost, offset, limit));
    }

}
