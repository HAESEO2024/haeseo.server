package PLOY.demo.domain.question.controller;

import PLOY.demo.domain.question.model.dto.GeneratedQuestionDTO;
import PLOY.demo.domain.question.model.dto.ResponseGeneratedQuestionDTO;
import PLOY.demo.domain.question.model.dto.StatusResponse;
import PLOY.demo.domain.question.service.AIService;
import PLOY.demo.domain.question.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class QuestionController {

    @Autowired
    private AIService aiService;

    @Autowired
    private QuestionService questionService;

    // 파일 업로드 및 AI 분석 처리
    @PostMapping("/upload-file")
    public ResponseGeneratedQuestionDTO uploadFile(
            @RequestPart("file") MultipartFile file,
            @RequestPart("grade") int grade,
            @RequestPart("category") int category,
            @RequestPart("problemId") int problemId) {
        try {
            return aiService.AISay(file, grade, category, problemId); // 추가 파라미터 전달
        } catch (IOException e) {
            throw new RuntimeException("파일 처리 중 오류 발생", e);
        }
    }

    @PostMapping("/save")
    public StatusResponse save(@RequestBody List<GeneratedQuestionDTO> generatedQuestions) {
        System.out.println(generatedQuestions); // 데이터 확인
        return questionService.saveQuestion(generatedQuestions);
    }


}
