package com.ankit.blog.controller;

import com.ankit.blog.entity.Comments;
import com.ankit.blog.entity.Posts;
import com.ankit.blog.service.PostsService;
import com.ankit.blog.service.TagsService;
import com.ankit.blog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
public class BlogsController {

    @Autowired
    private PostsService postsService;

    @Autowired
    private TagsService tagsService;

    @Autowired
    private UserService userService;

    @GetMapping("/")
    public String getBlogsPage(@RequestParam(required = false, defaultValue = "1") Integer pageNo,
                               @RequestParam(required = false, defaultValue = "2") Integer limit,
                               @RequestParam(value = "search", required = false) String keyword ,
                               @RequestParam(value = "startDate", required = false, defaultValue = "2022-01-01")String startDate,
                               @RequestParam(value = "endDate", required = false, defaultValue = "2050-12-31") String endDate,
                               @RequestParam(value = "author", required = false, defaultValue = "All") String author,
                               @RequestParam(value = "tags", required = false) List<String> tagNames,
                               @RequestParam(value = "sortOnPostPublishedAt", defaultValue = "ASC") String sortOrder,Model model) throws ParseException {

        Pageable paging = PageRequest.of(pageNo - 1, limit, Sort.by("postCreatedAt").ascending());
        switch (sortOrder) {
            case "ASC" -> paging = PageRequest.of(pageNo - 1, limit, Sort.by("postCreatedAt").ascending());
            case "DSC" -> paging = PageRequest.of(pageNo - 1, limit, Sort.by("postCreatedAt").descending());
        }
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Page<Posts> blogs;
        boolean dateAuthorAndTagNames = (!startDate.equals("2022-01-01") & !endDate.equals("2050-12-31") & !author.equals("All") & tagNames != null);
        boolean  dateAndAuthor = (!startDate.equals("2022-01-01") & !endDate.equals("2050-12-31") & !author.equals("All") & tagNames == null);
        boolean dateAndTagNames = (!startDate.equals("2022-01-01") & !endDate.equals("2050-12-31") & author.equals("All") & tagNames != null);
        boolean authorAndTagNames = (startDate.equals("2022-01-01") & endDate.equals("2050-12-31") & !author.equals("All") & tagNames != null);
        boolean dateIsPresent = (!startDate.equals("2022-01-01") & !endDate.equals("2050-12-31"));
        boolean authorIsPresent = (!author.equals("All"));
        boolean tagNamesIsPresent = (tagNames != null);
        if(keyword != null){
            if(dateAuthorAndTagNames){
                blogs = postsService.searchAndFilterPostsByAuthorDateAndTags(format.parse(endDate),format.parse(startDate),author,tagNames,keyword,paging);
            }
            else if(dateAndAuthor){
                blogs = postsService.searchAndFilterPostsByAuthorAndDate(format.parse(endDate),format.parse(startDate),author,keyword,paging);
            }
            else if(dateAndTagNames){
                blogs = postsService.searchAndFilterPostsByDateAndTags(format.parse(endDate),format.parse(startDate),tagNames,keyword,paging);
            }
            else if(authorAndTagNames){
                blogs = postsService.searchAndFilterPostsByAuthorAndTags(author,tagNames,keyword,paging);
            }
            else if(dateIsPresent){
                blogs = postsService.searchAndFilterPostsByDate(format.parse(endDate),format.parse(startDate),keyword,paging);
            }
            else if (authorIsPresent){
                blogs = postsService.searchAndFilterPostsByAuthor(author,keyword,paging);
            }
            else if (tagNamesIsPresent){
                blogs = postsService.searchAndFilterPostsByTags(tagNames,keyword,paging);
            }
            else{
                blogs = postsService.searchPosts(keyword,paging);
            }
        }
        else{
            if(dateAuthorAndTagNames){
                blogs = postsService.filterPostsByAuthorDateAndTags(format.parse(endDate),format.parse(startDate),author,tagNames,paging);
            }
            else if(dateAndAuthor){
                blogs = postsService.filterPostsByDateAndAuthor(format.parse(endDate),format.parse(startDate),author,paging);
            }
            else if(dateAndTagNames){
                blogs = postsService.filterPostsByDateAndTags(format.parse(endDate),format.parse(startDate),tagNames,paging);
            }
            else if(authorAndTagNames){
                blogs = postsService.filterPostsByAuthorAndTags(author,tagNames,paging);
            }
            else if(dateIsPresent){
                blogs = postsService.filterPostsByDate(format.parse(endDate),format.parse(startDate),paging);
            }
            else if (authorIsPresent){
                blogs = postsService.filterPostsByAuthor(author,paging);
            }
            else if (tagNames != null){
                blogs = postsService.filterPostsByTags(tagNames,paging);
            }
            else{
                blogs = postsService.getPosts(paging);
            }
        }
        model.addAttribute("blogs",blogs);
        model.addAttribute("tags",tagsService.getAllTags());
        model.addAttribute("authors",postsService.getAllAuthors());
        model.addAttribute("totalPages",blogs.getTotalPages());
        model.addAttribute("currentPage",pageNo);
        model.addAttribute("pageSize",limit);
        model.addAttribute("sortOnPostPublishedAt",sortOrder);
        model.addAttribute("search",keyword);
        model.addAttribute("startDate",startDate);
        model.addAttribute("endDate",endDate);
        model.addAttribute("author",author);
        model.addAttribute("tagNames",tagNames);
        return "blogs";
    }

    @GetMapping("/newpost")
    public String getNewPostPage(Model model, Principal principal){
        Posts post = new Posts();
        model.addAttribute("blog",post);
        model.addAttribute("authorName",userService.getNameOfUser(principal.getName()));
        return "newpost";
    }

    @PostMapping("/newpost")
    public String addNewPost(@ModelAttribute("blog") Posts post, Principal principal){
        post.setUser(userService.getUserByUserName(principal.getName()));
        postsService.savePost(post);
        return "redirect:/";
    }

    @GetMapping("/post")
    public String showPost(@RequestParam Integer postId,Model model,Principal principal){
        boolean isOwnerOfPost = postsService.checkIsOwnerOfPost(postId,principal);
        Comments comment = new Comments();
        model.addAttribute("blog",postsService.getPostByID(postId));
        model.addAttribute("commentObject",comment);
        model.addAttribute("isOwnerOfPost",isOwnerOfPost);
        return "blog";
    }

    @GetMapping("/editpost")
    public String getEditPostPage(@RequestParam Integer postId, Principal principal, Model model) {
        Posts post = postsService.getPostByID(postId);
        boolean isOwnerOfPost = postsService.checkIsOwnerOfPost(postId,principal);
        if(isOwnerOfPost) {
            model.addAttribute("blog", post);
            return "editpost";
        }
        return "redirect:/";
    }

    @PostMapping("/editpost")
    public String editPost(@ModelAttribute("blog") Posts post, Principal principal){
        boolean isOwnerOfPost = postsService.checkIsOwnerOfPost(post.getId(),principal);
        if(isOwnerOfPost) {
            post.setUser(postsService.getPostByID(post.getId()).getUser());
            postsService.savePost(post);
        }
        return "redirect:/";
    }

    @GetMapping("/deletepost")
    public String deletePost(@RequestParam Integer postId, Principal principal){
        boolean isOwnerOfPost = postsService.checkIsOwnerOfPost(postId,principal);
        if(isOwnerOfPost) {
            postsService.deletePostById(postId);
        }
        return "redirect:/";
    }
}
