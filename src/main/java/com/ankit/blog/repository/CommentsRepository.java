package com.ankit.blog.repository;

import com.ankit.blog.entity.Comments;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentsRepository extends JpaRepository<Comments , Integer> {
}
