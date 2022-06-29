package com.example.lasttask.model.entity.item;

import com.example.lasttask.model.entity.collection.FieldEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity(name = "item_fields")
public class ItemFieldEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String data;

    @ManyToOne
    @JsonIgnore
    private ItemEntity item;

    @OneToOne
    @JsonIgnore
    private FieldEntity fieldEntity;

}
