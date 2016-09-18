package sample.service.forum;

import lombok.val;
import org.springframework.stereotype.Service;
import sample.domain.forum.*;
import sample.domain.forum.ForumExceptions.CategoryNotFoundException;
import sample.domain.forum.ForumExceptions.TopicNotFoundException;

import java.util.List;
import java.util.UUID;

/**
 * @author ykpark@woowahan.com
 */
@Service
public class SampleForumService implements ForumService {

    private CategoryRepository categoryRepository;
    private TopicRepository topicRepository;
    private PostRepository postRepository;


    public SampleForumService(CategoryRepository categoryRepository, TopicRepository topicRepository, PostRepository postRepository) {
        this.categoryRepository = categoryRepository;
        this.topicRepository = topicRepository;
        this.postRepository = postRepository;
    }


    @Override
    public List<Category> categories() {
        return categoryRepository.findAll();
    }

    @Override
    public Category loadCategory(Long categoryId) {
        return categoryRepository.findById(categoryId)
                                 .orElseThrow(() -> new CategoryNotFoundException(categoryId));
    }

    @Override
    public List<Topic> loadTopics(Category category) {
        return topicRepository.findByCategory(category);
    }

    @Override
    public Topic loadTopic(UUID topicId) {
        return topicRepository.findById(topicId)
                              .orElseThrow(() -> new TopicNotFoundException(topicId));
    }


    @Override
    public void write(WriteTopic command) {
        val category = loadCategory(command.getCategoryId());

        topicRepository.save(new Topic(command.getId(), command.getTitle(), command.getAuthor(), command.getPassword(), category));
    }

    @Override
    public void edit(EditTopic command) {
        loadTopic(command.getId()).edit(command.getTitle(), command.getAuthor(), command.getPassword());
    }

    @Override
    public void remove(RemoveTopic command) {
        val topic = loadTopic(command.getTopicId());

        topic.ifRemovable( command.getPassword(), () -> postRepository.countByTopic(topic), topicRepository::delete);
    }

    @Override
    public void reply(ReplyPost command) {
        val topic = loadTopic(command.getTopicId());

        topic.reply(command.toPostCreator());
        topicRepository.save(topic);
    }

    @Override
    public List<Post> loadPosts(Topic topic) {
        return postRepository.findByTopic(topic);
    }

}
