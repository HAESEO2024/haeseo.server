package PLOY.demo.domain.question.service;


import PLOY.demo.domain.question.model.dto.GeneratedQuestionDTO;
import PLOY.demo.domain.question.model.dto.StatusResponse;

import java.util.List;

public interface QuestionService {
    //문제를 저장하는 로직
    StatusResponse saveQuestion(List<GeneratedQuestionDTO> questions);

    //문제 보여주는 로직
    List<GeneratedQuestionDTO> getAllQuestions();

    //문제 수정로직
    StatusResponse editQuestion(GeneratedQuestionDTO question);

    //문제생성로직
    StatusResponse createQuestion(GeneratedQuestionDTO question);
}
