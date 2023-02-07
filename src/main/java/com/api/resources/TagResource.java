package com.api.resources;

import com.api.dao.TagDAO;
import com.api.services.PostService;
import com.api.services.TagService;
import com.api.utils.ResponseUtil;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.websocket.server.PathParam;
import java.sql.SQLException;

@RestController
@RequestMapping("tags")
public class TagResource {

    private final TagService tagService;
    private final PostService postService;

    public TagResource(TagService tagService, PostService postService){
        this.tagService = tagService;
        this.postService = postService;
    }

    @GetMapping("{id}")
    public ResponseEntity<?> getTag(@PathVariable int id) throws SQLException {
        return ResponseUtil.render(tagService.getTag(id));
    }

    @GetMapping("/popular")
    public ResponseEntity<?> getPopularTags() throws SQLException {
        return ResponseUtil.render(tagService.getPopularTags());
    }

    @GetMapping(value = "/{id}/posts", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getTagPosts(@PathVariable int id,
                                         @RequestParam(required = false, defaultValue = "0") int offset,
                                         @RequestParam(required = false, defaultValue = "5") int limit) throws Exception {
        return ResponseUtil.render(postService.findPostsWithTag(id, offset, limit));
    }

    @GetMapping("/search")
    public ResponseEntity<?> searchTags(@RequestParam String term, @RequestParam(required = false, defaultValue = "5") int limit) throws SQLException {
        return ResponseUtil.render(tagService.search(term, limit));
    }
}
