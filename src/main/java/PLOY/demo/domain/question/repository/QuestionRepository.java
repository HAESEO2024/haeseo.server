package PLOY.demo.domain.question.repository;

import PLOY.demo.domain.question.model.entity.QuestionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface QuestionRepository extends JpaRepository<QuestionEntity, Integer> {
    @Query("SELECT MAX(q.id) FROM QuestionEntity q")
    Long findMaxId();
}
