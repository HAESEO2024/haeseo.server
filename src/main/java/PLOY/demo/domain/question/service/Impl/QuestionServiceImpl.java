package PLOY.demo.domain.question.service.Impl;

import PLOY.demo.domain.question.model.dto.GeneratedQuestionDTO;
import PLOY.demo.domain.question.model.dto.StatusResponse;
import PLOY.demo.domain.question.repository.QuestionRepository;
import PLOY.demo.domain.question.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class QuestionServiceImpl implements QuestionService {

    @Autowired
     QuestionRepository questionRepository;

    @Override
    public StatusResponse saveQuestion(GeneratedQuestionDTO question) {
        return null;
    }

    @Override
    public List<GeneratedQuestionDTO> getAllQuestions() {
        return List.of();
    }

    @Override
    public StatusResponse editQuestion(GeneratedQuestionDTO question) {
        return null;
    }

    @Override
    public StatusResponse createQuestion(GeneratedQuestionDTO question) {
        return null;
    }
}
