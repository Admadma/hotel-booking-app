package com.application.hotelbooking.services.repositoryservices;

import com.application.hotelbooking.entities.Hotel;
import com.application.hotelbooking.domain.HotelModel;
import com.application.hotelbooking.dto.HotelCreationServiceDTO;
import com.application.hotelbooking.repositories.HotelRepository;
import com.application.hotelbooking.services.repositoryservices.implementations.HotelRepositoryServiceImpl;
import com.application.hotelbooking.transformers.HotelTransformer;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class HotelRepositoryServiceImplTest {

    private static final List<Hotel> EMPTY_LIST_OF_HOTELS = List.of();
    private static final List<HotelModel> EMPTY_LIST_OF_HOTEL_MODELS = List.of();
    private static final String HOTEL_NAME = "Test hotel";
    private static final String HOTEL_NAME_TWO = "Test hotel 2";
    private static final long HOTEL_ID = 1l;
    private static final HotelModel HOTEL_MODEL_ONE = HotelModel.builder().id(HOTEL_ID).hotelName(HOTEL_NAME).build();
    private static final HotelModel HOTEL_MODEL_TWO = HotelModel.builder().hotelName(HOTEL_NAME_TWO).build();
    private static final List<HotelModel> TRANSFORMED_HOTELS = List.of(HOTEL_MODEL_ONE, HOTEL_MODEL_TWO);
    private static final Hotel HOTEL_ONE = Hotel.builder().id(HOTEL_ID).hotelName(HOTEL_NAME).build();
    private static final Hotel HOTEL_TWO = Hotel.builder().hotelName(HOTEL_NAME_TWO).build();
    private static final List<Hotel> FOUND_HOTELS = List.of(HOTEL_ONE, HOTEL_TWO);
    private static final HotelCreationServiceDTO HOTEL_CREATION_SERVICE_DTO = HotelCreationServiceDTO.builder().hotelName(HOTEL_NAME).build();
    private static final Optional<Hotel> OPTIONAL_HOTEL = Optional.of(HOTEL_ONE);
    private static final Optional<Hotel> EMPTY_OPTIONAL_HOTEL = Optional.empty();
    private static final Optional<HotelModel> OPTIONAL_HOTEL_MODEL = Optional.of(HOTEL_MODEL_ONE);
    private static final Optional<HotelModel> EMPTY_OPTIONAL_HOTEL_MODEL = Optional.empty();
    @Mock
    private HotelRepository hotelRepository;

    @Mock
    private HotelTransformer hotelTransformer;

    @InjectMocks
    private HotelRepositoryServiceImpl hotelRepositoryService;

    @Test
    public void testGetHotelByIdShouldReturnOptionalOfHotelModelIfHotelExists(){
        when(hotelRepository.findById(HOTEL_ID)).thenReturn(OPTIONAL_HOTEL);
        when(hotelTransformer.transformToOptionalHotelModel(OPTIONAL_HOTEL)).thenReturn(OPTIONAL_HOTEL_MODEL);

        Optional<HotelModel> resultHotel = hotelRepositoryService.getHotelById(HOTEL_ID);

        verify(hotelRepository).findById(HOTEL_ID);
        verify(hotelTransformer).transformToOptionalHotelModel(OPTIONAL_HOTEL);
        Assertions.assertThat(resultHotel).isNotNull();
        Assertions.assertThat(resultHotel).isNotEmpty();
        Assertions.assertThat(resultHotel.get().getId()).isEqualTo(HOTEL_ID);
    }

    @Test
    public void testGetHotelByIdShouldReturnEmptyOptionalIfHotelDoesNotExist(){
        when(hotelRepository.findById(HOTEL_ID)).thenReturn(EMPTY_OPTIONAL_HOTEL);
        when(hotelTransformer.transformToOptionalHotelModel(EMPTY_OPTIONAL_HOTEL)).thenReturn(EMPTY_OPTIONAL_HOTEL_MODEL);

        Optional<HotelModel> resultHotel = hotelRepositoryService.getHotelById(HOTEL_ID);

        verify(hotelRepository).findById(HOTEL_ID);
        verify(hotelTransformer).transformToOptionalHotelModel(EMPTY_OPTIONAL_HOTEL);
        Assertions.assertThat(resultHotel).isNotNull();
        Assertions.assertThat(resultHotel).isEmpty();
    }

    @Test
    public void testGetAllHotelsShouldReturnSavedHotelsAsListOfHotelModels(){
        when(hotelRepository.findAll()).thenReturn(FOUND_HOTELS);
        when(hotelTransformer.transformToHotelModels(FOUND_HOTELS)).thenReturn(TRANSFORMED_HOTELS);

        List<HotelModel> resultHotels = hotelRepositoryService.getAllHotels();

        verify(hotelRepository).findAll();
        verify(hotelTransformer).transformToHotelModels(FOUND_HOTELS);
        Assertions.assertThat(resultHotels).isNotNull();
        Assertions.assertThat(resultHotels).isNotEmpty();
        Assertions.assertThat(resultHotels).isEqualTo(TRANSFORMED_HOTELS);
    }

    @Test
    public void testGetAllHotelsShouldReturnEmptyListIfNoHotelsSaved(){
        when(hotelRepository.findAll()).thenReturn(EMPTY_LIST_OF_HOTELS);
        when(hotelTransformer.transformToHotelModels(EMPTY_LIST_OF_HOTELS)).thenReturn(EMPTY_LIST_OF_HOTEL_MODELS);

        List<HotelModel> resultHotels = hotelRepositoryService.getAllHotels();

        verify(hotelRepository).findAll();
        verify(hotelTransformer).transformToHotelModels(EMPTY_LIST_OF_HOTELS);
        Assertions.assertThat(resultHotels).isNotNull();
        Assertions.assertThat(resultHotels).isEmpty();
    }

    @Test
    public void testFindHotelByHotelNameShouldReturnOptionalOfHotelModelIfHotelExists(){
        when(hotelRepository.findHotelByHotelName(HOTEL_NAME)).thenReturn(OPTIONAL_HOTEL);
        when(hotelTransformer.transformToOptionalHotelModel(OPTIONAL_HOTEL)).thenReturn(OPTIONAL_HOTEL_MODEL);

        Optional<HotelModel> resultHotel = hotelRepositoryService.findHotelByHotelName(HOTEL_NAME);

        verify(hotelRepository).findHotelByHotelName(HOTEL_NAME);
        verify(hotelTransformer).transformToOptionalHotelModel(OPTIONAL_HOTEL);
        Assertions.assertThat(resultHotel).isNotNull();
        Assertions.assertThat(resultHotel).isNotEmpty();
        Assertions.assertThat(resultHotel.get().getHotelName()).isEqualTo(HOTEL_NAME);
    }

    @Test
    public void testFindHotelByHotelNameShouldReturnEmptyOptionalIfHotelDoesNotExist(){
        when(hotelRepository.findHotelByHotelName(HOTEL_NAME)).thenReturn(EMPTY_OPTIONAL_HOTEL);
        when(hotelTransformer.transformToOptionalHotelModel(EMPTY_OPTIONAL_HOTEL)).thenReturn(EMPTY_OPTIONAL_HOTEL_MODEL);

        Optional<HotelModel> resultHotel = hotelRepositoryService.findHotelByHotelName(HOTEL_NAME);

        verify(hotelRepository).findHotelByHotelName(HOTEL_NAME);
        verify(hotelTransformer).transformToOptionalHotelModel(EMPTY_OPTIONAL_HOTEL);
        Assertions.assertThat(resultHotel).isNotNull();
        Assertions.assertThat(resultHotel).isEmpty();
    }

    @Test
    public void testCreateShouldReturnHotelModelOfSavedHotel() {
        when(hotelTransformer.transformToHotel(HOTEL_CREATION_SERVICE_DTO)).thenReturn(HOTEL_ONE);
        when(hotelRepository.save(HOTEL_ONE)).thenReturn(HOTEL_ONE);
        when(hotelTransformer.transformToHotelModel(HOTEL_ONE)).thenReturn(HOTEL_MODEL_ONE);

        HotelModel savedHotel = hotelRepositoryService.create(HOTEL_CREATION_SERVICE_DTO);

        verify(hotelTransformer).transformToHotel(HOTEL_CREATION_SERVICE_DTO);
        verify(hotelRepository).save(HOTEL_ONE);
        verify(hotelTransformer).transformToHotelModel(HOTEL_ONE);
        Assertions.assertThat(savedHotel).isNotNull();
        Assertions.assertThat(savedHotel.getHotelName()).isEqualTo(HOTEL_NAME);
    }

    @Test
    public void testSaveShouldReturnHotelModelOfSavedHotel() {
        when(hotelTransformer.transformToHotel(HOTEL_MODEL_ONE)).thenReturn(HOTEL_ONE);
        when(hotelRepository.save(HOTEL_ONE)).thenReturn(HOTEL_ONE);
        when(hotelTransformer.transformToHotelModel(HOTEL_ONE)).thenReturn(HOTEL_MODEL_ONE);

        HotelModel savedHotel = hotelRepositoryService.save(HOTEL_MODEL_ONE);

        verify(hotelTransformer).transformToHotel(HOTEL_MODEL_ONE);
        verify(hotelRepository).save(HOTEL_ONE);
        verify(hotelTransformer).transformToHotelModel(HOTEL_ONE);
        Assertions.assertThat(savedHotel).isNotNull();
        Assertions.assertThat(savedHotel.getHotelName()).isEqualTo(HOTEL_NAME);
    }
}
