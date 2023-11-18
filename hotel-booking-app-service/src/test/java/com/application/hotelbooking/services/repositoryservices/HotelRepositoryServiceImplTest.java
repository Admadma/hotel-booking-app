package com.application.hotelbooking.services.repositoryservices;

import com.application.hotelbooking.domain.Hotel;
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

    @Mock
    private HotelRepository hotelRepository;

    @Mock
    private HotelTransformer hotelTransformer;

    @InjectMocks
    private HotelRepositoryServiceImpl hotelRepositoryService;

    @Test
    public void testGetHotelByIdShouldReturnOptionalOfHotelModelIfHotelExists(){
        long id = 1l;
        Optional<Hotel> foundHotel = Optional.of(Hotel.builder().id(id).build());
        Optional<HotelModel> transformedHotel = Optional.of(HotelModel.builder().id(id).build());
        when(hotelRepository.findById(id)).thenReturn(foundHotel);
        when(hotelTransformer.transformToOptionalHotelModel(foundHotel)).thenReturn(transformedHotel);

        Optional<HotelModel> resultHotel = hotelRepositoryService.getHotelById(id);

        verify(hotelRepository).findById(id);
        verify(hotelTransformer).transformToOptionalHotelModel(foundHotel);
        Assertions.assertThat(resultHotel).isNotNull();
        Assertions.assertThat(resultHotel).isNotEmpty();
        Assertions.assertThat(resultHotel.get().getId()).isEqualTo(id);
    }

    @Test
    public void testGetHotelByIdShouldReturnEmptyOptionalIfHotelDoesNotExist(){
        long id = 1l;
        Optional<Hotel> noHotel = Optional.empty();
        Optional<HotelModel> transformedNoHotel = Optional.empty();
        when(hotelRepository.findById(id)).thenReturn(noHotel);
        when(hotelTransformer.transformToOptionalHotelModel(noHotel)).thenReturn(transformedNoHotel);

        Optional<HotelModel> resultHotel = hotelRepositoryService.getHotelById(id);

        verify(hotelRepository).findById(id);
        verify(hotelTransformer).transformToOptionalHotelModel(noHotel);
        Assertions.assertThat(resultHotel).isNotNull();
        Assertions.assertThat(resultHotel).isEmpty();
    }

    @Test
    public void testGetAllHotelsShouldReturnSavedHotelsAsListOfHotelModels(){
        List<Hotel> foundHotels = List.of(new Hotel(), new Hotel());
        List<HotelModel> transformedHotels = List.of(new HotelModel(), new HotelModel());
        when(hotelRepository.findAll()).thenReturn(foundHotels);
        when(hotelTransformer.transformToHotelModels(foundHotels)).thenReturn(transformedHotels);

        List<HotelModel> resultHotels = hotelRepositoryService.getAllHotels();

        verify(hotelRepository).findAll();
        verify(hotelTransformer).transformToHotelModels(foundHotels);
        Assertions.assertThat(resultHotels).isNotNull();
        Assertions.assertThat(resultHotels).isNotEmpty();
        Assertions.assertThat(resultHotels).isEqualTo(transformedHotels);
    }

    @Test
    public void testGetAllHotelsShouldReturnEmptyListIfNoHotelsSaved(){
        List<Hotel> foundHotels = List.of();
        List<HotelModel> transformedHotels = List.of();
        when(hotelRepository.findAll()).thenReturn(foundHotels);
        when(hotelTransformer.transformToHotelModels(foundHotels)).thenReturn(transformedHotels);

        List<HotelModel> resultHotels = hotelRepositoryService.getAllHotels();

        verify(hotelRepository).findAll();
        verify(hotelTransformer).transformToHotelModels(foundHotels);
        Assertions.assertThat(resultHotels).isNotNull();
        Assertions.assertThat(resultHotels).isEmpty();
    }

    @Test
    public void testFindHotelByHotelNameShouldReturnOptionalOfHotelModelIfHotelExists(){
        String hotelName = "Test hotel";
        Optional<Hotel> foundHotel = Optional.of(Hotel.builder().hotelName(hotelName).build());
        Optional<HotelModel> transformedHotel = Optional.of(HotelModel.builder().hotelName(hotelName).build());
        when(hotelRepository.findHotelByHotelName(hotelName)).thenReturn(foundHotel);
        when(hotelTransformer.transformToOptionalHotelModel(foundHotel)).thenReturn(transformedHotel);

        Optional<HotelModel> resultHotel = hotelRepositoryService.findHotelByHotelName(hotelName);

        verify(hotelRepository).findHotelByHotelName(hotelName);
        verify(hotelTransformer).transformToOptionalHotelModel(foundHotel);
        Assertions.assertThat(resultHotel).isNotNull();
        Assertions.assertThat(resultHotel).isNotEmpty();
        Assertions.assertThat(resultHotel.get().getHotelName()).isEqualTo(hotelName);
    }

    @Test
    public void testFindHotelByHotelNameShouldReturnEmptyOptionalIfHotelDoesNotExist(){
        String hotelName = "Test hotel";
        Optional<Hotel> noHotel = Optional.empty();
        Optional<HotelModel> transformedNoHotel = Optional.empty();
        when(hotelRepository.findHotelByHotelName(hotelName)).thenReturn(noHotel);
        when(hotelTransformer.transformToOptionalHotelModel(noHotel)).thenReturn(transformedNoHotel);

        Optional<HotelModel> resultHotel = hotelRepositoryService.findHotelByHotelName(hotelName);

        verify(hotelRepository).findHotelByHotelName(hotelName);
        verify(hotelTransformer).transformToOptionalHotelModel(noHotel);
        Assertions.assertThat(resultHotel).isNotNull();
        Assertions.assertThat(resultHotel).isEmpty();
    }

    @Test
    public void testSaveReturnsHotelModelOfSavedHotel() {
        HotelCreationServiceDTO hotelCreationServiceDTO = new HotelCreationServiceDTO();
        Hotel hotel = new Hotel();
        HotelModel transformedNoHotel = new HotelModel();
        when(hotelTransformer.transformToHotel(hotelCreationServiceDTO)).thenReturn(hotel);
        when(hotelRepository.save(hotel)).thenReturn(hotel);
        when(hotelTransformer.transformToHotelModel(hotel)).thenReturn(transformedNoHotel);

        HotelModel savedHotel = hotelRepositoryService.save(hotelCreationServiceDTO);

        verify(hotelTransformer).transformToHotel(hotelCreationServiceDTO);
        verify(hotelRepository).save(hotel);
        verify(hotelTransformer).transformToHotelModel(hotel);
        Assertions.assertThat(savedHotel).isNotNull();
        Assertions.assertThat(savedHotel).isEqualTo(transformedNoHotel);
    }
}
