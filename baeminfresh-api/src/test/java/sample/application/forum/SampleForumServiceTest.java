package sample.application.forum;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.MockBeans;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import sample.domain.forum.*;

import java.util.Optional;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author ykpark@woowahan.com
 */
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = SampleForumServiceTest.SampleForumServiceTestConfig.class)
@MockBeans({ @MockBean(PostRepository.class) })
public class SampleForumServiceTest {

    @Autowired
    private SampleForumService forumService;

    @MockBean
    private CategoryRepository categoryRepository;

    @MockBean
    private TopicRepository topicRepository;


    @Test
    public void categoryFound() {
        Category category = mock(Category.class);
        when(category.getId()).thenReturn(Long.valueOf(999));
        when(categoryRepository.findById(category.getId())).thenReturn(Optional.of(category));

        assertThat(category, is(forumService.loadCategory(category.getId())));
    }

    @Test(expected = ForumExceptions.CategoryNotFoundException.class)
    public void categoryNotFound() {
        when(categoryRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        forumService.loadCategory(Long.MAX_VALUE);
    }


    @Configuration
    @ComponentScan
    static class SampleForumServiceTestConfig {

    }

}