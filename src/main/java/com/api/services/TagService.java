package com.api.services;

import com.api.dao.TagDAO;
import com.api.entities.Tag;
import org.springframework.stereotype.Service;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Service
public class TagService {

    private final TagDAO tagDAO;

    public TagService(TagDAO tagDAO){
        this.tagDAO = tagDAO;
    }

    public List<Tag> getPostTags(int idPost) throws SQLException {
        List<Tag> tags = new ArrayList<>();
        for(Integer id : tagDAO.findPostTags(idPost)){
            tags.add(tagDAO.find(id));
        }
        return tags;
    }

    public List<Tag> getPopularTags() throws SQLException {
        return tagDAO.getPopularTags();
    }
    
    public Tag getTag(int id) throws SQLException {
        return tagDAO.find(id);
    }

    public List<Tag> search(String term, int limit) throws SQLException {
        return tagDAO.searchTag(term, limit);
    }
}
