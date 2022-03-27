package com.ankit.blog.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
public class Role {

    @Id
    private int id;
    private String roleName;
    @ManyToMany(mappedBy = "roles")
    private List<User> user;
}
