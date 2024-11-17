package PLOY.demo.domain.question.service;


import PLOY.demo.domain.question.model.dto.response.GeneratedQuestionDTO;
import PLOY.demo.domain.question.model.dto.response.ResponseGeneratedQuestionDTO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;


public interface AIService {

    // 파일 업로드 메서드
    String uploadFile(MultipartFile file) throws IOException;

    // AI 분석 메서드
    List<GeneratedQuestionDTO> analyzeFileWithAI(String filePath);

    // 응답 생성 메서드
    ResponseGeneratedQuestionDTO createResponse(List<GeneratedQuestionDTO> generatedQuestions);

    // 메인 메서드: 파일 업로드 후 AI 분석 및 응답 반환
    ResponseGeneratedQuestionDTO AISay(MultipartFile file) throws IOException;
}
