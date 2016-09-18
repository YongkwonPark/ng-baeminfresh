package sample.domain.forum;

import lombok.NonNull;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;
import java.util.function.Consumer;

/**
 * @author ykpark@woowahan.com
 */
@Entity
public class Topic implements Password.PasswordProtectable {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String author;
    private Password password;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Category category;

    private Date createdAt = new Date();
    private Date updatedAt = createdAt;


    public Topic(String title, String author, String rawPassword, Category category) {
        Objects.requireNonNull(title, "title must not be null");
        Objects.requireNonNull(author, "author must not be null");
        Objects.requireNonNull(rawPassword, "password must not be null");
        Objects.requireNonNull(category, "category must not be null");

        this.title = title;
        this.author = author;
        this.password = Password.wrap(rawPassword);
        this.category = category;
    }

    public void edit(@NonNull String title, @NonNull String author, @NonNull String rawPassword) {
        verify(rawPassword);

        this.title = title;
        this.updatedAt = new Date();

        if (!author.isEmpty()) {
            this.author = author;
        }
    }

    public void ifRemovable(String rawPassword, Consumer<Topic> consumer) {
        verify(rawPassword);
        consumer.accept(this);
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
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

        Topic topic = (Topic) o;

        if (!title.equals(topic.title)) return false;
        if (!author.equals(topic.author)) return false;
        return createdAt.equals(topic.createdAt);

    }

    @Override
    public int hashCode() {
        int result = title.hashCode();
        result = 31 * result + author.hashCode();
        result = 31 * result + createdAt.hashCode();
        return result;
    }

    // for hibernate
    private Topic() { }

}
