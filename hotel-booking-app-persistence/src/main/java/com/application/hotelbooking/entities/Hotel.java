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
@Table(name = "hotels")
public class Hotel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String hotelName;
    @Column(nullable = false)
    private String city;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "hotel")
    private List<Room> rooms;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "hotel")
    private List<Review> reviews;

    @Column(nullable = false)
    private Double averageRating = 0.0;

    private String imageName;

}
