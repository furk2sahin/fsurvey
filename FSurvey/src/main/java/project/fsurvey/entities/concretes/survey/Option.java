package project.fsurvey.entities.concretes.survey;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "options")
@Data
public class Option {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String answer;

    @Column(nullable = false)
    private int optionOrder;

    @ManyToOne
    @JoinColumn(foreignKey = @ForeignKey(name = "fk_option_survey"))
    @JsonIgnoreProperties({"options", "survey", "answers"})
    private Issue issue;
}
