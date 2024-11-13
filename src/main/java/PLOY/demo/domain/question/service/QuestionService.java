package PLOY.demo.domain.question.service;


import PLOY.demo.domain.question.model.entity.Question;

import java.util.List;

public interface QuestionService {
    //문제를 저장하는 로직
    String saveQuestion(Question question);

    //문제 보여주는 로직
    List<Question> getAllQuestions();

    //문제 수정로직
    String editQuestion(Question question);

    //문제생성로직
    String createQuestion(Question question);
}
