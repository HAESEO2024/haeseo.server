package PLOY.demo.domain.question.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GeneratedQuestionDTO {
    private int id;//고유번호
    private int grade;//학년
    private int questionId;//문제집 안에서 몇번 문제인지.
    private int category;//카테고리 정하기
    private int problemId;//문제집 보기
    private String questionTitle;//문제 제목
    private String questionText;//문제 내용
    private List<String> options;//선지 굳이 없어도 됨
    private String description;//문제에 대한 설명
    private String type;//주관식인지 객관식인지
    private String answers;//정답
}
