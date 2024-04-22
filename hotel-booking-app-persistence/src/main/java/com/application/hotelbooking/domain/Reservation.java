package com.application.hotelbooking.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "reservations")
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // Used for identification in the context of the database

    @Column(nullable = false, unique = true)
    private UUID uuid; // Used for identification in the context of the application

    @ManyToOne
    @JoinColumn(name = "room_id", nullable = false)
    private Room room;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private LocalDate startDate;
    @Column(nullable = false)
    private LocalDate endDate;
    @Column(nullable = false)
    private int totalPrice;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReservationStatus reservationStatus;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Reservation that = (Reservation) o;
        return totalPrice == that.totalPrice && Objects.equals(id, that.id) && Objects.equals(room.getId(), that.room.getId()) && Objects.equals(user.getId(), that.user.getId()) && Objects.equals(startDate, that.startDate) && Objects.equals(endDate, that.endDate) && Objects.equals(reservationStatus, that.reservationStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, room.getId(), user.getId(), startDate, endDate, totalPrice, reservationStatus);
    }
}
