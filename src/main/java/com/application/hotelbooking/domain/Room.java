package com.application.hotelbooking.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Room {

    @Id
    @GeneratedValue
    private Long id;
    private int numberOfBeds;
    private boolean hasBalcony;

    public Room(int numberOfBeds, boolean hasBalcony) {
        this.numberOfBeds = numberOfBeds;
        this.hasBalcony = hasBalcony;
    }
}
