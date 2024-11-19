package PLOY.demo.domain.question.model.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.Map;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "Questions")
public class QuestionEntity {

    //문제 아이디 autoincrement이기 때문에@GeneratedValue(strategy = GenerationType.IDENTITY)이게 필수임
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    //학년
    @Column(name = "grade", nullable = false)
    private Integer grade;

    //문제 번호 (문제집안에서)
    @Column(name = "question_id", nullable = false, unique = true)
    private Integer questionId;

    //수학인지 뭔지 번호로 이건 우리 시험 번호임
    @Column(name = "category", nullable = false)
    private Integer category;

    //몇번 문제집인지
    @Column(name = "problem_id", nullable = false)
    private Integer problemId;

    //문제의 제목
    @Column(name = "question_title", nullable = false)
    private String questionTitle;

    //문제내용
    @Lob
    @Column(name = "question_text", nullable = false)
    private String questionText;

    //객관식일때만 사용됨
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "options")
    private Map<String, Object> options;

    //설명
    @Lob
    @Column(name = "description")
    private String description;


    //객관식인지 주관식인지 확인해줌
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private QuestionType type;

    //정답을 적어줌
    @Lob
    @Column(name = "answers", nullable = false)
    private String answers;


    //이쯤 됐으면 익숙한 빌더패턴
    @Builder
    public QuestionEntity(Integer grade, Integer questionId, Integer category, Integer problemId, String questionTitle,
                          String questionText, Map<String, Object> options, String description, QuestionType type, String answers) {
        this.grade = grade;
        this.questionId = questionId;
        this.category = category;
        this.problemId = problemId;
        this.questionTitle = questionTitle;
        this.questionText = questionText;
        this.options = options;
        this.description = description;
        this.type = type;
        this.answers = answers;
    }

    //이건 없어도 되는데 내가 보기 편할려고 넣어놓음
    @Override
    public String toString() {
        return "QuestionEntity{" +
                "id=" + id +
                ", questionText='" + questionText + '\'' +
                ", options=" + options +
                ", type=" + type +
                '}';
    }

}

