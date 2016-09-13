package com.woowahan.sample.api;

import com.fasterxml.jackson.databind.JsonNode;
import com.woowahan.baeminfresh.BaeminfreshAPIApplication;
import com.woowahan.sample.modules.forum.components.ForumService.TopicForm;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpMethod.POST;

/**
 * @author ykpark@woowahan.com
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = BaeminfreshAPIApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ForumRestControllerTest {

    static final String URI_TOPICS = "/sample/forum/categories/{categoryId}/topics";
    static final String URI_WRITE_TOPIC = "/sample/forum/categories/{categoryId}/topics";

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void topics() {
        ResponseEntity<JsonNode> entity = restTemplate.getForEntity(URI_TOPICS, JsonNode.class, 1);
        assertThat(entity.getBody().isArray()).isTrue();
    }

    @Test
    public void writeTopic() {
        TopicForm form = new TopicForm();
        form.setTitle("write new topic!");
        form.setAuthor("junit");
        form.setPassword("password");

        ResponseEntity<Void> entity = restTemplate.exchange(URI_WRITE_TOPIC, POST, new HttpEntity(form), Void.class, 1);
        assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

    @Test
    public void writeTopic_ValidationFailed() {
        TopicForm form = new TopicForm();

        ResponseEntity<JsonNode> entity = restTemplate.exchange(URI_WRITE_TOPIC, POST, new HttpEntity(form), JsonNode.class, 1);
        assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(entity.getBody().path("additionalInformation").size()).isEqualTo(3);
    }

}