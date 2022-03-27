package com.ankit.blog.entity;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Data
@Table(name = "posts")
public class Posts {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(nullable = false)
    private String title;
    @Column(nullable = false)
    private String author;
    private String excerpt;
    @Column(nullable = false)
    @Lob
    private String content;
    @Column(name = "published_at")
    @CreationTimestamp
    private Date postPublishedAt;
    @Column(columnDefinition = "boolean default true")
    private boolean isPublished;
    @Column(name = "created_at")
    @CreationTimestamp
    private Date postCreatedAt;
    @Column(name = "updated_at")
    @UpdateTimestamp
    private Date postUpdatedAt;
    @Transient
    private String tagNames;
    @OneToMany(mappedBy = "postId")
    private List<Comments> comments;
    @ManyToMany(cascade=CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinTable(
            name = "post_tags",
            joinColumns = @JoinColumn(name = "post_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id"))
    private List<Tags> tags;
    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "user")
    private User user;
}
