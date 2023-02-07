package com.api.resources;

import com.api.annotations.UserScope;
import com.api.entities.Comment;
import com.api.exceptions.RequestException;
import com.api.exceptions.ResourceFoundException;
import com.api.services.CommentService;
import com.api.utils.ResponseUtil;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping("comments")
public class CommentResource {

    private final CommentService commentService;

    public CommentResource(CommentService commentService){
        this.commentService = commentService;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @UserScope
    public ResponseEntity<?> addComment(@RequestBody Comment comment) throws Exception {
        try {
            return ResponseUtil.render(commentService.addComment(comment));
        } catch (RequestException e) {
            return ResponseUtil.renderException(e);
        }
    }

    @GetMapping("post/{id}")
    public ResponseEntity<?> getPostComments(@PathVariable int id,
                                             @RequestParam(required = false, defaultValue = "0") int offset,
                                             @RequestParam(required = false, defaultValue = "5") int limit) throws SQLException {
        try {
            return ResponseUtil.render(this.commentService.findComments(id, offset, limit));
        } catch (RequestException e) {
            return ResponseUtil.renderException(e);
        }
    }

}
