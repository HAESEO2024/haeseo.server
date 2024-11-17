package PLOY.demo.domain.question.controller;

import PLOY.demo.domain.question.model.dto.response.ResponseGeneratedQuestionDTO;
import PLOY.demo.domain.question.model.entity.Question;
import PLOY.demo.domain.question.service.AIService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
public class QuestionController {
    @Autowired
    private AIService aiService;

    @PostMapping("/api/admin/upload-file")
    public ResponseGeneratedQuestionDTO uploadFile(@RequestParam("file") MultipartFile file){
        try {
            return aiService.AISay(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
