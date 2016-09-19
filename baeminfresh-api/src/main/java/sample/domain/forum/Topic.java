package sample.domain.forum;

import lombok.NonNull;
import lombok.val;
import org.hibernate.annotations.Type;
import org.springframework.dao.DataAccessException;

import javax.persistence.*;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * @author ykpark@woowahan.com
 */
@Entity
public class Topic implements Password.PasswordProtectable {

    @Id
    @Type(type = "org.hibernate.type.UUIDCharType")
    private UUID id;

    private String title;
    private String author;
    private Password password;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Category category;

    @OneToMany(mappedBy = "topic", fetch = FetchType.LAZY)
    private List<Post> posts = new ArrayList<>();

    private Date createdAt;
    private Date updatedAt;


    public Topic(UUID id, String title, String author, String rawPassword, Category category) {
        Objects.requireNonNull(id, "id must not be null");
        Objects.requireNonNull(title, "title must not be null");
        Objects.requireNonNull(author, "author must not be null");
        Objects.requireNonNull(rawPassword, "password must not be null");
        Objects.requireNonNull(category, "category must not be null");

        this.id = id;
        this.title = title;
        this.author = author;
        this.password = Password.wrap(rawPassword);
        this.category = category;
        this.createdAt = new Date();
        this.updatedAt = createdAt;
    }

    public void edit(@NonNull String title, @NonNull String author, @NonNull String rawPassword) {
        verify(rawPassword);

        this.title = title;
        this.updatedAt = new Date();

        if (!author.isEmpty()) {
            this.author = author;
        }
    }

    public void ifRemovable(String rawPassword, Function<Topic, Long> postCounter, Consumer<Topic> action) {
        Objects.requireNonNull(postCounter, "postCreator must not be null");

        verify(rawPassword);

        if (postCounter.apply(this) > 0) {
            throw new ForumExceptions.PostAlreadyExistsInTopicException(getId());
        }

        if (Objects.nonNull(action)) {
            action.accept(this);
        }
    }


    public UUID getId() {
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

        return id.equals(topic.id);

    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }


    // for hibernate
    private Topic() { }

}
