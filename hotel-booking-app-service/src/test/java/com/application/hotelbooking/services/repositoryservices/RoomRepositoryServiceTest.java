package com.application.hotelbooking.services.repositoryservices;

import com.application.hotelbooking.domain.Room;
import com.application.hotelbooking.domain.RoomModel;
import com.application.hotelbooking.repositories.RoomRepository;
import com.application.hotelbooking.services.repositoryservices.implementations.RoomRepositoryServiceImpl;
import com.application.hotelbooking.transformers.RoomTransformer;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RoomRepositoryServiceTest {

    @InjectMocks
    private RoomRepositoryServiceImpl roomRepositoryService;

    @Mock
    private RoomRepository roomRepository;

    @Mock
    private RoomTransformer roomTransformer;

    @Test
    public void testGetRoomByIdShouldReturnOptionalOfRoomModelIfRoomExists(){
        long id = 1l;
        Optional<Room> foundRoom = Optional.of(Room.builder().id(id).build());
        Optional<RoomModel> transformedRoom = Optional.of(RoomModel.builder().id(id).build());
        when(roomRepository.findById(id)).thenReturn(foundRoom);
        when(roomTransformer.transformToOptionalRoomModel(foundRoom)).thenReturn(transformedRoom);

        Optional<RoomModel> resultRoomModel = roomRepositoryService.getRoomById(id);

        verify(roomRepository).findById(id);
        verify(roomTransformer).transformToOptionalRoomModel(foundRoom);
        Assertions.assertThat(resultRoomModel).isNotNull();
        Assertions.assertThat(resultRoomModel).isNotEmpty();
        Assertions.assertThat(resultRoomModel.get().getId()).isEqualTo(id);
    }

    @Test
    public void testGetRoomByIdShouldReturnEmptyOptionalIfRoomDoesNotExist(){
        long id = 1l;
        Optional<Room> noRoom = Optional.empty();
        Optional<RoomModel> transformedNoRoom = Optional.empty();
        when(roomRepository.findById(id)).thenReturn(noRoom);
        when(roomTransformer.transformToOptionalRoomModel(noRoom)).thenReturn(transformedNoRoom);

        Optional<RoomModel> resultRoomModel = roomRepositoryService.getRoomById(id);

        verify(roomRepository).findById(id);
        verify(roomTransformer).transformToOptionalRoomModel(noRoom);
        Assertions.assertThat(resultRoomModel).isNotNull();
        Assertions.assertThat(resultRoomModel).isEmpty();
    }

}
