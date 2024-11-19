package PLOY.demo.domain.question.model.dto;


import lombok.*;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResponseGeneratedQuestionDTO {
    private String status;
    private List<GeneratedQuestionDTO> generatedQuestions;
}