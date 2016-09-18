package sample.domain.forum;

import com.woowahan.common.BusinessException;
import com.woowahan.common.ResourceNotFoundException;

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
            super(String.format("%d 번 주제를 찾을 수 없습니다.", topicId));

            this.topicId = topicId;
        }

    }


    private ForumExceptions() { }

}
