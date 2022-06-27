package com.example.lasttask.model.entity.collection;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity(name = "field")
public class FieldEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    /**
     * swicht(type)
     * case 0: Integer
     * case 1: String
     * case 2: Date
     * case 3: Boolean
     */
    @Column(nullable = false)
    private int type;

    @Column(nullable = false)
    private LocalDateTime createDate;

    @ManyToOne
    @JsonIgnore
    private CollectionEntity collection;
}
