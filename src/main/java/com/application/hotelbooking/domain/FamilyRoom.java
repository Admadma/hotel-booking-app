package com.application.hotelbooking.domain;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class FamilyRoom extends Room{

    private int singleBeds;
    private int doubleBeds;

    public FamilyRoom(int roomNumber, int singleBeds, int doubleBeds) {
        super(roomNumber);
        this.singleBeds = singleBeds;
        this.doubleBeds = doubleBeds;
    }
}
