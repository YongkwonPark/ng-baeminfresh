package com.woowahan.sample.api;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.woowahan.sample.modules.forum.*;
import com.woowahan.sample.modules.forum.PostRepository.CountByTopicID;
import com.woowahan.sample.modules.forum.components.ForumService;
import com.woowahan.sample.modules.forum.components.ForumService.TopicForm;
import com.woowahan.sample.modules.forum.specs.TopicSpec;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.val;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.beanvalidation.SpringValidatorAdapter;
import org.springframework.web.bind.annotation.*;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.springframework.data.domain.Sort.Direction.DESC;

/**
 * @author ykpark@woowahan.com
 */
@RestController
@RequestMapping(value = "/sample/forum")
public class ForumRestController {

    final static String FIELD_CREATEDAT = "createdAt";

    private ForumService forumService;
    private SpringValidatorAdapter validator;

    private TopicRepository topicRepository;
    private PostRepository postRepository;

    public ForumRestController(ForumService forumService, SpringValidatorAdapter validator, TopicRepository topicRepository, PostRepository postRepository) {
        this.forumService = forumService;
        this.validator = validator;
        this.topicRepository = topicRepository;
        this.postRepository = postRepository;
    }

    @GetMapping(value = "/categories")
    public List<Category> categories() {
        return forumService.categories();
    }

    @GetMapping(value = "/categories/{categoryId}/topics")
    public Page<TopicData> topics(TopicSearchQuery query
                                , @PageableDefault(sort = FIELD_CREATEDAT, direction = DESC) Pageable pageable) {
        query.category = forumService.loadCategory(query.getCategoryId());

        System.out.println("pageable = " + pageable);
        System.out.println("pageable.getSort() = " + pageable.getSort());
        if (Objects.isNull(pageable.getSort().getOrderFor(FIELD_CREATEDAT))) {
            pageable.getSort().and(new Sort(DESC, FIELD_CREATEDAT));
        }

        val topics = topicRepository.findAll(query, pageable);
        val postCountByTopicIDs = postRepository.countByTopics(topics.getContent())
                .stream().collect(Collectors.toMap(CountByTopicID::getTopicId, CountByTopicID::getPostCount));

        return topics.map(source -> TopicData.of(source).postCount(postCountByTopicIDs.getOrDefault(source.getId(), 0l)));
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
    public Page<PostData> posts(@PathVariable Long categoryId, @PathVariable Long topicId
                              , @PageableDefault(sort = FIELD_CREATEDAT, direction = DESC) Pageable pageable) {
        forumService.loadCategory(categoryId);
        val topic = forumService.loadTopic(topicId);

        QPost query = QPost.post;
        val posts = postRepository.findAll(query.topic.eq(topic), pageable);

        return posts.map(PostData::of);
    }

    private void validateAndThrow(Object target, BindingResult bindingResult, Object...validationHints) throws BindException {
        validator.validate(target, bindingResult);
        validator.validate(target, bindingResult, validationHints);

        if (bindingResult.hasErrors()) {
            throw new BindException(bindingResult);
        }
    }


    @Data
    static class TopicSearchQuery implements Specification<Topic> {
        Long categoryId;
        Category category;
        String title;

        @Override
        public Predicate toPredicate(Root<Topic> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
            Objects.requireNonNull(category);

            Specifications<Topic> specs = Specifications.where(TopicSpec.isEqualTo(category));
            if (StringUtils.hasText(title)) {
                specs = specs.and(TopicSpec.likeTitle(title));
            }

            return specs.toPredicate(root, query, cb);
        }

    }

    @Data
    @Accessors(fluent = true)
    @JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
    static class TopicData {

        Long id;
        String title;
        String author;
        Date createdAt;
        Date updatedAt;

        Long postCount = 0l;

        static TopicData of(Topic source) {
            return new TopicData().id(source.getId())
                                  .title(source.getTitle())
                                  .author(source.getAuthor())
                                  .createdAt(source.getCreatedAt())
                                  .updatedAt(source.getUpdatedAt());
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
