package project.fsurvey.dtos;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@Data
public class AnswerDto {
    @Min(1)
    @NotBlank
    private Long participantId;

    @Min(1)
    @NotBlank
    private Long surveyId;

    @Min(1)
    @NotBlank
    private Long issueId;

    @Min(1)
    @NotBlank
    private Long optionId;
}
