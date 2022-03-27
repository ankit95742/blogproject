package com.ankit.blog.service;

import com.ankit.blog.entity.Posts;
import com.ankit.blog.entity.Role;
import com.ankit.blog.entity.Tags;
import com.ankit.blog.entity.User;
import com.ankit.blog.repository.PostsRepository;
import com.ankit.blog.repository.TagsRepository;
import com.ankit.blog.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Service
public class PostsService{

    @Autowired
    private PostsRepository postsRepository;

    @Autowired
    private TagsRepository tagsRepository;

    @Autowired
    private UserRepository userRepository;

    public void savePost(Posts post) {
        List<String> tagNames = List.of(post.getTagNames().split(","));
        List<Tags> tagsList = new ArrayList<>();
        for (String tagName : tagNames){
            if (!tagsRepository.tagIsPresent(tagName)) {
                Tags tag = new Tags();
                tag.setTagName(tagName);
                tagsList.add(tag);
            }
            else{
                tagsList.add(tagsRepository.findByTagName(tagName));
            }
        }
        post.setTags(tagsList);
        postsRepository.save(post);
    }

    public Posts getPostByID(Integer id){
        Posts posts = postsRepository.getReferenceById(id);
        StringBuilder tagNames = new StringBuilder();
        for (Tags tag : posts.getTags()){
            tagNames.append(tag.getTagName()).append(",");
        }
        posts.setTagNames(String.valueOf(tagNames));
        return postsRepository.getPostById(id);
    }

    public void deletePostById(int id){
        Posts post = getPostByID(id);
        post.setTags(null);
        postsRepository.deleteById(id);
    }

    public boolean checkIsOwnerOfPost(Integer postId, Principal principal){
        boolean isOwnerOfPost = false;
        if (principal != null){
            Set<Role> roles = userRepository.getRolesByUserName(principal.getName());
            isOwnerOfPost = (postsRepository.getPostById(postId).getUser().getId() == userRepository.getIdByUserName(principal.getName()));
            for (Role role:roles){
                if (role.getRoleName().equals("ADMIN")) {
                    isOwnerOfPost = true;
                    break;
                }
            }
        }
        return isOwnerOfPost;
    }

    public Page<Posts> getPosts(Pageable paging){
        return postsRepository.findAll(paging);
    }

    public Set<String> getAllAuthors(){
        return postsRepository.getAllAuthors();
    }

    public Page<Posts> searchPosts(String keyword, Pageable paging){
        return postsRepository.findAllBySearch(keyword,paging);
    }

    public Page<Posts> filterPostsByDateAndAuthor(Date endDate, Date startDate, String author, Pageable paging){
        return postsRepository.findAllByPostCreatedAtLessThanEqualAndPostCreatedAtGreaterThanEqualAndAuthor(endDate,startDate,author,paging);
    }

    public Page<Posts> filterPostsByDateAndTags(Date endDate, Date startDate, List<String> tagNames, Pageable paging){

        return  postsRepository.findAllByPostCreatedAtLessThanEqualAndPostCreatedAtGreaterThanEqualAndTags(endDate,startDate,tagNames,paging);
    }

    public Page<Posts> filterPostsByAuthorAndTags(String author, List<String> tagNames,Pageable paging){
        return postsRepository.findAllByAuthorAndTags(author,tagNames,paging);
    }

    public Page<Posts> filterPostsByAuthorDateAndTags(Date endDate, Date startDate, String author, List<String> tagNames, Pageable paging){
        return postsRepository.findAllByPostCreatedAtLessThanEqualAndPostCreatedAtGreaterThanEqualAndAuthorAndTags(endDate,startDate,author,tagNames,paging);
    }

    public Page<Posts> filterPostsByAuthor(String author, Pageable paging){
        return postsRepository.findAllByAuthor(author,paging);
    }

    public Page<Posts> filterPostsByTags(List<String> tagNames, Pageable paging){
        return postsRepository.findAllByTags(tagNames,paging);
    }

    public Page<Posts> filterPostsByDate(Date endDate, Date startDate, Pageable paging){
        return  postsRepository.findAllByPostCreatedAtLessThanEqualAndPostCreatedAtGreaterThanEqual(endDate,startDate,paging);
    }

    public Page<Posts> searchAndFilterPostsByAuthorDateAndTags(Date endDate, Date startDate, String author, List<String> tagNames, String keyword, Pageable paging){
        return postsRepository.searchByKeywordAndFilterByDateAuthorTags(endDate,startDate,author,tagNames,keyword,paging);
    }

    public Page<Posts> searchAndFilterPostsByAuthorAndTags(String author, List<String> tagNames, String keyword, Pageable paging){
        return postsRepository.searchByKeywordAndFilterByAuthorTags(author,tagNames,keyword,paging);
    }

    public Page<Posts> searchAndFilterPostsByDateAndTags(Date endDate, Date startDate, List<String> tagNames, String keyword, Pageable paging){
        return postsRepository.searchByKeywordAndFilterByDateTags(endDate,startDate,tagNames,keyword,paging);
    }

    public Page<Posts> searchAndFilterPostsByAuthorAndDate(Date endDate, Date startDate, String author, String keyword,Pageable paging){
        return postsRepository.searchByKeywordAndFilterByDateAuthor(endDate,startDate,author,keyword,paging);
    }

    public Page<Posts> searchAndFilterPostsByAuthor(String author, String keyword, Pageable paging){
        return postsRepository.searchByKeywordAndFilterByAuthor(author,keyword,paging);
    }

    public Page<Posts> searchAndFilterPostsByTags(List<String> tagNames, String keyword, Pageable paging){
        return postsRepository.searchByKeywordAndFilterByTags(tagNames,keyword,paging);
    }

    public Page<Posts> searchAndFilterPostsByDate(Date endDate, Date startDate, String keyword, Pageable paging){
        return postsRepository.searchByKeywordAndFilterByDate(endDate,startDate,keyword,paging);
    }
}
