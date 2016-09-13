package com.woowahan.sample.modules.forum;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;

/**
 * @author ykpark@woowahan.com
 */
@Entity
public class Post implements Password.PasswordProtectable {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String text;
    private String author;
    private Password password;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Topic topic;

    private Date createdAt = new Date();
    private Date updatedAt = createdAt;


    public Post(String text, String author, String rawPassword, Topic topic) {
        Objects.requireNonNull(text, "text must not be null");
        Objects.requireNonNull(author, "author must not be null");
        Objects.requireNonNull(rawPassword, "password must not be null");
        Objects.requireNonNull(topic, "topic must not be null");

        this.text = text;
        this.author = author;
        this.password = Password.wrap(rawPassword);
        this.topic = topic;
    }

    public void edit(String text, String rawPassword) {
        verify(rawPassword);

        this.text = text;
        this.updatedAt = new Date();
    }

    public Long getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    public String getAuthor() {
        return author;
    }

    public Password getPassword() {
        return password;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Post post = (Post) o;

        if (!text.equals(post.text)) return false;
        if (!author.equals(post.author)) return false;
        return createdAt.equals(post.createdAt);

    }

    @Override
    public int hashCode() {
        int result = text.hashCode();
        result = 31 * result + author.hashCode();
        result = 31 * result + createdAt.hashCode();
        return result;
    }

    // for hibernate
    private Post() { }

}
