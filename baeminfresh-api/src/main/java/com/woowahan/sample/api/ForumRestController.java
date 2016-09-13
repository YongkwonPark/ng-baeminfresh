package com.woowahan.sample.api;

import com.woowahan.sample.modules.forum.Category;
import com.woowahan.sample.modules.forum.Post;
import com.woowahan.sample.modules.forum.Topic;
import com.woowahan.sample.modules.forum.components.ForumService;
import com.woowahan.sample.modules.forum.components.ForumService.TopicForm;
import lombok.Builder;
import lombok.Data;
import lombok.val;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.beanvalidation.SpringValidatorAdapter;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author ykpark@woowahan.com
 */
@RestController
@RequestMapping(value = "/sample/forum")
public class ForumRestController {

    private ForumService forumService;
    private SpringValidatorAdapter validator;

    public ForumRestController(ForumService forumService, SpringValidatorAdapter validator) {
        this.forumService = forumService;
        this.validator = validator;
    }

    @GetMapping(value = "/categories")
    public List<Category> categories() {
        return forumService.categories();
    }

    @GetMapping(value = "/categories/{categoryId}/topics")
    public List<TopicData> topics(@PathVariable Long categoryId) {
        val category = forumService.loadCategory(categoryId);

        return forumService.loadTopics(category).stream().map(TopicData::of).collect(Collectors.toList());
    }

    @PostMapping(value = "/categories/{categoryId}/topics", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public void writeTopic(@PathVariable Long categoryId, @RequestBody TopicForm form, BindingResult bindingResult) throws BindException {
        validateAndThrow(form, bindingResult, TopicForm.Write.class);

        forumService.write(forumService.loadCategory(categoryId), form);
    }

    @PutMapping(value = "/categories/{categoryId}/topics/{topicId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void editTopic(@PathVariable Long categoryId, @PathVariable Long topicId, @RequestBody TopicForm form, BindingResult bindingResult) throws BindException {
        form.setId(topicId);

        validateAndThrow(form, bindingResult, TopicForm.Edit.class);

        forumService.edit(form);
    }

    @DeleteMapping(value = "/categories/{categoryId}/topics/{topicId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void removeTopic(@PathVariable Long categoryId, @PathVariable Long topicId, @RequestBody TopicRemoveCommand command) {
        forumService.loadCategory(categoryId);
        val topic = forumService.loadTopic(topicId);

        forumService.remove(topic, command.password);
    }

    @GetMapping(value = "/categories/{categoryId}/topics/{topicId}/posts")
    public List<PostData> posts(@PathVariable Long categoryId, @PathVariable Long topicId) {
        forumService.loadCategory(categoryId);
        val topic = forumService.loadTopic(topicId);

        return forumService.loadPosts(topic).stream().map(PostData::of).collect(Collectors.toList());
    }


    public void validateAndThrow(Object target, BindingResult bindingResult, Object...validationHints) throws BindException {
        validator.validate(target, bindingResult);
        validator.validate(target, bindingResult, validationHints);

        if (bindingResult.hasErrors()) {
            throw new BindException(bindingResult);
        }
    }



    @Data
    @Builder
    static class TopicData {

        Long id;
        String title;
        String author;
        Date createdAt;
        Date updatedAt;

        static TopicData of(Topic source) {
            return TopicData.builder().id(source.getId())
                                      .title(source.getTitle())
                                      .author(source.getAuthor())
                                      .createdAt(source.getCreatedAt())
                                      .updatedAt(source.getUpdatedAt())
                                      .build();
        }

    }

    @Data
    @Builder
    static class PostData {

        Long id;
        String text;
        String author;
        Date createdAt;
        Date updatedAt;

        static PostData of(Post source) {
            return PostData.builder().id(source.getId())
                                     .text(source.getText())
                                     .author(source.getAuthor())
                                     .createdAt(source.getCreatedAt())
                                     .updatedAt(source.getUpdatedAt())
                                     .build();
        }

    }

    @Data
    class TopicRemoveCommand {
        String password;
    }

}
