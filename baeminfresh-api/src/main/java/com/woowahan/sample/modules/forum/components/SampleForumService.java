package com.woowahan.sample.modules.forum.components;

import com.woowahan.sample.modules.forum.*;
import com.woowahan.sample.modules.forum.ForumExceptions.CategoryNotFoundException;
import com.woowahan.sample.modules.forum.ForumExceptions.TopicNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

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
    public Topic loadTopic(Long topicId) {
        return topicRepository.findById(topicId)
                              .orElseThrow(() -> new TopicNotFoundException(topicId));
    }

    @Override
    public void write(Category category, TopicForm form) {
        topicRepository.save(new Topic(form.getTitle(), form.getAuthor(), form.getPassword(), category));
    }

    @Override
    public void edit(TopicForm form) {
        loadTopic(form.getId()).edit(form.getTitle(), form.getAuthor(), form.getPassword());
    }

    @Override
    public void remove(Topic topic, String rawPassword) {
        topic.ifRemovable(rawPassword, topicRepository::delete);
    }

    @Override
    public List<Post> loadPosts(Topic topic) {
        return postRepository.findByTopic(topic);
    }

}
