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
    @JoinColumn(name = "participant_id")
    @JsonIgnoreProperties({"answers", "username", "password", "role", "authorities", "nationalIdentity",
            "accountNonLocked", "credentialsNonExpired", "accountNonExpired", "enabled"})
    private Participant participant;

    @ManyToOne
    @JoinColumn(name = "survey_id")
    @JsonIgnoreProperties("issues")
    private Survey survey;

    @ManyToOne
    @JoinColumn(name = "issue_id")
    @JsonIgnoreProperties({"answers", "options", "survey"})
    private Issue issue;

    @ManyToOne
    @JoinColumn(name = "option_id")
    @JsonIgnoreProperties({"issue"})
    private Option option;
}
