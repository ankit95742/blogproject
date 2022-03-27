package com.ankit.blog.repository;

import com.ankit.blog.entity.Tags;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TagsRepository extends JpaRepository<Tags, Integer> {

    @Query("select case when count(t) > 0 then true else false end from Tags t where t.tagName = :tagName")
    boolean tagIsPresent(@Param("tagName")String tagName);

    @Query("select t from Tags t where t.tagName = :tagName")
    Tags findByTagName(@Param("tagName")String tagName);
}
