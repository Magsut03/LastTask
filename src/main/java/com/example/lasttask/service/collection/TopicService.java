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
import com.example.lasttask.service.CheckService;
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
    private final CheckService checkService;


    public ApiResponse add(TopicRequestDto topicRequestDto){
        checkService.checkTopicForNotExist(topicRequestDto.getName());
        TopicEntity topic = modelMapper.map(topicRequestDto, TopicEntity.class);
        topicRepository.save(topic);
        return new ApiResponse(1, "success", null);
    }

    public ApiResponse edit(Long topicId, TopicRequestDto topicRequestDto) {
        TopicEntity topic = checkService.checkTopicForExist(topicId);
        topic.setName(topicRequestDto.getName());
        topicRepository.save(topic);
        return new ApiResponse(1, "success", null);
    }

    public ApiResponse delete(Long id){
        TopicEntity topic = checkService.checkTopicForExist(id);
        checkService.checkTopicFordelete(topic);
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
