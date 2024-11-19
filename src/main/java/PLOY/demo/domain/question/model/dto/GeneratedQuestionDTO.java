package PLOY.demo.domain.question.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GeneratedQuestionDTO {
    private int id;
    private int grade;
    private int questionId;
    private int category;
    private int problemId;
    private String questionTitle;
    private String questionText;
    private List<String> options;
    private String description;
    private String type;
    private String answers;
}
