package com.woowahan.sample.modules.forum;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * @author ykpark@woowahan.com
 */
public interface TopicRepository extends JpaRepository<Topic, Long> {

    Optional<Topic> findById(Long topicId);
    List<Topic> findByCategory(Category category);

}
