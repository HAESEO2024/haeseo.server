package PLOY.demo.domain.question.service;

import PLOY.demo.domain.question.model.dto.GeneratedQuestionDTO;
import PLOY.demo.domain.question.model.dto.ResponseGeneratedQuestionDTO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface AIService {

    // 파일 업로드 메서드
    String uploadFile(MultipartFile file) throws IOException;

    // AI 분석 메서드 (파일 경로와 함께 grade, category, problemId 추가)
    List<GeneratedQuestionDTO> analyzeFileWithAI(String filePath, int grade, int category, int problemId);

    // 응답 생성 메서드
    ResponseGeneratedQuestionDTO createResponse(List<GeneratedQuestionDTO> generatedQuestions);

    // 메인 메서드: 파일 업로드 후 AI 분석 및 응답 반환
    ResponseGeneratedQuestionDTO AISay(MultipartFile file, int grade, int category, int problemId) throws IOException;
}
