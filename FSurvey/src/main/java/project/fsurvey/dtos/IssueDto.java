package project.fsurvey.dtos;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Data
public class IssueDto {

    @Min(1)
    @NotBlank
    private Integer issueOrder;

    @NotBlank(message = "Question cannot be empty.")
    @Size(min = 6)
    private String question;

    @Min(1)
    @NotBlank
    private Long surveyId;

    private List<OptionDto> options = new ArrayList<>();
}
