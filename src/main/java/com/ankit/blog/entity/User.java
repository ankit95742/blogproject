package com.ankit.blog.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Entity
@Data
public class User {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int id;
    @Column(nullable = false)
    private String name;
    @Column(name="email", unique=true, nullable = false)
    private String email;
    @Column(nullable = false)
    private String password;
    @ManyToMany(cascade = CascadeType.MERGE,fetch=FetchType.EAGER)
    @JoinTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles;
    @OneToMany(mappedBy = "user")
    private List<Posts> posts;
}
