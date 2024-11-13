package PLOY.demo.domain.question.service;


import PLOY.demo.domain.question.model.entity.Question;
import org.springframework.web.multipart.MultipartFile;

public interface AIService {
    //가져온 파일로 문제 만들어주는 로직
    Question AISay(MultipartFile file);
}
