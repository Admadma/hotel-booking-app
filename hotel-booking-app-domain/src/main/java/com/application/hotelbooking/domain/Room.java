package com.application.hotelbooking.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "rooms")
public class Room {

    public int asd(){
        return 1;
    }

    @Id
    @GeneratedValue
    private Long id;
    private Long version;

    @Column(unique = true, nullable = false)
    private int roomNumber;
    private int singleBeds;
    private int doubleBeds;
    private int pricePerNight;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RoomType roomType;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "room")
    private List<Reservation> reservations;

    @ManyToOne
    private Hotel hotel;
}
