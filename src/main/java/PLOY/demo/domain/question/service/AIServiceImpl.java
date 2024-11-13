package PLOY.demo.domain.question.service;

import PLOY.demo.domain.question.model.entity.Question;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Service
public class AIServiceImpl implements AIService {

    @Override
    public Question AISay(MultipartFile file) {
        // 파일 저장 설정
        String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
        String uploadDir = System.getProperty("user.dir") + "/images/uploaded/"; // 절대 경로 사용

        try {
            // 디렉토리가 없으면 생성
            File directory = new File(uploadDir);
            if (!directory.exists()) {
                directory.mkdirs(); // 디렉토리 생성
            }

            // 파일 저장
            File destinationFile = new File(uploadDir + fileName);
            file.transferTo(destinationFile);

            // 파일이 실제로 저장되었는지 확인
            if (destinationFile.exists()) {
                System.out.println("파일이 성공적으로 저장되었습니다: " + destinationFile.getAbsolutePath());
            } else {
                System.out.println("파일 저장에 실패했습니다.");
            }

            // AI 로직 연결 (예시로 고정된 데이터 반환)
            return Question.builder()
                    .content("태현이 엉덩이")  // AI에서 반환받은 질문 텍스트
                    .correctAnswer("태현이 엉덩이는 빨개")
                    .options(null)  // 객관식 보기 리스트, 주관식일 경우 null
                    .build();

        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("파일 저장에 실패했습니다.", e);
        }
    }
}
