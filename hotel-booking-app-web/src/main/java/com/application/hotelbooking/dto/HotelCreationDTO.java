package com.application.hotelbooking.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HotelCreationDTO {
    @Size(min = 2, message = "{admin.hotel.validation.name.size.too_short}")
    private String hotelName;
    @NotEmpty(message = "{admin.hotel.validation.city.empty}")
    private String city;
    private MultipartFile multipartFile;
}
