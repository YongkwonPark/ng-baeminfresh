package sample.domain.forum;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;

import java.util.List;

/**
 * @author ykpark@woowahan.com
 */
public interface PostRepository extends JpaRepository<Post, Long>
                                      , QueryDslPredicateExecutor<Post> {

    List<Post> findByTopic(Topic topic);

    @Query("select p.topic.id as topicId, count(p.id) as postCount from Post p where p.topic in ?1 group by p.topic")
    List<CountByTopicID> countByTopics(List<Topic> topics);


    interface CountByTopicID {
        Long getTopicId();
        Long getPostCount();
    }

}
