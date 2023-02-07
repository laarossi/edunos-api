package com.api.resources;

import com.api.annotations.UserScope;
import com.api.entities.post.Post;
import com.api.exceptions.RequestException;
import com.api.services.AuthenticationService;
import com.api.services.PostService;
import com.api.utils.ResponseUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.sql.SQLException;

@RestController
@RequestMapping("posts")
public class PostResource {


    private final PostService postService;

    public PostResource(PostService postService, AuthenticationService authenticationService){
        this.postService = postService;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getPosts() throws Exception {
        return ResponseUtil.render(postService.findAll());
    }

    @GetMapping(value = "/p/", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getPosts(@RequestParam(required = false, defaultValue = "0") int offset, @RequestParam(required = false, defaultValue = "5") int limit) throws Exception {
        return ResponseUtil.render(postService.findAll(limit, offset));
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> addPost(@RequestBody Post post) throws SQLException {
        try {
            return ResponseUtil.render(postService.addPost(post));
        } catch (RequestException e) {
            return ResponseUtil.renderException(e);
        }
    }

    @GetMapping(value = "{id}")
    public ResponseEntity<?> getPost(@PathVariable int id) throws Exception {
        return ResponseUtil.render(postService.find(id));
    }

    @PostMapping("{id}/like")
    @UserScope
    public ResponseEntity<?> likePost(@PathVariable int id) throws SQLException {
        postService.like(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("{id}/suggestions")
    public ResponseEntity<?> getPostSuggestions(@PathVariable int id) throws Exception {
        try{
            return ResponseUtil.render(postService.findSuggestions(id));
        }catch (RequestException e){
            return ResponseUtil.renderException(e);
        }
    }

    @GetMapping("favorites")
    @UserScope
    public ResponseEntity<?> getUserFavorites() throws SQLException {
        return ResponseUtil.render(postService.getUserFavorites());
    }

    @DeleteMapping("{id}")
    public ResponseEntity<?> deletePost(@PathVariable int id) throws SQLException{
        try {
            return ResponseUtil.render(postService.delete(id));
        } catch (RequestException e) {
            return ResponseUtil.renderException(e);
        }
    }

    @GetMapping("search")
    public ResponseEntity<?> search(@RequestParam String term,
                                    @RequestParam(required = false, defaultValue = "0") int offset,
                                    @RequestParam(required = false, defaultValue = "5") int limit) throws SQLException {
        return ResponseUtil.render(postService.searchPosts(term, offset, limit));
    }
}
