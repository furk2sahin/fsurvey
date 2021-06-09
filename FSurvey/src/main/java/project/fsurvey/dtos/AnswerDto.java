package project.fsurvey.dtos;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
public class AnswerDto {
    @Min(1)
    @NotNull
    private Long participantId;

    @Min(1)
    @NotNull
    private Long surveyId;

    @Min(1)
    @NotNull
    private Long issueId;

    @Min(1)
    @NotNull
    private Long optionId;
}
