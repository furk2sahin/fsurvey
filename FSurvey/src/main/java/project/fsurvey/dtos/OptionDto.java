package project.fsurvey.dtos;

import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@Data
public class OptionDto {

    @NotBlank(message = "Answer cannot be empty.")
    private String answer;

    @NotBlank
    @Min(1)
    @Max(value = 5, message = "Max. 5 option can be found in an issue")
    private Integer optionOrder;

    @Min(1)
    @NotBlank
    private Long issueId;
}
