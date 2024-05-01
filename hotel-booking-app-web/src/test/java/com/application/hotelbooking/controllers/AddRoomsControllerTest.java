package com.application.hotelbooking.controllers;

import com.application.hotelbooking.views.RoomType;
import com.application.hotelbooking.models.HotelModel;
import com.application.hotelbooking.views.HotelView;
import com.application.hotelbooking.models.RoomModel;
import com.application.hotelbooking.dto.RoomCreationDTO;
import com.application.hotelbooking.dto.RoomCreationServiceDTO;
import com.application.hotelbooking.security.SecurityConfiguration;
import com.application.hotelbooking.services.RoomCreationService;
import com.application.hotelbooking.services.repositoryservices.HotelRepositoryService;
import com.application.hotelbooking.transformers.HotelViewTransformer;
import com.application.hotelbooking.transformers.RoomCreationDTOTransformer;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Import(SecurityConfiguration.class)
@WebMvcTest(AddRoomsController.class)
public class AddRoomsControllerTest {

    private static final com.application.hotelbooking.models.RoomType FAMILY_ROOM_MODEL = com.application.hotelbooking.models.RoomType.FAMILY_ROOM;
    private static final com.application.hotelbooking.views.RoomType FAMILY_ROOM_VIEW = com.application.hotelbooking.views.RoomType.FAMILY_ROOM;
    private static final RoomCreationDTO ROOM_CREATION_DTO = new RoomCreationDTO(1, 1, 1, FAMILY_ROOM_VIEW, 1L);
    private static final RoomCreationServiceDTO ROOM_CREATION_SERVICE_DTO = new RoomCreationServiceDTO(1L, 1, 1, 1, 1, FAMILY_ROOM_MODEL, 1L);
    private static final RoomModel ROOM_MODEL = new RoomModel();
    private static final RoomCreationDTO EMPTY_ROOM_CREATION_DTO = new RoomCreationDTO();
    private static final RoomCreationDTO ROOM_CREATION_DTO_WITH_FIVE_INVALID_FIELDS = new RoomCreationDTO(-1, -1, -1, null, null);
    private static final HotelModel HOTEL_MODEL = HotelModel.builder().hotelName("Test Hotel").city("Test City").build();
    private static final HotelView HOTEL_VIEW = HotelView.builder().hotelName("Test Hotel").city("Test City").build();
    private static final List<HotelModel> HOTEL_MODEL_LIST = List.of(HOTEL_MODEL);
    private static final List<HotelView> HOTEL_VIEW_LIST = List.of(HOTEL_VIEW);
    private static final List<String> LIST_OF_ROOM_TYPES = Arrays.stream(RoomType.values()).map(roomType -> roomType.name()).collect(Collectors.toList());

    @MockBean
    private RoomCreationService roomService;

    @MockBean
    private HotelRepositoryService hotelRepositoryService;

    @MockBean
    private RoomCreationDTOTransformer roomCreationDTOTransformer;

    @MockBean
    private HotelViewTransformer hotelViewTransformer;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser(authorities = "ADMIN")
    public void testAdminUserCanNavigateToAddRoomsPageAndSessionAndModelAttributesAdded() throws Exception {
        when(hotelRepositoryService.getAllHotels()).thenReturn(HOTEL_MODEL_LIST);
        when(hotelViewTransformer.transformToHotelViews(HOTEL_MODEL_LIST)).thenReturn(HOTEL_VIEW_LIST);

        mockMvc.perform(MockMvcRequestBuilders.get("/hotelbooking/admin/add-rooms"))
                .andExpect(status().isOk())
                .andExpect(view().name("addrooms"))
                .andExpect(model().attributeExists("roomCreationDTO"))
                .andExpect(model().attribute("roomCreationDTO", EMPTY_ROOM_CREATION_DTO))
                .andExpect(request().sessionAttribute("hotels", HOTEL_VIEW_LIST))
                .andExpect(request().sessionAttribute("roomTypes", LIST_OF_ROOM_TYPES));

        verify(hotelRepositoryService).getAllHotels();
        verify(hotelViewTransformer).transformToHotelViews(HOTEL_MODEL_LIST);
    }

    @Test
    @WithMockUser(authorities = "USER")
    public void testNonAdminUserForbiddenToNavigateToAddRoomsPage() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/hotelbooking/admin/add-rooms"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(authorities = "USER")
    public void testCreateNewRoomForbiddenForNonAdminUser() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/hotelbooking/admin/add-rooms/create-new-room")
                        .flashAttr("roomCreationDTO", Matchers.any(RoomCreationDTO.class)))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    public void testCreateNewRoomShouldReturnToAddRoomsPageWithErrorIfBindingResultHasErrors() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/hotelbooking/admin/add-rooms/create-new-room")
                        .flashAttr("roomCreationDTO", ROOM_CREATION_DTO_WITH_FIVE_INVALID_FIELDS))
                .andExpect(status().isOk())
                .andExpect(model().attributeDoesNotExist("successMessage"))
                .andExpect(view().name("addrooms"))
                .andExpect(model().attribute("roomCreationDTO", ROOM_CREATION_DTO_WITH_FIVE_INVALID_FIELDS))
                .andExpect(model().attributeErrorCount("roomCreationDTO", 5));
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    public void testCreateNewRoomShouldRejectRoomIfAnyOtherErrorHappenedDuringRoomCreationAndReturnToAddRoomsPageWithGlobalError() throws Exception {
        when(roomCreationDTOTransformer.transformToRoomCreationServiceDTO(ROOM_CREATION_DTO)).thenReturn(ROOM_CREATION_SERVICE_DTO);
        when(roomService.createRoomFromDTO(ROOM_CREATION_SERVICE_DTO)).thenThrow(DataIntegrityViolationException.class);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/hotelbooking/admin/add-rooms/create-new-room")
                        .flashAttr("roomCreationDTO", ROOM_CREATION_DTO))
                .andExpect(status().isOk())
                .andExpect(model().attributeDoesNotExist("successMessage"))
                .andExpect(model().attribute("roomCreationDTO", ROOM_CREATION_DTO))
                .andExpect(view().name("addrooms"));

        verify(roomCreationDTOTransformer).transformToRoomCreationServiceDTO(ROOM_CREATION_DTO);
        verify(roomService).createRoomFromDTO(ROOM_CREATION_SERVICE_DTO);
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    public void testCreateNewRoomShouldAddSuccessMessageAttributeAndReturnToAddRoomsPageIfNoErrorOccurred() throws Exception {
        when(roomCreationDTOTransformer.transformToRoomCreationServiceDTO(ROOM_CREATION_DTO)).thenReturn(ROOM_CREATION_SERVICE_DTO);
        when(roomService.createRoomFromDTO(ROOM_CREATION_SERVICE_DTO)).thenReturn(ROOM_MODEL);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/hotelbooking/admin/add-rooms/create-new-room")
                        .flashAttr("roomCreationDTO", ROOM_CREATION_DTO))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("successMessage"))
                .andExpect(model().attribute("roomCreationDTO", ROOM_CREATION_DTO))
                .andExpect(view().name("addrooms"));

        verify(roomCreationDTOTransformer).transformToRoomCreationServiceDTO(ROOM_CREATION_DTO);
        verify(roomService).createRoomFromDTO(ROOM_CREATION_SERVICE_DTO);
    }
}
