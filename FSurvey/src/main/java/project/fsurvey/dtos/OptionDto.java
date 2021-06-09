package project.fsurvey.dtos;

import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class OptionDto {

    @NotBlank(message = "Answer cannot be empty.")
    private String answer;

    @NotNull
    @Min(1)
    @Max(value = 5, message = "Max. 5 option can be found in an issue")
    private Integer optionOrder;

    @Min(1)
    @NotNull
    private Long issueId;
}
