package com.example.lasttask.service;

import com.example.lasttask.exception.BadRequestException;
import com.example.lasttask.exception.NotFoundException;
import com.example.lasttask.model.entity.CommentEntity;
import com.example.lasttask.model.entity.TagEntity;
import com.example.lasttask.model.entity.UserEntity;
import com.example.lasttask.model.entity.collection.CollectionEntity;
import com.example.lasttask.model.entity.collection.FieldEntity;
import com.example.lasttask.model.entity.collection.TopicEntity;
import com.example.lasttask.model.entity.item.ItemEntity;
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
    private final CommentRepository commentRepository;
    private final FieldRepository fieldRepository;
    private final TopicRepository topicRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final TagRepository tagRepository;


    public void checkPermission(Long userId, UserEntity user, CollectionEntity collection, String custom){
        if (!(collection.getUser().getId().equals(userId) || user.getRole().equals(ROLE_ADMIN))){
            throw new BadRequestException("you don't have permission to " + custom + " this collection!");
        }
    }



           /////////////////////       USER          ////////////////////////
    public UserEntity checkUserForExist(Long userId){
        Optional<UserEntity> optionalUser = userRepository.findById(userId);
        if (!optionalUser.isPresent()){
            throw new NotFoundException("User not found with this Id: " + userId);
        }
        return optionalUser.get();
    }





                     /////////////      COLLECTION         ///////////////


    public CollectionEntity checkCollectionForExist(Long collectionId){
        Optional<CollectionEntity> optionalCollection = collectionRepository.findById(collectionId);
        if (!optionalCollection.isPresent()){
            throw new NotFoundException("Collection not found with this Id: " + collectionId);
        }
        return optionalCollection.get();
    }

                //////////////     FIELD        ///////////////////////

    public FieldEntity checkFieldForExist(Long id){
        Optional<FieldEntity> optionalField = fieldRepository.findById(id);
        if (!optionalField.isPresent()){
            throw new NotFoundException("Field not found with this Id: " + id);
        }
        return optionalField.get();
    }


    public void checkFieldForNotExist(String name){
        Optional<FieldEntity> optionalField = fieldRepository.findByName(name);
        if (optionalField.isPresent()){
            throw new NotFoundException("Field is already exist with this Name: " + name);
        }
    }



                    /////////////     TOPIC    ////////////

    public TopicEntity checkTopicForExistByName(String name){
        Optional<TopicEntity> optionalTopic = topicRepository.findByName(name);
        if (!optionalTopic.isPresent()){
            throw new BadRequestException("Topic not found with this Name: " + name);
        }
        return optionalTopic.get();
    }

    public TopicEntity checkTopicForExist(Long id){
        Optional<TopicEntity> optionalTopic = topicRepository.findById(id);
        if (!optionalTopic.isPresent()){
            throw new BadRequestException("Topic not found with this Id: " + id);
        }
        return optionalTopic.get();
    }

    public void checkTopicForNotExist(String name){
        Optional<TopicEntity> optionalTopic = topicRepository.findByName(name);
        if (optionalTopic.isPresent()){
            throw new NotFoundException("Topic is already exist with this Name: " + name);
        }
    }


    public void checkTopicFordelete(TopicEntity topic){
        List<CollectionEntity> collectionEntityList = collectionRepository.findByTopicId(topic.getId());
        if (collectionEntityList.size() > 0){
            throw new BadRequestException("you can't delete this topic because it is being used with '" + collectionEntityList.get(0).getName() + "' collection");
        }
    }







    //////////////////     ITEM        ///////////////////////

    public ItemEntity checkItemForExist(Long id){
        Optional<ItemEntity> optionalItem = itemRepository.findById(id);
        if (!optionalItem.isPresent()){
            throw new NotFoundException("Item not found with this Id: " + id);
        }
        return optionalItem.get();
    }









    ////////////////////       COMMENT    ////////////////////////
    public CommentEntity checkCommentForExist(Long commentId){
        Optional<CommentEntity> optionalComment = commentRepository.findById(commentId);
        if (!optionalComment.isPresent()){
            throw new NotFoundException("Comment not found with this Id: " + commentId);
        }
        return optionalComment.get();
    }



    ////////////////         TAG        //////////////////////


    public TagEntity checkTagForExist(Long id){
        Optional<TagEntity> optionalTag = tagRepository.findById(id);
        if (!optionalTag.isPresent()){
            throw new NotFoundException("Tag not found with this Id: " + id);
        }
        return optionalTag.get();
    }


    public void checkTagForNotExist(String name){
        Optional<TagEntity> optionalTagEntity = tagRepository.findByName(name);
        if (optionalTagEntity.isPresent()){
            throw new BadRequestException("Tag is already exist with this Name: " + name);
        }
    }





}
