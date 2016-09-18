package sample.resources.forum;

import com.fasterxml.jackson.databind.JsonNode;
import com.woowahan.BaeminfreshApiApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import sample.service.forum.ForumService.EditTopic;
import sample.service.forum.ForumService.WriteTopic;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpMethod.POST;

/**
 * @author ykpark@woowahan.com
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = BaeminfreshApiApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ForumRestControllerTest {

    static final String URI_TOPICS = "/sample/forum/categories/{categoryId}/topics";
    static final String URI_WRITE_TOPIC = "/sample/forum/categories/{categoryId}/topics";
    static final String URI_EDIT_TOPIC = "/sample/forum/categories/{categoryId}/topics/{topicId}";

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void topics() {
        ResponseEntity<JsonNode> entity = restTemplate.getForEntity(URI_TOPICS, JsonNode.class, 1);
        assertThat(entity.getBody().path("content").isArray()).isTrue();
    }

    @Test
    public void writeTopic() {
        WriteTopic command = new WriteTopic();
        command.setTitle("write new topic!");
        command.setAuthor("junit");
        command.setPassword("password");

        ResponseEntity<Void> entity = restTemplate.exchange(URI_WRITE_TOPIC, POST, new HttpEntity(command), Void.class, 1);
        assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

    @Test
    public void writeTopic_ValidationFailed() {
        EditTopic command = new EditTopic();
        command.setId(UUID.randomUUID());

        ResponseEntity<JsonNode> entity = restTemplate.exchange(URI_WRITE_TOPIC, POST, new HttpEntity(command), JsonNode.class, 1);
        assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(entity.getBody().path("additionalInformation").size()).isEqualTo(4);
    }

}