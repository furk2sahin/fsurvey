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
    @JoinColumn
    @JsonIgnoreProperties("answers")
    private Participant participant;

    @ManyToOne
    @JoinColumn
    @JsonIgnoreProperties("issues")
    private Survey survey;

    @ManyToOne
    @JoinColumn
    @JsonIgnoreProperties({"answers", "options", "survey"})
    private Issue issue;

    @ManyToOne
    @JoinColumn
    @JsonIgnoreProperties({"issue"})
    private Option option;
}
