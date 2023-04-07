package com.application.hotelbooking.domain;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@MappedSuperclass
public class Room {

    @Id
    @GeneratedValue
    private Long id;

    private int roomNumber;

    public Room(int roomNumber) {
        this.roomNumber = roomNumber;
    }
}
