package PLOY.demo.domain.question.model.entity;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Entity
@Setter
@NoArgsConstructor
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String content;

    @ElementCollection
    private List<String> options;  // 객관식 문제의 보기 목록

    private String correctAnswer;  // 정답

    @Builder
    public Question(String content, List<String> options, String correctAnswer) {
        this.content = content;
        this.options = options;
        this.correctAnswer = correctAnswer;
    }
}
