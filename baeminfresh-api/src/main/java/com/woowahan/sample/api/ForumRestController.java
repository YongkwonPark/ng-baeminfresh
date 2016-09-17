package com.woowahan.sample.api;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.woowahan.sample.modules.forum.*;
import com.woowahan.sample.modules.forum.PostRepository.CountByTopicID;
import com.woowahan.sample.modules.forum.components.ForumService;
import com.woowahan.sample.modules.forum.components.ForumService.TopicForm;
import com.woowahan.sample.modules.forum.specs.TopicSpec;
import lombok.Builder;
import lombok.Data;
import lombok.val;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.data.jpa.repository.query.JpaQueryCreator;
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
import java.util.Spliterator;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * @author ykpark@woowahan.com
 */
@RestController
@RequestMapping(value = "/sample/forum")
public class ForumRestController {

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
    public List<TopicData> topics(@PathVariable Long categoryId, TopicSearchCommand command) {
        command.category = forumService.loadCategory(categoryId);

        val topics = topicRepository.findAll(command);
        val postCountByTopicIDs = postRepository.countByTopics(topics)
                .stream().collect(Collectors.toMap(CountByTopicID::getTopicId, CountByTopicID::getPostCount));

        return topics.stream().map(TopicData::of)
                              .peek(data -> data.setPostCount(postCountByTopicIDs.getOrDefault(data.getId(), 0l)))
                              .collect(Collectors.toList());
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

        QPost query = QPost.post;
        Iterable<Post> posts = postRepository.findAll(query.topic.eq(topic), query.createdAt.desc());

        return StreamSupport.stream(posts.spliterator(), false)
                            .map(PostData::of).collect(Collectors.toList());
    }


    public void validateAndThrow(Object target, BindingResult bindingResult, Object...validationHints) throws BindException {
        validator.validate(target, bindingResult);
        validator.validate(target, bindingResult, validationHints);

        if (bindingResult.hasErrors()) {
            throw new BindException(bindingResult);
        }
    }


    @Data
    static class TopicSearchCommand implements Specification<Topic> {
        Category category;
        String title;

        @Override
        public Predicate toPredicate(Root<Topic> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
            Objects.requireNonNull(category);

            Specifications<Topic> specs = Specifications.where(TopicSpec.isEqualTo(category));
            if (StringUtils.hasText(title)) {
                specs = specs.and(TopicSpec.likeTitle(title));
            }

            query.orderBy(cb.desc(root.get("createdAt")));

            return specs.toPredicate(root, query, cb);
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

        Long postCount = 0l;

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
