package com.application.hotelbooking.transformers;

import com.application.hotelbooking.domain.Hotel;
import com.application.hotelbooking.domain.HotelModel;
import com.application.hotelbooking.dto.HotelCreationServiceDTO;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)

public class HotelTransformerTest {

    public static final Class<Hotel> HOTEL_CLASS = Hotel.class;
    public static final Hotel HOTEL = new Hotel();
    public static final List<Hotel> HOTEL_LIST = List.of(HOTEL);
    public static final Optional<Hotel> OPTIONAL_HOTEL = Optional.of(new Hotel());
    public static final Optional<Hotel> EMPTY_HOTEL = Optional.empty();
    public static final Optional<HotelModel> OPTIONAL_HOTEL_MODEL = Optional.of(new HotelModel());
    public static final Class<HotelModel> HOTEL_MODEL_CLASS = HotelModel.class;
    public static final HotelModel HOTEL_MODEL = new HotelModel();
    public static final List<HotelModel> HOTEL_MODEL_LIST = List.of(HOTEL_MODEL);
    public static final HotelCreationServiceDTO HOTEL_CREATION_SERVICE_DTO = new HotelCreationServiceDTO();

    @InjectMocks
    private HotelTransformer hotelTransformer;

    @Mock
    private ModelMapper modelMapper;

    @Test
    public void testTransformToHotelShouldReturnTransformedHotel(){
        when(modelMapper.map(HOTEL_CREATION_SERVICE_DTO, HOTEL_CLASS)).thenReturn(HOTEL);

        Hotel resultHotel = hotelTransformer.transformToHotel(HOTEL_CREATION_SERVICE_DTO);

        verify(modelMapper).map(HOTEL_CREATION_SERVICE_DTO, HOTEL_CLASS);
        Assertions.assertThat(resultHotel).isEqualTo(HOTEL);
    }

    @Test
    public void testTransformToHotelModelShouldReturnTransformedHotelModel(){
        when(modelMapper.map(HOTEL, HOTEL_MODEL_CLASS)).thenReturn(HOTEL_MODEL);

        HotelModel resultHotelModel = hotelTransformer.transformToHotelModel(HOTEL);

        verify(modelMapper).map(HOTEL, HOTEL_MODEL_CLASS);
        Assertions.assertThat(resultHotelModel).isNotNull();
        Assertions.assertThat(resultHotelModel).isEqualTo(HOTEL_MODEL);
    }

    @Test
    public void testTransformToOptionalHotelModelShouldReturnOptionalOfPresentHotelModel(){
        when(modelMapper.map(OPTIONAL_HOTEL, HOTEL_MODEL_CLASS)).thenReturn(HOTEL_MODEL);

        Optional<HotelModel> resultHotelModel = hotelTransformer.transformToOptionalHotelModel(OPTIONAL_HOTEL);

        verify(modelMapper).map(OPTIONAL_HOTEL, HOTEL_MODEL_CLASS);
        Assertions.assertThat(resultHotelModel).isNotNull();
        Assertions.assertThat(resultHotelModel).isNotEmpty();
        Assertions.assertThat(resultHotelModel).isEqualTo(OPTIONAL_HOTEL_MODEL);
    }

    @Test
    public void testTransformToOptionalHotelModelShouldReturnEmptyOptionalOfEmptyHotel(){

        Optional<HotelModel> resultHotelModel = hotelTransformer.transformToOptionalHotelModel(EMPTY_HOTEL);

        Assertions.assertThat(resultHotelModel).isNotNull();
        Assertions.assertThat(resultHotelModel).isEmpty();
    }

    @Test
    public void testTransformToHotelModelsShouldReturnOptionalOfPresentHotelModel(){
        when(modelMapper.map(HOTEL, HOTEL_MODEL_CLASS)).thenReturn(HOTEL_MODEL);

        List<HotelModel> resultHotelModels = hotelTransformer.transformToHotelModels(HOTEL_LIST);

        verify(modelMapper).map(HOTEL, HOTEL_MODEL_CLASS);
        Assertions.assertThat(resultHotelModels).isNotNull();
        Assertions.assertThat(resultHotelModels).isNotEmpty();
        Assertions.assertThat(resultHotelModels).isEqualTo(HOTEL_MODEL_LIST);
    }
}
