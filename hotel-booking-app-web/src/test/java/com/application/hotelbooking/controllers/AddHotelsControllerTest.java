package com.application.hotelbooking.controllers;

import com.application.hotelbooking.dto.HotelCreationDTO;
import com.application.hotelbooking.dto.HotelCreationServiceDTO;
import com.application.hotelbooking.exceptions.InvalidHotelException;
import com.application.hotelbooking.security.SecurityConfiguration;
import com.application.hotelbooking.services.HotelService;
import com.application.hotelbooking.services.imagehandling.FileSystemStorageService;
import com.application.hotelbooking.services.imagehandling.StorageException;
import com.application.hotelbooking.services.imagehandling.StorageService;
import com.application.hotelbooking.transformers.HotelViewTransformer;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.multipart.MultipartFile;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Import({SecurityConfiguration.class, FileSystemStorageService.class})
@WebMvcTest(AddHotelsController.class)
public class AddHotelsControllerTest {

    private static final String IMAGE_NAME = "image_name";
    private static final byte[] FILE_CONTENT = "Test file content".getBytes();
    private static final String FILE_NAME = "test_file.PNG";
    private static final String FILE_TYPE = "image/PNG";
    private static final String FILE_PARAM_NAME = "file";
    private static final MultipartFile MULTIPART_FILE = new MockMultipartFile(FILE_PARAM_NAME, FILE_NAME, FILE_TYPE, FILE_CONTENT);
    private HotelCreationDTO HOTEL_CREATION_DTO = new HotelCreationDTO("Test Hotel", "Test City", MULTIPART_FILE);
    private HotelCreationDTO EMPTY_HOTEL_CREATION_DTO = new HotelCreationDTO();
    private HotelCreationServiceDTO HOTEL_CREATION_SERVICE_DTO = new HotelCreationServiceDTO("Test Hotel", "Test City", IMAGE_NAME);
    private HotelCreationDTO HOTEL_CREATION_DTO_WITH_TWO_INVALID_FIELDS = new HotelCreationDTO("A", "", MULTIPART_FILE);

    @MockBean
    private HotelService hotelService;

    @MockBean
    private HotelViewTransformer hotelViewTransformer;

