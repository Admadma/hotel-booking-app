package com.application.hotelbooking.transformers;

import com.application.hotelbooking.domain.RoomModel;
import com.application.hotelbooking.domain.RoomView;
import com.application.hotelbooking.dto.RoomSearchFormDTO;
import com.application.hotelbooking.dto.RoomSearchFormServiceDTO;
import com.application.hotelbooking.dto.RoomSearchResultDTO;
import com.application.hotelbooking.dto.RoomSearchResultViewDTO;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RoomSearchDTOTransformer {

    @Autowired
    private ModelMapper modelMapper;

    public RoomSearchFormServiceDTO transformToRoomSearchFormServiceDTO(RoomSearchFormDTO roomSearchFormDTO){
        return modelMapper.map(roomSearchFormDTO, RoomSearchFormServiceDTO.class);
    }

    public RoomSearchResultViewDTO transformToRoomSearchResultViewDTO(RoomSearchResultDTO roomSearchResultDTO){
        return modelMapper.map(roomSearchResultDTO, RoomSearchResultViewDTO.class);
    }

    public RoomSearchResultDTO transformToRoomSearchResultDTO(RoomSearchResultViewDTO roomSearchResultViewDTO){
        return modelMapper.map(roomSearchResultViewDTO, RoomSearchResultDTO.class);
    }

    public List<RoomSearchResultViewDTO> transformToRoomSearchResultViewDTOs(List<RoomSearchResultDTO> RoomSearchResultDTOs){
        return RoomSearchResultDTOs.stream()
                .map(roomSearchResultDTO -> modelMapper.map(roomSearchResultDTO, RoomSearchResultViewDTO.class))
                .collect(Collectors.toList());
    }
}
