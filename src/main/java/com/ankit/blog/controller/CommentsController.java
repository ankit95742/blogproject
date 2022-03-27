package com.ankit.blog.controller;

import com.ankit.blog.entity.Comments;
import com.ankit.blog.service.CommentService;
import com.ankit.blog.service.PostsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class CommentsController {

    @Autowired
    private PostsService postsService;

    @Autowired
    private CommentService commentService;

    @PostMapping("/newcomment")
    public String addNewComment(@RequestParam Integer postId, @ModelAttribute("commentObject") Comments comment){
        comment.setPostId(postsService.getPostByID(postId));
        commentService.saveComment(comment);
        return "redirect:/post?postId="+postId;
    }

    @GetMapping("/deletecomment")
    public String deleteComment(@RequestParam Integer postId, @RequestParam Integer commentId){
        commentService.deleteComment(commentId);
        return "redirect:/post?postId="+postId;
    }

    @GetMapping("/editcomment")
    public String editComment(@RequestParam Integer postId, @RequestParam Integer commentId, Model model){
        model.addAttribute("commentObject",commentService.getCommentById(commentId));
        model.addAttribute("postId",postId);
        return "editcomment";
    }
}
