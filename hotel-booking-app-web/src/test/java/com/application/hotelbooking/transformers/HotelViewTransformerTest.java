package com.application.hotelbooking.transformers;

import com.application.hotelbooking.models.HotelModel;
import com.application.hotelbooking.domain.HotelView;
import com.application.hotelbooking.dto.HotelCreationDTO;
import com.application.hotelbooking.dto.HotelCreationServiceDTO;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class HotelViewTransformerTest {

    private static final HotelCreationDTO HOTEL_CREATION_DTO = new HotelCreationDTO();
    private static final HotelCreationServiceDTO HOTEL_CREATION_SERVICE_DTO = new HotelCreationServiceDTO();
    private static final Class<HotelCreationServiceDTO> HOTEL_CREATION_SERVICE_DTO_CLASS = HotelCreationServiceDTO.class;
    private static final Class<HotelView> HOTEL_VIEW_CLASS = HotelView.class;
    private static final HotelModel HOTEL_MODEL = new HotelModel();
    private static final List<HotelModel> HOTEL_MODEL_LIST = List.of(HOTEL_MODEL);
    private static final HotelView HOTEL_VIEW = new HotelView();
    private static final List<HotelView> HOTEL_VIEW_LIST = List.of(HOTEL_VIEW);

    @InjectMocks
    private HotelViewTransformer hotelViewTransformer;

    @Mock
    private ModelMapper modelMapper;

    @Test
    public void testTransformToHotelCreationServiceDTOShouldReturnTransformedHotelCreationDTO(){
        when(modelMapper.map(HOTEL_CREATION_DTO, HOTEL_CREATION_SERVICE_DTO_CLASS)).thenReturn(HOTEL_CREATION_SERVICE_DTO);

        HotelCreationServiceDTO resultHotelCreationServiceDTO = hotelViewTransformer.transformToHotelCreationServiceDTO(HOTEL_CREATION_DTO, "image_name");

        verify(modelMapper).map(HOTEL_CREATION_DTO, HOTEL_CREATION_SERVICE_DTO_CLASS);
        Assertions.assertThat(resultHotelCreationServiceDTO).isEqualTo(HOTEL_CREATION_SERVICE_DTO);
    }

    @Test
    public void testTransformToHotelViewsShouldReturnListOfTransformedHotelModels(){
        when(modelMapper.map(HOTEL_MODEL, HOTEL_VIEW_CLASS)).thenReturn(HOTEL_VIEW);

        List<HotelView> resultHotelViews = hotelViewTransformer.transformToHotelViews(HOTEL_MODEL_LIST);

        verify(modelMapper).map(HOTEL_MODEL, HOTEL_VIEW_CLASS);
        Assertions.assertThat(resultHotelViews).isNotNull();
        Assertions.assertThat(resultHotelViews).isNotEmpty();
        Assertions.assertThat(resultHotelViews).isEqualTo(HOTEL_VIEW_LIST);
    }
}
