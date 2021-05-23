package project.fsurvey.entities.concretes.survey;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "surveys")
@Data
public class Survey {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column
    private String description;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "survey")
    @LazyCollection(LazyCollectionOption.FALSE)
    @JsonIgnoreProperties("survey")
    private List<Issue> issues  = new ArrayList<>();
}
