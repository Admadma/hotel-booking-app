package com.application.hotelbooking.domain;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@DiscriminatorValue("singleRoom")
public class SingleRoom extends Room{

    private int singleBeds;

    public SingleRoom(int roomNumber, int singleBeds) {
        super(roomNumber);
        this.singleBeds = singleBeds;
    }
}
