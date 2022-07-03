package com.example.lasttask.model.entity.collection;


import com.example.lasttask.model.entity.UserEntity;
import com.example.lasttask.model.entity.item.ItemEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity(name = "collection")
public class CollectionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String description;


    @Column(nullable = false)
    private String imageUrl;

    @Column(nullable = false)
    private int numberOfItem = 0;

    @ManyToOne
    @JsonIgnore
    private UserEntity user;

    @ManyToOne
    private TopicEntity topic;

}
