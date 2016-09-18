package sample.domain.forum;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * @author ykpark@woowahan.com
 */
public interface CategoryRepository extends JpaRepository<Category, Long> {

    Optional<Category> findById(Long categoryId);

}
