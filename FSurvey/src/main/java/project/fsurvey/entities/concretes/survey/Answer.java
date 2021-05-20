package project.fsurvey.entities.concretes.survey;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import project.fsurvey.entities.concretes.users.Participant;

import javax.persistence.*;

@Data
@Entity
@Table(name = "participant_answers")
public class Answer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "participant_id", nullable = false)
    @JsonIgnoreProperties("answers")
    private Participant participant;

    @ManyToOne
    @JoinColumn(name = "survey_id", nullable = false)
    @JsonIgnoreProperties("issues")
    private Survey survey;

    @ManyToOne
    @JoinColumn(name = "issue_id", nullable = false)
    @JsonIgnoreProperties({"answers", "options", "survey"})
    private Issue issue;

    @ManyToOne
    @JoinColumn(name = "option_id", nullable = false)
    @JsonIgnoreProperties({"issue"})
    private Option option;
}
