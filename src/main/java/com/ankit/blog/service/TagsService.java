package com.ankit.blog.service;

import com.ankit.blog.entity.Tags;
import com.ankit.blog.repository.TagsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TagsService {

    @Autowired
    private TagsRepository tagsRepository;

    public List<Tags> getAllTags() {
        return tagsRepository.findAll();
    }
}
