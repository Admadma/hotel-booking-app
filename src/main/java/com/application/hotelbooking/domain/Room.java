package com.application.hotelbooking.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public class Room {

    @Id
    @GeneratedValue
    private Long id;

    private int roomNumber;

    public Room(int roomNumber) {
        this.roomNumber = roomNumber;
    }
}
