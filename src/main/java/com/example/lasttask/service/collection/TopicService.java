package com.example.lasttask.service.collection;

import com.example.lasttask.dto.request.collection.TopicRequestDto;
import com.example.lasttask.dto.response.ApiResponse;
import com.example.lasttask.dto.response.collection.CollectionResponseDto;
import com.example.lasttask.dto.response.collection.TopicResponseDto;
import com.example.lasttask.exception.BadRequestException;
import com.example.lasttask.exception.NotFoundException;
import com.example.lasttask.model.entity.collection.CollectionEntity;
import com.example.lasttask.model.entity.collection.TopicEntity;
import com.example.lasttask.repository.CollectionRepository;
import com.example.lasttask.repository.TopicRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.expression.spel.ast.OpEQ;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class TopicService {

    private final CollectionRepository collectionRepository;
    private final TopicRepository topicRepository;
    private final ModelMapper modelMapper;

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

    public ApiResponse add(TopicRequestDto topicRequestDto){
        checkTopicForNotExist(topicRequestDto.getName());
        TopicEntity topic = modelMapper.map(topicRequestDto, TopicEntity.class);
        topicRepository.save(topic);
        return new ApiResponse(1, "success", null);
    }

    public ApiResponse edit(Long topicId, TopicRequestDto topicRequestDto) {
        TopicEntity topic = checkTopicForExist(topicId);
        topic.setName(topicRequestDto.getName());
        topicRepository.save(topic);
        return new ApiResponse(1, "success", null);
    }

    public ApiResponse delete(Long id){
        TopicEntity topic = checkTopicForExist(id);
        checkTopicFordelete(topic);
        topicRepository.deleteById(id);
        return new ApiResponse(1, "success", null);
    }

    public ApiResponse getAll(){
        List<TopicEntity> topicEntityList = topicRepository.findAll();
        List<TopicResponseDto> topicResponseDtoList = new ArrayList<>();
        topicEntityList.forEach(topicEntity -> {
            TopicResponseDto topicResponseDto = modelMapper.map(topicEntity, TopicResponseDto.class);
            topicResponseDtoList.add(topicResponseDto);
        });
        return new ApiResponse(1, "success", topicResponseDtoList);
    }
}
