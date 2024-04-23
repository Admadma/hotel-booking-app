package com.application.hotelbooking.entities;

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
@Table(name = "rooms", uniqueConstraints = @UniqueConstraint(columnNames = {"room_number", "hotel_id"}))
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long version;

    @Column(name = "room_number", nullable = false)
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
    @JoinColumn(name = "hotel_id", nullable = false)
    private Hotel hotel;

    @Override
    public String toString() {
        return "Room{" +
                "id=" + id +
                ", version=" + version +
                ", roomNumber=" + roomNumber +
                ", singleBeds=" + singleBeds +
                ", doubleBeds=" + doubleBeds +
                ", pricePerNight=" + pricePerNight +
                ", roomType=" + roomType +
                ", hotelId=" + hotel.getId() +
                '}';
    }
}
