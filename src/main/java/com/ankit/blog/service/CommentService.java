package com.ankit.blog.service;

import com.ankit.blog.entity.Comments;
import com.ankit.blog.repository.CommentsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommentService {

    @Autowired
    private CommentsRepository commentsRepository;

    public void saveComment(Comments comment){
        commentsRepository.save(comment);
    }

    public void deleteComment(int id){
        commentsRepository.deleteById(id);
    }

    public Comments getCommentById(int id){
        return commentsRepository.getById(id);
    }
}
