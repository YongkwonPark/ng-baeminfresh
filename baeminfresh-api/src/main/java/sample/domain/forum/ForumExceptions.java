package sample.domain.forum;

import com.woowahan.common.BusinessException;
import com.woowahan.common.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.UUID;

/**
 * @author ykpark@woowahan.com
 */
public class ForumExceptions {

    public static class BadPasswordException extends BusinessException {

        public BadPasswordException() {
            super("비밀번호가 일치하지 않습니다.");
        }

    }

    public static class CategoryNotFoundException extends ResourceNotFoundException {

        final Long categoryId;

        public CategoryNotFoundException(Long categoryId) {
            super(String.format("카테고리(id: %d)를 찾을 수 없습니다.", categoryId));

            this.categoryId = categoryId;
        }

    }

    public static class TopicNotFoundException extends ResourceNotFoundException {

        final UUID topicId;

        public TopicNotFoundException(UUID topicId) {
            super(String.format("주제(id: %s)를 찾을 수 없습니다.", topicId));

            this.topicId = topicId;
        }

    }

    public static class PostAlreadyExistsInTopicException extends BusinessException {

        final UUID topicId;

        public PostAlreadyExistsInTopicException(UUID topicId) {
            super(String.format("삭제를 요청한 주제(id: %s)에 등록된 글이 존재합니다.", topicId));

            this.topicId = topicId;
        }

    }


    private ForumExceptions() { }

}
