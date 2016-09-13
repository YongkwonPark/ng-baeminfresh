package com.woowahan.sample.modules.forum;

import com.woowahan.baeminfresh.common.BusinessException;
import com.woowahan.baeminfresh.common.ResourceNotFoundException;

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

        final Long topicId;

        public TopicNotFoundException(Long topicId) {
            super(String.format("%d 번 주제를 찾을 수 없습니다.", topicId));

            this.topicId = topicId;
        }

    }


    private ForumExceptions() { }

}
