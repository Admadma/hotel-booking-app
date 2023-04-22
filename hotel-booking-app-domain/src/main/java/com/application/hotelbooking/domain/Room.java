package com.application.hotelbooking.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="room_type",
        discriminatorType = DiscriminatorType.STRING)
public class Room {

    @Id
    @GeneratedValue
    private Long id;

    private int roomNumber;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "room")
    private List<Reservation> reservations;

    public Room(int roomNumber) {
        this.roomNumber = roomNumber;
    }
}
