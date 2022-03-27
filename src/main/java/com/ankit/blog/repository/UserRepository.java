package com.ankit.blog.repository;

import com.ankit.blog.entity.Role;
import com.ankit.blog.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    User findByEmail(String email);

    @Query(value = "select u.name from User u where u.email = :userName")
    String getNameByEmail(String userName);

    @Query(value = "select u.id from User u where u.email = :userName")
    Integer getIdByUserName(String userName);

    @Query(value = "select u.roles from User u where u.email = :userName")
    Set<Role>  getRolesByUserName(String userName);
}
