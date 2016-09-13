package com.woowahan.sample.modules.forum;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author ykpark@woowahan.com
 */
public interface PostRepository extends JpaRepository<Post, Long> {

    List<Post> findByTopic(Topic topic);

}
