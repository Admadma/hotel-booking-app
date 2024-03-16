package com.application.hotelbooking.transformers;

import com.application.hotelbooking.dto.RoomSearchFormDTO;
import com.application.hotelbooking.dto.RoomSearchFormServiceDTO;
import com.application.hotelbooking.dto.ReservableRoomDTO;
import com.application.hotelbooking.dto.ReservableRoomViewDTO;
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

    public ReservableRoomDTO transformToRoomSearchResultDTO(ReservableRoomViewDTO reservableRoomViewDTO){
        return modelMapper.map(reservableRoomViewDTO, ReservableRoomDTO.class);
    }

    public List<ReservableRoomViewDTO> transformToRoomSearchResultViewDTOs(List<ReservableRoomDTO> reservableRoomDTOS){
        return reservableRoomDTOS.stream()
                .map(reservableRoomDTO -> modelMapper.map(reservableRoomDTO, ReservableRoomViewDTO.class))
                .collect(Collectors.toList());
    }
}
