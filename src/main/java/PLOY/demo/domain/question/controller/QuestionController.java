package PLOY.demo.domain.question.controller;

import PLOY.demo.domain.question.model.dto.ResponseGeneratedQuestionDTO;
import PLOY.demo.domain.question.service.AIService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
public class QuestionController {

    @Autowired
    private AIService aiService;

    // 파일 업로드 및 AI 분석 처리
    @PostMapping("/api/admin/upload-file")
    public ResponseGeneratedQuestionDTO uploadFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam("grade") int grade,
            @RequestParam("category") int category,
            @RequestParam("problemId") int problemId) {
        try {
            return aiService.AISay(file, grade, category, problemId); // 추가 파라미터 전달
        } catch (IOException e) {
            throw new RuntimeException("파일 처리 중 오류 발생", e);
        }
    }
}
