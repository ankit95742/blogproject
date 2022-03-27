package com.ankit.blog.entity;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Data
@Table(name = "tags")
public class Tags {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name="name", unique = true, nullable = false)
    private String tagName;
    @Column(name = "created_at")
    @CreationTimestamp
    private Date tagCreatedAt;
    @Column(name = "updated_at")
    @UpdateTimestamp
    private Date tagUpdatedAt;
    @ManyToMany(mappedBy = "tags",fetch = FetchType.LAZY)
    private List<Posts> posts;
}
