package sample.service.forum;

import com.woowahan.BaeminfreshApiApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.transaction.TestTransaction;
import sample.domain.forum.PostCreator;
import sample.domain.forum.PostRepository;
import sample.domain.forum.Topic;

import javax.transaction.Transactional;
import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * @author ykpark@woowahan.com
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = BaeminfreshApiApplication.class)
@Transactional
public class SampleForumServiceIntegrationTest {

    @Autowired
    private SampleForumService forumService;

    @Autowired
    private PostRepository postRepository;

    @Test
    public void reply() {
        Topic topic = forumService.loadTopic(UUID.fromString("53fc14fc-2a3a-41d4-ac2a-cd35ed935217"));
        Long countByTopic = postRepository.countByTopic(topic);

        topic.reply(new PostCreator("text", "author", "password"));

        TestTransaction.flagForCommit();
        TestTransaction.end();
        TestTransaction.start();

        assertThat(countByTopic + 1, is(postRepository.countByTopic(topic)));
    }

}