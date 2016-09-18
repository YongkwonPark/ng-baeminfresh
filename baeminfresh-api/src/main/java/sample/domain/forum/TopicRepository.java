package sample.domain.forum;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;

import java.util.List;
import java.util.Optional;

/**
 * @author ykpark@woowahan.com
 */
public interface TopicRepository extends JpaRepository<Topic, Long>
                                       , JpaSpecificationExecutor<Topic>
                                       , QueryDslPredicateExecutor<Topic> {

    Optional<Topic> findById(Long topicId);
    List<Topic> findByCategory(Category category);

}
