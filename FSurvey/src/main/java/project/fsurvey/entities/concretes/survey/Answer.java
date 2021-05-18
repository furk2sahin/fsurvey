package project.fsurvey.entities.concretes.survey;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import project.fsurvey.entities.concretes.survey.Option;
import project.fsurvey.entities.concretes.survey.Issue;
import project.fsurvey.entities.concretes.survey.Survey;

import javax.persistence.*;

@Data
@Entity
@Table(name = "participant_answers")
public class Answer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn
    private Survey survey;

    @ManyToOne
    @JoinColumn
    @JsonIgnoreProperties({"answers", "options", "survey"})
    private Issue issue;

    @ManyToOne
    @JoinColumn
    @JsonIgnoreProperties({"question"})
    private Option option;
}
