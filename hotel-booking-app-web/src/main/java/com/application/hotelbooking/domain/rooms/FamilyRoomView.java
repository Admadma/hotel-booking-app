package com.application.hotelbooking.domain.rooms;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FamilyRoomView {

    private int roomNumber;
    private int singleBeds;
    private int doubleBeds;
}
