package sample.domain.forum;

import lombok.Data;
import lombok.Value;

import java.util.function.Function;

/**
 * @author ykpark@woowahan.com
 */
@Value
public class PostCreator implements Function<Topic, Post> {

    private String text;
    private String author;
    private String password;

    @Override
    public Post apply(Topic topic) {
        return new Post(text, author, password, topic);
    }

}
