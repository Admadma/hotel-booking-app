package com.application.hotelbooking.dto;

import com.application.hotelbooking.models.RoomType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoomCreationServiceDTO {

    private Long version;
    private int roomNumber;
    private int singleBeds;
    private int doubleBeds;
    private int pricePerNight;
    private RoomType roomType;
    private Long hotelId;

    @Override
    public String toString() {
        return "RoomCreationServiceDTO{" +
                "version=" + version +
                ", roomNumber=" + roomNumber +
                ", singleBeds=" + singleBeds +
                ", doubleBeds=" + doubleBeds +
                ", pricePerNight=" + pricePerNight +
                ", roomType=" + roomType +
                ", hotelId='" + hotelId + '\'' +
                '}';
    }
}
