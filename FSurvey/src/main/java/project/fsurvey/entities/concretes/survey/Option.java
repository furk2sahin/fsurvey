package project.fsurvey.entities.concretes.survey;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Entity
@Table(name = "options")
@Data
public class Option {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @NotBlank(message = "Answer cannot be empty.")
    private String answer;

    @Column(nullable = false)
    @Pattern(regexp = "^[12]{1}$",
            message = "Wrong option order format")
    private int optionOrder;

    @ManyToOne
    @JoinColumn(foreignKey = @ForeignKey(name = "fk_option_survey"))
    @JsonIgnoreProperties({"options", "survey", "answers"})
    private Issue issue;
}
