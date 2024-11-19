package PLOY.demo.domain.question.service.Impl;

import PLOY.demo.domain.question.model.dto.GeneratedQuestionDTO;
import PLOY.demo.domain.question.model.dto.ResponseGeneratedQuestionDTO;
import PLOY.demo.domain.question.model.entity.QuestionEntity;
import PLOY.demo.domain.question.repository.QuestionRepository;
import PLOY.demo.domain.question.service.AIService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;

@Service
public class AIServiceImpl implements AIService {

    // QuestionRepository 빈을 자동 주입
    @Autowired
    QuestionRepository questionRepository;

    // 문제 ID를 관리하기 위한 변수
    private int questionIdCounter;

    // @PostConstruct: 빈 초기화 후 가장 먼저 호출되는 메서드
    @PostConstruct
    public void init() {
        // JPA를 통해 가장 큰 ID 값을 찾기
        Long maxId = questionRepository.findMaxId();

        // maxId가 null인 경우 첫 번째 ID를 1로 설정, 아니면 maxId + 1로 설정하여 ID 관리
        questionIdCounter = (maxId != null) ? maxId.intValue() + 1 : 1;
    }

    // 1. 파일 업로드 메서드
    @Override
    public String uploadFile(MultipartFile file) throws IOException {
        // 업로드될 파일에 대한 고유한 이름을 생성 (UUID를 사용하여 이름 충돌 방지)
        String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();

        // 파일을 저장할 디렉토리 경로 설정
        String uploadDir = System.getProperty("user.dir") + "/images/uploaded/";

        // 디렉토리가 없으면 생성
        File directory = new File(uploadDir);
        if (!directory.exists() && !directory.mkdirs()) {
            throw new IOException("디렉토리 생성 실패");
        }

        // 업로드된 파일을 저장할 위치 지정
        File destinationFile = new File(uploadDir + fileName);

        // 파일을 지정된 위치에 저장
        file.transferTo(destinationFile);

        // 파일이 성공적으로 저장되었으면 절대 경로를 반환
        if (destinationFile.exists()) {
            return destinationFile.getAbsolutePath();
        } else {
            throw new IOException("파일 저장에 실패했습니다.");
        }
    }

    // 2. AI 분석 메서드
    @Override
    public List<GeneratedQuestionDTO> analyzeFileWithAI(String filePath, int grade, int category, int problemId) {
        String base64Image;

        // 이미지 파일을 Base64 형식으로 변환 (AI 서버로 전송하기 위해)
        try {
            File imageFile = new File(filePath);
            byte[] fileContent = Files.readAllBytes(imageFile.toPath());
            base64Image = Base64.getEncoder().encodeToString(fileContent);
        } catch (IOException e) {
            throw new RuntimeException("이미지 파일을 읽는 중 오류 발생", e);
        }

        // AI 분석을 위한 API 요청 준비 (RestTemplate을 사용하여 HTTP 요청을 보냄)
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // 요청 바디에 Base64로 변환된 이미지를 포함
        Map<String, Object> payload = new HashMap<>();
        payload.put("imgB64", base64Image);

        // Flask 서버로 POST 요청을 보냄 (Flask 서버의 AI를 사용하여 문제를 생성)
        HttpEntity<Map<String, Object>> request = new HttpEntity<>(payload, headers);
        String flaskServerUrl = "http://127.0.0.1:5000/openAI/img";

        try {
            // Flask 서버에서 생성된 문제를 응답받음
            ResponseEntity<String> response = restTemplate.postForEntity(flaskServerUrl, request, String.class);
            String responseText = response.getBody();

            // 응답을 JSON 형태로 파싱하여 GeneratedQuestionDTO 리스트로 변환
            ObjectMapper objectMapper = new ObjectMapper();
            List<GeneratedQuestionDTO> generatedQuestions = objectMapper.readValue(responseText, new TypeReference<List<GeneratedQuestionDTO>>() {});

            // 각 문제에 학년, 카테고리, 문제 ID를 추가
            //이건 프론트들이 알아서 던져줄거임ㅇㅇ
            for (GeneratedQuestionDTO question : generatedQuestions) {
                question.setId(generateQuestionId());  // 문제 ID 자동 생성
                question.setGrade(grade);
                question.setCategory(category);
                question.setProblemId(problemId);
            }

            return generatedQuestions;
        } catch (IOException e) {
            throw new RuntimeException("Flask 서버와의 통신 중 오류 발생", e);
        }
    }

    // 3. 응답 생성 메서드
    @Override
    public ResponseGeneratedQuestionDTO createResponse(List<GeneratedQuestionDTO> generatedQuestions) {
        // 문제 리스트를 포함하여 응답 객체를 생성
        return ResponseGeneratedQuestionDTO.builder()
                .status("success")  // 응답 상태
                .generatedQuestions(generatedQuestions)  // 생성된 문제 리스트
                .build();//빌더패턴
    }

    // 문제 ID를 자동으로 1씩 증가시키는 메서드
    private int generateQuestionId() {
        return questionIdCounter++;  // 문제 ID 증가 후 반환
    }

    // 메인 메서드: 파일 업로드 후 AI 분석 및 응답 반환
    @Override
    public ResponseGeneratedQuestionDTO AISay(MultipartFile file, int grade, int category, int problemId) {
        try {
            // 1. 파일 업로드
            String filePath = uploadFile(file);

            // 2. AI 분석
            List<GeneratedQuestionDTO> generatedQuestions = analyzeFileWithAI(filePath, grade, category, problemId);

            // 3. 응답 생성
            return createResponse(generatedQuestions);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("파일 처리 중 오류 발생", e);
        }
    }
}
