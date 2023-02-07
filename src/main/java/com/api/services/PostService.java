package com.api.services;

import com.api.dao.PostDAO;
import com.api.dao.SubjectDAO;
import com.api.dao.TagDAO;
import com.api.entities.PaginationWrapper;
import com.api.entities.Subject;
import com.api.entities.Tag;
import com.api.entities.User;
import com.api.entities.post.Post;
import com.api.exceptions.BadRequestException;
import com.api.exceptions.ResourceFoundException;
import org.springframework.stereotype.Service;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class PostService {

    private final PostDAO postDAO;
    private final TagDAO tagDAO;
    private final SubjectDAO subjectDAO;
    private final AuthenticationService authenticationService;

    public PostService(PostDAO postDAO, TagDAO tagDAO, SubjectDAO subjectDAO, AuthenticationService authenticationService){
        this.postDAO = postDAO;
        this.tagDAO = tagDAO;
        this.subjectDAO = subjectDAO;
        this.authenticationService = authenticationService;
    }

    public Post find(int idPost) throws Exception{
        User user = authenticationService.getUser();
        Post post = postDAO.find(idPost);
        if(post != null && user != null)
            post.setLiked(postDAO.isLiked(idPost, user.getId()));
        return post;
    }

    public List<Post> findAll() throws Exception {
        return findAll(-1,-1);
    }

    public List<Post> findAll(int offset, int limit) throws Exception {
        User user = authenticationService.getUser();
        long count = postDAO.count();
        if(count == -1) throw new Exception("Counting error, PostService::findAll offset = " + offset + ", limit = " + limit);
        if(count == 0 || limit == 0 || offset > count) return new ArrayList<>();
        List<Post> posts = postDAO.findAll(offset, limit);
        if(user != null)
            for(Post post : posts)
                post.setLiked(postDAO.isLiked(post.getId(), user.getId()));

        return posts;
    }

    public Post addPost(Post post) throws SQLException, BadRequestException {
        if(post.getSubject() == null || post.getData().isEmpty() || post.getTitle().isEmpty() || post.getUser() == null || post.getTags().isEmpty()) {
            throw new BadRequestException();
        }

        post = postDAO.save(post);
        for(Tag tag : post.getTags()){
            Tag copyTag = tagDAO.findByTag(tag.getName());
            if(copyTag == null){
                tagDAO.save(tag);
                copyTag = tagDAO.findByTag(tag.getName());
            }
            tagDAO.addPostTag(post.getId(), copyTag);
        }

        Subject subject = subjectDAO.findByName(post.getSubject().getName());
        if(subject == null)
            subject = subjectDAO.save(post.getSubject());

        post.setSubject(subject);
        return post;
    }

    public List<Post> findSuggestions(int idPost) throws Exception {
        Post post = postDAO.find(idPost);
        if(post == null)
            throw new ResourceFoundException("Post with id " + idPost + " not found");
        List<Post> recommendationPosts = new ArrayList<>();
        Set<Integer> postsId = new HashSet<>();
        postDAO.searchByTitle(post.getTitle(), 0, 3).forEach(p -> postsId.add(p.getId()));
        findPostsByTags(post.getTags(), 0, 3).forEach(p -> postsId.add(p.getId()));
        for(int id : postsId){
            recommendationPosts.add(find(id));
        }
        return recommendationPosts;
    }

    public List<Post> findPostsByTags(List<Tag> tags, int offset, int limit) throws SQLException {
        List<Post> posts = postDAO.findSimilarPostsWithTags(tags, offset, limit);
        User user = authenticationService.getUser();
        if (user != null) {
            for (Post post : posts) {
                post.setLiked(isLiked(post.getId(), user.getId()));
            }
        }
        return posts;
    }

    public List<Post> getUserFavorites() throws SQLException {
        User user = authenticationService.getUser();
        List<Post> posts = postDAO.findUserFavorites(user.getId());
        for(Post post : posts)
            post.setLiked(isLiked(post.getId(), user.getId()));
        return posts;
    }

    public void like(int idPost) throws SQLException {
        User user = authenticationService.getUser();
        if(isLiked(idPost, user.getId())){
            dislike(idPost);
            return;
        }
        postDAO.addLike(idPost, user.getId());
    }

    public void dislike(int idPost) throws SQLException {
        User user = authenticationService.getUser();
        postDAO.deleteLike(idPost, user.getId());
    }

    public boolean isLiked(int idPost, int idUser) throws SQLException {
        return postDAO.findLike(idPost, idUser);
    }

    public boolean delete(int id) throws SQLException, ResourceFoundException {
        Post post = postDAO.find(id);
        if(post == null)
            throw new ResourceFoundException("Post with id " + id + " not found");
        return postDAO.delete(post);
    }

    public PaginationWrapper<?> searchPosts(String term, int offset, int limit) throws SQLException {
        int count = postDAO.searchByTitleCount(term);
        if (count == 0 || offset >= count) return new PaginationWrapper<>(count, count, 0, new ArrayList<>());
        List<Post> posts = postDAO.searchByTitle(term, offset, limit);
        checkIfPostsAreLiked(posts);
        return new PaginationWrapper<>(count, offset + limit, limit, posts);
    }
    public PaginationWrapper<?> findPostsWithTag(int idTag, int offset, int limit) throws SQLException, ResourceFoundException {
        if (tagDAO.find(idTag) == null) throw new ResourceFoundException("Entity[Tag] with @" + idTag + " not found");
        int count = postDAO.getPostsWithTagCount(idTag);
        if (count == 0 || offset >= count) return new PaginationWrapper<>(count, count, 0, new ArrayList<>());
        List<Post> posts = postDAO.findPostsWithTag(idTag, limit, offset);
        checkIfPostsAreLiked(posts);
        return new PaginationWrapper<>(count, offset + limit, limit, posts);
    }


    public void checkIfPostsAreLiked(List<Post> posts) throws SQLException {
        User user = authenticationService.getUser();
        if (user != null) {
            for (Post post : posts) {
                post.setLiked(isLiked(post.getId(), user.getId()));
            }
        }
    }
}
