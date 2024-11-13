package PLOY.demo.domain.question.controller;

import PLOY.demo.domain.question.model.entity.Question;
import PLOY.demo.domain.question.service.AIService;
import PLOY.demo.domain.question.service.AIServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class QuestionController {
    @Autowired
    private AIService aiService;

    @PostMapping("/api/admin/upload-file")
    public ResponseEntity<Question> uploadFile(@RequestParam("file") MultipartFile file){
        try {

            // AIService를 사용하여 파일을 처리하고 Question 객체를 반환
            Question question = aiService.AISay(file);
            System.out.println("question = " + question);
            // 성공 시 생성된 Question 객체와 함께 200 응답 반환
            return ResponseEntity.ok(question);

        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }
}
