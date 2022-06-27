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

    private Long type0;
    private String type1;
    private Boolean type2;
    private Date type3;

    public ItemFieldEntity(Long type0, String type1, Boolean type2, Date type3) {
        this.type0 = type0;
        this.type1 = type1;
        this.type2 = type2;
        this.type3 = type3;
    }

    @ManyToOne
    @JsonIgnore
    private ItemEntity item;

    @OneToOne
    @JsonIgnore
    private FieldEntity fieldEntity;

}
