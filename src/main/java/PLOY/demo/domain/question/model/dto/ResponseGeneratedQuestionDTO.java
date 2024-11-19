package PLOY.demo.domain.question.model.dto;


import lombok.*;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResponseGeneratedQuestionDTO {
    private String status;//이거 상태
    private List<GeneratedQuestionDTO> generatedQuestions;//이건 문제들
}
