package com.application.hotelbooking.dto;

import com.application.hotelbooking.domain.RoomType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoomsFilteredDTO {

    private Long id;
    private Integer singleBeds;
    private Integer doubleBeds;
    private RoomType roomType;
    private String city;
    //in progress
}
