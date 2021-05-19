package project.fsurvey.entities.concretes.survey;

import lombok.Data;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.util.Date;
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

    @Column(nullable = false)
    private int questionCount;

    @Column
    private int descripton;

    @CreatedDate
    private Date createDate;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "survey")
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<Issue> issues;
}
