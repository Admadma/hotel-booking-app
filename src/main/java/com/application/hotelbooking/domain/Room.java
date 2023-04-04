package com.application.hotelbooking.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "rooms")
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
