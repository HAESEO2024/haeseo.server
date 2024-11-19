package PLOY.demo.domain.question.model.dto;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class StatusResponse {
    //이건 상태
    private String status;
}
