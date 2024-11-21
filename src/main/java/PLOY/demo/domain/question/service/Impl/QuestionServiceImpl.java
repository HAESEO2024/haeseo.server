package PLOY.demo.domain.question.service.Impl;

import PLOY.demo.domain.question.model.dto.GeneratedQuestionDTO;
import PLOY.demo.domain.question.model.dto.StatusResponse;
import PLOY.demo.domain.question.model.entity.QuestionEntity;
import PLOY.demo.domain.question.model.entity.QuestionType;
import PLOY.demo.domain.question.repository.QuestionRepository;
import PLOY.demo.domain.question.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class QuestionServiceImpl implements QuestionService {

    @Autowired
    QuestionRepository questionRepository;

    @Override
    public StatusResponse saveQuestion(List<GeneratedQuestionDTO> questions) {
        for (GeneratedQuestionDTO question : questions) {
            System.out.println("question.getId() = " + question.getId());
            questionRepository.save(QuestionEntity.builder()
                    .grade(question.getGrade())
                    .category(question.getCategory())
                    .questionId(question.getId())
                    .problemId(question.getProblemId())
                    .questionTitle(question.getQuestionTitle())
                    .questionText(question.getQuestionText())
                    .options(map(question.getOptions()))  // options 변환
                    .description(question.getDescription())
                    .type(QuestionType.valueOf(question.getType()))
                    .answers(question.getAnswers())
                    .build()
            );
        }

        return StatusResponse.builder()
                .status("success")
                .build();
    }

    @Override
    public List<GeneratedQuestionDTO> getAllQuestions() {
        return List.of();  // 이 부분은 나중에 실제 로직으로 구현해야 합니다
    }

    @Override
    public StatusResponse editQuestion(GeneratedQuestionDTO question) {
        return null;
    }

    @Override
    public StatusResponse createQuestion(GeneratedQuestionDTO question) {
        return null;
    }

    // options 리스트를 Map<String, Object> 형태로 변환
    private Map<String, Object> map(List<String> options) {
        Map<String, Object> optionMap = new HashMap<>();
        if (options != null) {
            for (int i = 0; i < options.size(); i++) {
                // 옵션 항목을 "option1", "option2"와 같이 매핑
                optionMap.put("option" + (i + 1), options.get(i));
            }
        }
        return optionMap;
    }
}
