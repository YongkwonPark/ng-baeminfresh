package sample.application.forum;

import sample.domain.forum.Category;
import sample.domain.forum.Post;
import sample.domain.forum.Topic;
import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

import javax.transaction.Transactional;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * @author ykpark@woowahan.com
 */
@Transactional
public interface ForumService {

    List<Category> categories();
    Category loadCategory(Long categoryId);


    List<Topic> loadTopics(Category category);
    Topic loadTopic(Long topicId);

    void write(Category category, TopicForm form);
    void edit(TopicForm form);
    void remove(Topic topic, String rawPassword);


    List<Post> loadPosts(Topic topic);


    @Data
    class TopicForm {

        public interface Write { }
        public interface Edit { }

        @NotNull(groups = Edit.class)
        private Long id;

        @NotEmpty
        private String title;

        @NotEmpty(groups = Write.class)
        private String author;

        @NotEmpty
        @Size(min = 4)
        private String password;

    }

}
