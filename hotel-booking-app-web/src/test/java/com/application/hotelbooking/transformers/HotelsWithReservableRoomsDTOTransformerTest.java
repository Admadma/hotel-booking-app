package com.application.hotelbooking.transformers;

import com.application.hotelbooking.domain.RoomType;
import com.application.hotelbooking.dto.*;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class HotelsWithReservableRoomsDTOTransformerTest {
    private static final Class<HotelWithReservableRoomsDTO> HOTEL_WITH_RESERVABLE_ROOMS_DTO_CLASS = HotelWithReservableRoomsDTO.class;
    private static final Class<HotelWithReservableRoomsServiceDTO> HOTEL_WITH_RESERVABLE_ROOMS_SERVICE_DTO_CLASS = HotelWithReservableRoomsServiceDTO.class;
    private static final String HOTEL_NAME = "Hotel name";
    private static final String CITY = "City name";
    private static final String IMAGE_NAME = "image_name.png";
    private static final double AVERAGE_RATING = 5.0;
    private static final TypeMap<HotelWithReservableRoomsDTO, HotelWithReservableRoomsServiceDTO> TYPE_MAP = Mockito.mock(TypeMap.class);
    private static final LocalDate START_DATE = LocalDate.now().plusDays(7);
    private static final LocalDate END_DATE = LocalDate.now().plusDays(14);
    private static UniqueReservableRoomOfHotelDTO UNIQUE_RESERVABLE_ROOM_OF_HOTEL_DTO = UniqueReservableRoomOfHotelDTO.builder()
            .number(1)
            .singleBeds(1)
            .doubleBeds(0)
            .pricePerNight(10)
            .totalPrice(40)
            .roomType(RoomType.SINGLE_ROOM)
            .startDate(START_DATE)
            .endDate(END_DATE)
            .build();
    private static final HotelWithReservableRoomsDTO HOTEL_WITH_RESERVABLE_ROOMS_DTO = HotelWithReservableRoomsDTO.builder()
            .hotelName(HOTEL_NAME)
            .city(CITY)
            .imageName(IMAGE_NAME)
            .averageRating(AVERAGE_RATING)
            .uniqueReservableRoomOfHotelDTOList(List.of(UNIQUE_RESERVABLE_ROOM_OF_HOTEL_DTO))
            .build();
    private static UniqueReservableRoomOfHotelServiceDTO UNIQUE_RESERVABLE_ROOM_OF_HOTEL_SERVICE_DTO = UniqueReservableRoomOfHotelServiceDTO.builder()
            .number(1)
            .singleBeds(1)
            .doubleBeds(0)
            .pricePerNight(10)
            .totalPrice(40)
            .roomType(RoomType.SINGLE_ROOM)
            .startDate(START_DATE)
            .endDate(END_DATE)
            .build();
    private static final HotelWithReservableRoomsServiceDTO HOTEL_WITH_RESERVABLE_ROOMS_SERVICE_DTO = HotelWithReservableRoomsServiceDTO.builder()
            .hotelName(HOTEL_NAME)
            .city(CITY)
            .imageName(IMAGE_NAME)
            .averageRating(AVERAGE_RATING)
            .uniqueReservableRoomOfHotelServiceDTOList(List.of(UNIQUE_RESERVABLE_ROOM_OF_HOTEL_SERVICE_DTO))
            .build();
    private static final List<HotelWithReservableRoomsDTO> HOTEL_WITH_RESERVABLE_ROOMS_DTO_LIST = List.of(HOTEL_WITH_RESERVABLE_ROOMS_DTO);
    private static final List<HotelWithReservableRoomsServiceDTO> HOTEL_WITH_RESERVABLE_ROOMS_SERVICE_DTO_LIST = List.of(HOTEL_WITH_RESERVABLE_ROOMS_SERVICE_DTO);

    @InjectMocks
    private HotelsWithReservableRoomsDTOTransformer hotelsWithReservableRoomsDTOTransformer;

    @Mock
    private ModelMapper modelMapper;

    @Test
    public void testTransformToHotelsWithReservableRoomsDTOsShouldReturnListOfTransformedHotelWithReservableRoomsServiceDTOs(){
        when(modelMapper.map(HOTEL_WITH_RESERVABLE_ROOMS_SERVICE_DTO, HOTEL_WITH_RESERVABLE_ROOMS_DTO_CLASS)).thenReturn(HOTEL_WITH_RESERVABLE_ROOMS_DTO);

        List<HotelWithReservableRoomsDTO> resultHotelWithReservableRoomsDTOs = hotelsWithReservableRoomsDTOTransformer.transformToHotelsWithReservableRoomsDTOs(HOTEL_WITH_RESERVABLE_ROOMS_SERVICE_DTO_LIST);

        verify(modelMapper).map(HOTEL_WITH_RESERVABLE_ROOMS_SERVICE_DTO, HOTEL_WITH_RESERVABLE_ROOMS_DTO_CLASS);
        Assertions.assertThat(resultHotelWithReservableRoomsDTOs).isEqualTo(HOTEL_WITH_RESERVABLE_ROOMS_DTO_LIST);
    }

    @Test
    public void testTransformToHotelWithReservableRoomsServiceDTOsShouldReturnListOfTransformedHotelsWithReservableRoomsDTOs(){
        when(modelMapper.typeMap(HOTEL_WITH_RESERVABLE_ROOMS_DTO_CLASS, HOTEL_WITH_RESERVABLE_ROOMS_SERVICE_DTO_CLASS)).thenReturn(TYPE_MAP);
        when(modelMapper.map(HOTEL_WITH_RESERVABLE_ROOMS_DTO, HOTEL_WITH_RESERVABLE_ROOMS_SERVICE_DTO_CLASS)).thenReturn(HOTEL_WITH_RESERVABLE_ROOMS_SERVICE_DTO);

        List<HotelWithReservableRoomsServiceDTO> resultHotelWithReservableRoomsServiceDTOs = hotelsWithReservableRoomsDTOTransformer.transformToHotelWithReservableRoomsServiceDTOs(HOTEL_WITH_RESERVABLE_ROOMS_DTO_LIST);

        verify(modelMapper).typeMap(HOTEL_WITH_RESERVABLE_ROOMS_DTO_CLASS, HOTEL_WITH_RESERVABLE_ROOMS_SERVICE_DTO_CLASS);
        verify(modelMapper).map(HOTEL_WITH_RESERVABLE_ROOMS_DTO, HOTEL_WITH_RESERVABLE_ROOMS_SERVICE_DTO_CLASS);
        Assertions.assertThat(resultHotelWithReservableRoomsServiceDTOs).isEqualTo(HOTEL_WITH_RESERVABLE_ROOMS_SERVICE_DTO_LIST);
        Assertions.assertThat(resultHotelWithReservableRoomsServiceDTOs.get(0).getUniqueReservableRoomOfHotelServiceDTOList().get(0)).isEqualTo(UNIQUE_RESERVABLE_ROOM_OF_HOTEL_SERVICE_DTO);
    }
}
