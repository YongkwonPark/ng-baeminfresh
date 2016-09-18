package sample.domain.forum;

import org.junit.Test;

import java.util.function.Consumer;

import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * @author ykpark@woowahan.com
 */
public class TopicTest {

    @Test
    public void requiredAttributes() {
        assertRequiredAttributes(null, null, null, null);
        assertRequiredAttributes("title", null, null, null);
        assertRequiredAttributes("title", "author", null, null);
        assertRequiredAttributes("title", "author", "rawPassword", null);

        new Topic("title", "author", "rawPassword", mock(Category.class));
    }

    private void assertRequiredAttributes(String title, String author, String rawPassword, Category category) {
        try {
            new Topic(title, author, rawPassword, category);
            fail(String.format("title = {}, author = {}, password{} = ,category = {}", title, author, rawPassword, category));
        } catch (Exception e) {
            assertThat(e, instanceOf(NullPointerException.class));
        }
    }

    @Test
    public void edit() {
        String password = "password";
        Topic topic = new Topic("title", "author", password, mock(Category.class));

        String editTitle = "editTitle";
        String editAuthor = "editAuthor";
        topic.edit(editTitle, editAuthor, password);

        assertThat(topic.getTitle(), is(editTitle));
        assertThat(topic.getAuthor(), is(editAuthor));
    }

    @Test
    public void remove() {
        String password = "password";
        Topic topic = new Topic("title", "author", password, mock(Category.class));

        Consumer<Topic> consumer = mock(Consumer.class);
        topic.ifRemovable(password, consumer);

        verify(consumer, times(1)).accept(topic);
    }

}