package com.example.lasttask.service;

import com.example.lasttask.exception.BadRequestException;
import com.example.lasttask.exception.NotFoundException;
import com.example.lasttask.model.entity.UserEntity;
import com.example.lasttask.model.entity.collection.CollectionEntity;
import com.example.lasttask.model.entity.collection.FieldEntity;
import com.example.lasttask.model.entity.collection.TopicEntity;
import com.example.lasttask.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static com.example.lasttask.model.enums.RoleEnum.ROLE_ADMIN;

@Service
@RequiredArgsConstructor
public class CheckService {

    private final CollectionRepository collectionRepository;
    private final ItemFieldRepository itemFieldRepository;
    private final CommentRepository commentRepository;
    private final FieldRepository fieldRepository;
    private final TopicRepository topicRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final TagRepository tagRepository;

    public UserEntity checkUserForExist(Long userId){
        Optional<UserEntity> optionalUser = userRepository.findById(userId);
        if (!optionalUser.isPresent()){
            throw new NotFoundException("User not found with this Id: " + userId);
        }
        return optionalUser.get();
    }



    public CollectionEntity checkCollectionForExist(Long collectionId){
        Optional<CollectionEntity> optionalCollection = collectionRepository.findById(collectionId);
        if (!optionalCollection.isPresent()){
            throw new NotFoundException("Collection not found with this Id: " + collectionId);
        }
        return optionalCollection.get();
    }

    public void checkPermission(Long userId, UserEntity user, CollectionEntity collection, String custom){
        if (!(collection.getUser().getId().equals(userId) || user.getRole().equals(ROLE_ADMIN))){
            throw new BadRequestException("you don't have permission to " + custom + " this collection!");
        }
    }


    public TopicEntity checkTopicForExist(String  name){
        Optional<TopicEntity> optionalTopic = topicRepository.findByName(name);
        if (!optionalTopic.isPresent()){
            throw new BadRequestException("Topic not found with this Name: " + name);
        }
        return optionalTopic.get();
    }



    public void checkFieldForNotExist(String name){
        Optional<FieldEntity> optionalField = fieldRepository.findByName(name);
        if (optionalField.isPresent()){
            throw new NotFoundException("Field is already exist with this Name: " + name);
        }
    }

    public FieldEntity checkFieldForExist(Long id){
        Optional<FieldEntity> optionalField = fieldRepository.findById(id);
        if (!optionalField.isPresent()){
            throw new NotFoundException("Field not found with this Id: " + id);
        }
        return optionalField.get();
    }

    private void checkTopicForNotExist(String name){
        Optional<TopicEntity> optionalTopic = topicRepository.findByName(name);
        if (optionalTopic.isPresent()){
            throw new NotFoundException("Topic is already exist with this Name: " + name);
        }
    }

    private TopicEntity checkTopicForExist(Long id){
        Optional<TopicEntity> optionalTopic = topicRepository.findById(id);
        if (!optionalTopic.isPresent()){
            throw new NotFoundException("Topic not found with this Id: " + id);
        }
        return optionalTopic.get();
    }

    private void checkTopicFordelete(TopicEntity topic){
        List<CollectionEntity> collectionEntityList = collectionRepository.findByTopicId(topic.getId());
        if (collectionEntityList.size() > 0){
            throw new BadRequestException("you can't delete this topic because it is being used with '" + collectionEntityList.get(0).getName() + "' collection");
        }
    }


}
