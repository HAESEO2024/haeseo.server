package PLOY.demo.domain.question.service.Impl;

import PLOY.demo.domain.question.model.dto.GeneratedQuestionDTO;
import PLOY.demo.domain.question.model.dto.ResponseGeneratedQuestionDTO;
import PLOY.demo.domain.question.repository.QuestionRepository;
import PLOY.demo.domain.question.service.AIService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
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

    // 자동으로 빈에 등록되어있는 questionRepository를 주입받아 사용
    @Autowired
    private QuestionRepository questionRepository;

    // 1. 파일 업로드 메서드 굳이 없어도됨 확인용
    @Override
    public String uploadFile(MultipartFile file) throws IOException {
        // 업로드할 파일의 이름을 고유하게 생성 (UUID로 랜덤 값 추가)
        String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
        // 파일이 저장될 디렉토리 경로
        String uploadDir = System.getProperty("user.dir") + "/images/uploaded/";

        // 업로드 디렉토리가 존재하지 않으면 생성
        File directory = new File(uploadDir);
        if (!directory.exists() && !directory.mkdirs()) {
            throw new IOException("디렉토리 생성 실패");
        }

        // 업로드할 파일을 지정된 디렉토리에 저장
        File destinationFile = new File(uploadDir + fileName);
        file.transferTo(destinationFile);

        // 파일이 성공적으로 저장되었으면 그 경로 반환, 실패하면 예외 발생
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

        // 파일을 읽어 base64로 인코딩
        try {
            File imageFile = new File(filePath);
            byte[] fileContent = Files.readAllBytes(imageFile.toPath());
            base64Image = Base64.getEncoder().encodeToString(fileContent);
        } catch (IOException e) {
            throw new RuntimeException("이미지 파일을 읽는 중 오류 발생", e);
        }

        // Flask 서버에 요청을 보낼 준비
        RestTemplate restTemplate = new RestTemplate();
        //헤더 설정
        HttpHeaders headers = new HttpHeaders();
        //json으로 하겠다 선언
        headers.setContentType(MediaType.APPLICATION_JSON);

        // 요청할 데이터(payload)에 base64로 인코딩된 이미지 추가
        Map<String, Object> payload = new HashMap<>();
        payload.put("imgB64", base64Image);

        // 요청을 HttpEntity 형태로 포장
        HttpEntity<Map<String, Object>> request = new HttpEntity<>(payload, headers);

        //Flask서버의 url을 입력
        String flaskServerUrl = "http://127.0.0.1:5000/openAI/img";

        // Flask 서버로 POST 요청을 보내고, 응답 받기
        try {
            //응답보내기
            ResponseEntity<String> response = restTemplate.postForEntity(flaskServerUrl, request, String.class);
            //body로 받기
            String responseText = response.getBody();

            // 받은 응답을 JSON으로 변환
            ObjectMapper objectMapper = new ObjectMapper();
            //이거 잭슨인데 자세한건 문의 ㄱ
            List<GeneratedQuestionDTO> generatedQuestions = objectMapper.readValue(responseText, new TypeReference<List<GeneratedQuestionDTO>>() {});

            //자동으로 id할당해주기
            int newQuestionId = generateNextQuestionId();

            // 각 문제에 대한것들 넣어주기
            for (GeneratedQuestionDTO question : generatedQuestions) {
                question.setId(newQuestionId++);
                question.setGrade(grade);
                question.setCategory(category);
                question.setProblemId(problemId);
            }

            // 변환된 문제 리스트 반환
            return generatedQuestions;
        } catch (IOException e) {
            throw new RuntimeException("Flask 서버와의 통신 중 오류 발생", e);
        }
    }

    // 3. 응답 생성 메서드
    @Override
    public ResponseGeneratedQuestionDTO createResponse(List<GeneratedQuestionDTO> generatedQuestions) {
        // 생성된 문제 리스트를 Response 객체로 래핑하여 반환
        return ResponseGeneratedQuestionDTO.builder()
                .status("success")  // 상태는 항상 "success"로 설정
                .generatedQuestions(generatedQuestions)  // 생성된 문제 리스트 포함
                .build();
    }

    // 문제 ID를 자동으로 증가시키는 메서드
    private int generateNextQuestionId() {
        // DB에서 가장 큰 ID 값을 조회
        Long maxId = questionRepository.findMaxId();
        // 가장 큰 ID에 1을 더하여 새로운 문제 ID를 생성
        return (maxId != null ? maxId.intValue() + 1 : 1); // maxId가 null이면 첫 번째 ID는 1로 설정
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
            // 예외 발생 시 에러 로그 출력 후 예외 던짐
            e.printStackTrace();
            throw new RuntimeException("파일 처리 중 오류 발생", e);
        }
    }
}
