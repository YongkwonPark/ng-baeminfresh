package sample.service.forum;

import com.woowahan.common.service.validation.Validatable;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import org.hibernate.validator.constraints.NotEmpty;
import sample.domain.forum.Category;
import sample.domain.forum.Post;
import sample.domain.forum.PostCreator;
import sample.domain.forum.Topic;

import javax.transaction.Transactional;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.UUID;

/**
 * @author ykpark@woowahan.com
 */
@Transactional
public interface ForumService {

    List<Category> categories();
    Category loadCategory(Long categoryId);


    List<Topic> loadTopics(Category category);
    Topic loadTopic(UUID topicId);

    void write(WriteTopic command);
    void edit(EditTopic command);
    void remove(RemoveTopic command);

    void reply(ReplyPost command);


    List<Post> loadPosts(Topic topic);


    @Data
    class WriteTopic implements Validatable {

        @Setter(value = AccessLevel.NONE)
        private UUID id = UUID.randomUUID();

        @NotEmpty
        private String title;

        @NotEmpty
        private String author;

        @NotEmpty
        @Size(min = 4)
        private String password;

        @NotNull
        private Long categoryId;

    }

    @Data
    class EditTopic implements Validatable {

        @NotNull
        private UUID id;

        @NotEmpty
        private String title;

        @NotEmpty
        private String author;

        private String password = "";

    }

    @Data
    class RemoveTopic implements Validatable {

        @NotNull
        private Long categoryId;

        @NotNull
        private UUID topicId;

        private String password = "";

    }

    @Data
    class ReplyPost implements Validatable {

        @NotNull
        private Long categoryId;

        @NotNull
        private UUID topicId;

        @NotEmpty
        private String text;

        @NotEmpty
        private String author;

        @NotEmpty
        private String password;

        public PostCreator toPostCreator() {
            return new PostCreator(text, author, password);
        }

    }

}
