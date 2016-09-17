package com.woowahan.sample.modules.forum.specs;

import com.woowahan.sample.modules.forum.Category;
import com.woowahan.sample.modules.forum.Topic;
import org.springframework.data.jpa.domain.Specification;

/**
 * @author ykpark@woowahan.com
 */
public class TopicSpec {

    public static Specification<Topic> isEqualTo(Category category) {
        return (root, query, cb) -> cb.equal(root.get("category"), category);
    }

    public static Specification<Topic> likeTitle(String title) {
        return (root, query, cb) -> cb.like(root.get("title"), "%" + title + "%");
    }


    private TopicSpec() { }

}
