package project.fsurvey.dtos;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Data
public class SurveyDto {

    @NotBlank(message = "Survey name cannot be empty.")
    @Size(min = 2, message = "Survey name length cannot be less than 2.")
    private String name;

    private String description;

    private List<IssueDto> issue  = new ArrayList<>();
}
