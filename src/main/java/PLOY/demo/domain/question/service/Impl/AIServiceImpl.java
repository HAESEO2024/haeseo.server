package PLOY.demo.domain.question.service.Impl;

import PLOY.demo.domain.question.model.dto.response.GeneratedQuestionDTO;
import PLOY.demo.domain.question.model.dto.response.ResponseGeneratedQuestionDTO;
import PLOY.demo.domain.question.service.AIService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.*;

@Service
public class AIServiceImpl implements AIService {

    // 1. 파일 업로드 메서드
    @Override
    public String uploadFile(MultipartFile file) throws IOException {
        String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
        String uploadDir = System.getProperty("user.dir") + "/images/uploaded/";

        File directory = new File(uploadDir);
        if (!directory.exists() && !directory.mkdirs()) {
            throw new IOException("디렉토리 생성 실패");
        }

        File destinationFile = new File(uploadDir + fileName);
        file.transferTo(destinationFile);

        if (destinationFile.exists()) {
            return destinationFile.getAbsolutePath();
        } else {
            throw new IOException("파일 저장에 실패했습니다.");
        }
    }

    // 2. AI 분석 메서드
    @Override
    public List<GeneratedQuestionDTO> analyzeFileWithAI(String filePath) {
        String base64Image;

        // 이미지 파일을 Base64로 변환
        try {
            File imageFile = new File(filePath);
            byte[] fileContent = java.nio.file.Files.readAllBytes(imageFile.toPath());
            base64Image = Base64.getEncoder().encodeToString(fileContent);
        } catch (IOException e) {
            throw new RuntimeException("이미지 파일을 읽는 중 오류 발생", e);
        }

        // API 요청 준비
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // JSON 요청 바디 설정
        Map<String, Object> payload = new HashMap<>();
        payload.put("imgB64", base64Image);

        // Flask 서버로 POST 요청 보내기
        HttpEntity<Map<String, Object>> request = new HttpEntity<>(payload, headers);
        String flaskServerUrl = "http://127.0.0.1:5000/openAI/img";

        try {
            ResponseEntity<String> response = restTemplate.postForEntity(flaskServerUrl, request, String.class);
            String responseText = response.getBody();
            System.out.println("Flask 서버 응답: " + responseText);

            // 응답 처리
            // JSON 문자열을 GeneratedQuestionDTO 목록으로 변환
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(responseText, new TypeReference<List<GeneratedQuestionDTO>>() {});
        } catch (IOException e) {
            throw new RuntimeException("Flask 서버와의 통신 중 오류 발생", e);
        }
    }

    // 3. 응답 생성 메서드
    @Override
    public ResponseGeneratedQuestionDTO createResponse(List<GeneratedQuestionDTO> generatedQuestions) {
        return ResponseGeneratedQuestionDTO.builder()
                .status("success")
                .generatedQuestions(generatedQuestions)
                .build();
    }

    // 메인 메서드: 파일 업로드 후 AI 분석 및 응답 반환
    @Override
    public ResponseGeneratedQuestionDTO AISay(MultipartFile file) {
        try {
            // 1. 파일 업로드
            String filePath = uploadFile(file);

            // 2. AI 분석
            List<GeneratedQuestionDTO> generatedQuestions = analyzeFileWithAI(filePath);

            // 3. 응답 생성
            return createResponse(generatedQuestions);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("파일 처리 중 오류 발생", e);
        }
    }

}