    @MockBean
    private StorageService storageService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser(authorities = "ADMIN")
    public void testAdminUserCanNavigateToAddHotelsPage() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/hotelbooking/admin/addHotels"))
                .andExpect(status().isOk())
                .andExpect(view().name("addhotels"))
                .andExpect(model().attributeExists("hotelCreationDTO"))
                .andExpect(model().attribute("hotelCreationDTO", EMPTY_HOTEL_CREATION_DTO));
    }

    @Test
    @WithMockUser(authorities = "USER")
    public void testNonAdminUserForbiddenToNavigateToAddHotelsPage() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/hotelbooking/admin/addHotels"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(authorities = "USER")
    public void testCreateNewHotelForbiddenForNonAdminUser() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/hotelbooking/admin/create-new-hotel")
                        .flashAttr("hotelCreationDTO", Matchers.any(HotelCreationDTO.class)))
                .andExpect(status().isForbidden());

    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    public void testCreateNewHotelShouldReturnToAddHotelsPageWithErrorIfBindingResultHasErrors() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/hotelbooking/admin/create-new-hotel")
                        .flashAttr("hotelCreationDTO", HOTEL_CREATION_DTO_WITH_TWO_INVALID_FIELDS))
                .andExpect(status().isOk())
                .andExpect(model().attributeDoesNotExist("successMessage"))
                .andExpect(view().name("addhotels"))
                .andExpect(model().attribute("hotelCreationDTO", HOTEL_CREATION_DTO_WITH_TWO_INVALID_FIELDS))
                .andExpect(model().attributeErrorCount("hotelCreationDTO", 2));
    }
    @Test
    @WithMockUser(authorities = "ADMIN")
    public void testCreateNewHotelShouldRejectMultipartFileIfStorageExceptionWasThrownAndReturnToAddHotelsPageWithErrorCode() throws Exception {
        when(storageService.store(MULTIPART_FILE)).thenThrow(StorageException.class);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/hotelbooking/admin/create-new-hotel")
                        .flashAttr("hotelCreationDTO", HOTEL_CREATION_DTO))
                .andExpect(status().isOk())
                .andExpect(model().attributeDoesNotExist("successMessage"))
                .andExpect(model().attribute("hotelCreationDTO", HOTEL_CREATION_DTO))
                .andExpect(model().attributeHasFieldErrorCode("hotelCreationDTO", "multipartFile", "admin.hotel.validation.image.save.error"))
                .andExpect(view().name("addhotels"));

        verify(storageService).store(MULTIPART_FILE);
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    public void testCreateNewHotelShouldRejectTakenHotelNameAndReturnToAddHotelsPageWithErrorCode() throws Exception {
        when(storageService.store(MULTIPART_FILE)).thenReturn(IMAGE_NAME);
        when(hotelViewTransformer.transformToHotelCreationServiceDTO(HOTEL_CREATION_DTO, IMAGE_NAME)).thenReturn(HOTEL_CREATION_SERVICE_DTO);
        when(hotelService.createHotel(HOTEL_CREATION_SERVICE_DTO)).thenThrow(InvalidHotelException.class);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/hotelbooking/admin/create-new-hotel")
                        .flashAttr("hotelCreationDTO", HOTEL_CREATION_DTO))
                .andExpect(status().isOk())
                .andExpect(model().attributeDoesNotExist("successMessage"))
                .andExpect(model().attribute("hotelCreationDTO", HOTEL_CREATION_DTO))
                .andExpect(model().attributeHasFieldErrorCode("hotelCreationDTO", "hotelName", "admin.hotel.validation.hotelname.taken"))
                .andExpect(view().name("addhotels"));

        verify(storageService).store(MULTIPART_FILE);
        verify(hotelViewTransformer).transformToHotelCreationServiceDTO(HOTEL_CREATION_DTO, IMAGE_NAME);
        verify(hotelService).createHotel(HOTEL_CREATION_SERVICE_DTO);
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    public void testCreateNewHotelShouldRejectHotelIfAnyOtherErrorHappenedDuringSavingAndReturnToAddHotelsPageWithGlobalError() throws Exception {
        when(storageService.store(MULTIPART_FILE)).thenReturn(IMAGE_NAME);
        when(hotelViewTransformer.transformToHotelCreationServiceDTO(HOTEL_CREATION_DTO, IMAGE_NAME)).thenReturn(HOTEL_CREATION_SERVICE_DTO);
        when(hotelService.createHotel(HOTEL_CREATION_SERVICE_DTO)).thenThrow(DataIntegrityViolationException.class); // Example error that might occur during JpaRepository.save

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/hotelbooking/admin/create-new-hotel")
                        .flashAttr("hotelCreationDTO", HOTEL_CREATION_DTO))
                .andExpect(status().isOk())
                .andExpect(model().attributeDoesNotExist("successMessage"))
                .andExpect(model().attribute("hotelCreationDTO", HOTEL_CREATION_DTO))
                .andExpect(model().attributeErrorCount("hotelCreationDTO", 1))
                .andExpect(model().errorCount(1))
                .andExpect(view().name("addhotels"));

        verify(storageService).store(MULTIPART_FILE);
        verify(hotelViewTransformer).transformToHotelCreationServiceDTO(HOTEL_CREATION_DTO, IMAGE_NAME);
        verify(hotelService).createHotel(HOTEL_CREATION_SERVICE_DTO);
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    public void testCreateNewHotelShouldAddSuccessMessageAttributeAndReturnToAddHotelsPage() throws Exception {
        when(storageService.store(MULTIPART_FILE)).thenReturn(IMAGE_NAME);
        when(hotelViewTransformer.transformToHotelCreationServiceDTO(HOTEL_CREATION_DTO, IMAGE_NAME)).thenReturn(HOTEL_CREATION_SERVICE_DTO);
        when(hotelService.createHotel(HOTEL_CREATION_SERVICE_DTO)).thenReturn(null);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/hotelbooking/admin/create-new-hotel")
                        .flashAttr("hotelCreationDTO", HOTEL_CREATION_DTO))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("successMessage"))
                .andExpect(model().attribute("hotelCreationDTO", HOTEL_CREATION_DTO))
                .andExpect(model().attributeErrorCount("hotelCreationDTO", 0))
                .andExpect(model().errorCount(0))
                .andExpect(view().name("addhotels"));

        verify(storageService).store(MULTIPART_FILE);
        verify(hotelViewTransformer).transformToHotelCreationServiceDTO(HOTEL_CREATION_DTO, IMAGE_NAME);
        verify(hotelService).createHotel(HOTEL_CREATION_SERVICE_DTO);
    }
}
