package com.example.lasttask.model.entity.collection;


import com.example.lasttask.model.entity.UserEntity;
import com.example.lasttask.model.entity.item.ItemEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
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


    public List<String[]> toCSVString(){
        List<String[]> list = new ArrayList<>();
        list.addAll(Collections.singleton(new String[]{"COLLECTION"}));
        String[] csv = new String[5];
        csv[0] = "Id: " +  this.id;
        csv[1] = "Name: " + this.name;
        csv[2] = "Topic: " + this.topic.getName();
        csv[3] = "Description: " + this.description;
        csv[4] = "Number of items: " + this.numberOfItem;
        list.addAll(Collections.singleton(csv));
        list.addAll(Collections.singleton(new String[]{"ITEMS"}));
        return list;
    }

}
