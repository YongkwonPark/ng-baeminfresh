/*
 * 등록하는 모든 topic, post 비밀번호는 password 로 통일한다.
 * 비밀번호는 암호화해서 등록해야하고, password 의 암호화된 값은 '$2a$10$aZJLtvXsECrqJoWgn8vUeuqUhk6u/i3kRj7VTibjheeY6dYBEMzO2' 이다.
 */

INSERT INTO CATEGORY(name, created_at, updated_at) VALUES('Java', CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP());
INSERT INTO CATEGORY(name, created_at, updated_at) VALUES('Kotlin', CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP());


INSERT INTO TOPIC(title, author, password, category_id, created_at, updated_at) VALUES('Java 8은 많이 사용하시나요?', 'arawn', '$2a$10$aZJLtvXsECrqJoWgn8vUeuqUhk6u/i3kRj7VTibjheeY6dYBEMzO2', 1, CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP());
INSERT INTO POST(text, author, password, topic_id, created_at, updated_at) VALUES('저는 써요!!!', 'sonegy', '$2a$10$aZJLtvXsECrqJoWgn8vUeuqUhk6u/i3kRj7VTibjheeY6dYBEMzO2', 1, CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP());
INSERT INTO POST(text, author, password, topic_id, created_at, updated_at) VALUES('Scala 쓰세요~', 'holyeye', '$2a$10$aZJLtvXsECrqJoWgn8vUeuqUhk6u/i3kRj7VTibjheeY6dYBEMzO2', 1, CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP());
INSERT INTO POST(text, author, password, topic_id, created_at, updated_at) VALUES('엑셀과 파워포인트만 쓰고 있습니다.', 'fupfin', '$2a$10$aZJLtvXsECrqJoWgn8vUeuqUhk6u/i3kRj7VTibjheeY6dYBEMzO2', 1, CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP());


INSERT INTO TOPIC(title, author, password, category_id, created_at, updated_at) VALUES('Kotlin으로 만들어진 서비스가 있나요?', 'arawn', '$2a$10$aZJLtvXsECrqJoWgn8vUeuqUhk6u/i3kRj7VTibjheeY6dYBEMzO2', 2, CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP());
INSERT INTO TOPIC(title, author, password, category_id, created_at, updated_at) VALUES('Kotlin으로 웹 애플리케이션을 만들 수 있나요?', 'arawn', '$2a$10$aZJLtvXsECrqJoWgn8vUeuqUhk6u/i3kRj7VTibjheeY6dYBEMzO2', 2, CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP());