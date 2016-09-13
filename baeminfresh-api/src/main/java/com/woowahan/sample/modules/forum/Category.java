package com.woowahan.sample.modules.forum;

import lombok.NonNull;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

/**
 * @author ykpark@woowahan.com
 */
@Entity
public class Category {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    private Date createdAt = new Date();
    private Date updatedAt = createdAt;


    public Category(@NonNull String name) {
        this.name = name;
    }

    public void rename(@NonNull String name) {
        this.name = name;
        this.updatedAt = new Date();
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
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

        Category category = (Category) o;

        if (!name.equals(category.name)) return false;
        return createdAt.equals(category.createdAt);

    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + createdAt.hashCode();
        return result;
    }

    // for hibernate
    private Category() { }

}
