package com.application.hotelbooking.dto;

import com.application.hotelbooking.domain.RoomType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Objects;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UniqueReservableRoomOfHotelServiceDTO {

    private int number;
    private int singleBeds;
    private int doubleBeds;
    private int totalPrice;
    private RoomType roomType;
    private LocalDate startDate;
    private LocalDate endDate;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UniqueReservableRoomOfHotelServiceDTO that = (UniqueReservableRoomOfHotelServiceDTO) o;
        return singleBeds == that.singleBeds && doubleBeds == that.doubleBeds && totalPrice == that.totalPrice && roomType == that.roomType && Objects.equals(startDate, that.startDate) && Objects.equals(endDate, that.endDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(singleBeds, doubleBeds, totalPrice, roomType, startDate, endDate);
    }
}
