package com.ankit.blog.repository;

import com.ankit.blog.entity.Posts;
import com.ankit.blog.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Set;

@Repository
public interface PostsRepository extends JpaRepository<Posts, Integer> {

    @Query("select p from Posts p where p.id = :id")
    Posts getPostById(Integer id);

    @Query("select p.author from Posts p")
    Set<String> getAllAuthors();

    @Query("select p from Posts p left join p.tags t where t.tagName like %:keyword% or p.title like %:keyword% or p.author like %:keyword% or p.excerpt like %:keyword% or p.content like %:keyword% ")
    Page<Posts> findAllBySearch(String keyword, Pageable pageable);

    @Query("select p from Posts p where p.postCreatedAt <= ?1 and p.postCreatedAt >= ?2 and p.author = ?3")
    Page<Posts> findAllByPostCreatedAtLessThanEqualAndPostCreatedAtGreaterThanEqualAndAuthor(Date endDate, Date startDate, String author, Pageable pageable);

    @Query("select p from Posts p left join p.tags t where t.tagName in :tagNames and p.postCreatedAt <= :endDate and p.postCreatedAt >= :startDate ")
    Page<Posts> findAllByPostCreatedAtLessThanEqualAndPostCreatedAtGreaterThanEqualAndTags(Date endDate, Date startDate, List<String> tagNames, Pageable pageable);

    @Query("select p from Posts p left join p.tags t where t.tagName in :tagNames and p.author = :author")
    Page<Posts> findAllByAuthorAndTags(String author, List<String> tagNames, Pageable pageable);

    @Query("select p from Posts p left join p.tags t where t.tagName in :tagNames and p.postCreatedAt <= :endDate and p.postCreatedAt >= :startDate and p.author = :author")
    Page<Posts> findAllByPostCreatedAtLessThanEqualAndPostCreatedAtGreaterThanEqualAndAuthorAndTags(Date endDate, Date startDate, String author, List<String> tagNames, Pageable pageable);

    @Query("select p from Posts p where p.postCreatedAt <= ?1 and p.postCreatedAt >= ?2")
    Page<Posts> findAllByPostCreatedAtLessThanEqualAndPostCreatedAtGreaterThanEqual(Date endDate, Date startDate, Pageable pageable);

    @Query("select p from Posts p where p.author = :author")
    Page<Posts> findAllByAuthor(String author, Pageable pageable);

    @Query("select p from Posts p left join p.tags t where t.tagName in :tagNames ")
    Page<Posts> findAllByTags(List<String> tagNames, Pageable pageable);

    @Query("select p from Posts p left join p.tags t where t.tagName in :tagNames and p.postCreatedAt <= :endDate and p.postCreatedAt >= :startDate and p.author = :author and concat(p.title,' ',p.content,' ',p.author,' ',t.tagName) like %:keyword%")
    Page<Posts> searchByKeywordAndFilterByDateAuthorTags(Date endDate, Date startDate, String author, List<String> tagNames, String keyword, Pageable pageable);

    @Query("select p from Posts p left join p.tags t where p.postCreatedAt <= :endDate and p.postCreatedAt >= :startDate and p.author = :author and concat(p.title,' ',p.content,' ',p.author,' ',t.tagName) like %:keyword%")
    Page<Posts> searchByKeywordAndFilterByDateAuthor(Date endDate, Date startDate, String author, String keyword, Pageable pageable);

    @Query("select p from Posts p left join p.tags t where t.tagName in :tagNames and p.postCreatedAt <= :endDate and p.postCreatedAt >= :startDate and concat(p.title,' ',p.content,' ',p.author,' ',t.tagName) like %:keyword%")
    Page<Posts> searchByKeywordAndFilterByDateTags(Date endDate, Date startDate, List<String> tagNames, String keyword, Pageable pageable);

    @Query("select p from Posts p left join p.tags t where t.tagName in :tagNames and p.author = :author and concat(p.title,' ',p.content,' ',p.author,' ',t.tagName) like %:keyword%")
    Page<Posts> searchByKeywordAndFilterByAuthorTags(String author, List<String> tagNames, String keyword, Pageable pageable);

    @Query("select p from Posts p left join p.tags t where p.author = :author and concat(p.title,' ',p.content,' ',p.author,' ',t.tagName) like %:keyword%")
    Page<Posts> searchByKeywordAndFilterByAuthor(String author, String keyword, Pageable pageable);

    @Query("select p from Posts p left join p.tags t where t.tagName in :tagNames and concat(p.title,' ',p.content,' ',p.author,' ',t.tagName) like %:keyword%")
    Page<Posts> searchByKeywordAndFilterByTags(List<String> tagNames, String keyword, Pageable pageable);

    @Query("select p from Posts p left join p.tags t where p.postCreatedAt <= :endDate and p.postCreatedAt >= :startDate and concat(p.title,' ',p.content,' ',p.author,' ',t.tagName) like %:keyword%")
    Page<Posts> searchByKeywordAndFilterByDate(Date endDate, Date startDate, String keyword, Pageable pageable);
}