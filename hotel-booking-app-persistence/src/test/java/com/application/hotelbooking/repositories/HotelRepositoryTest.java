package com.application.hotelbooking.repositories;

import com.application.hotelbooking.entities.Hotel;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

@DataJpaTest
public class HotelRepositoryTest {

    private static final Hotel HOTEL_ONE = Hotel.builder()
            .hotelName("Hotel 1")
            .city("City 1")
            .imageName("image.png")
            .averageRating(0.0)
            .build();
    private static final Hotel HOTEL_TWO = Hotel.builder()
            .hotelName("Hotel 2")
            .city("City 2")
            .imageName("image.png")
            .averageRating(0.0)
            .build();

    private Hotel SAVED_HOTEL_ONE;

    @Autowired
    private HotelRepository hotelRepository;

    @Test
    public void testSaveReturnsSavedHotel(){
        Hotel savedHotel = hotelRepository.save(HOTEL_ONE);

        Assertions.assertThat(savedHotel).isNotNull();
        Assertions.assertThat(savedHotel.getId()).isNotNull();
        Assertions.assertThat(savedHotel.getHotelName()).isEqualTo(HOTEL_ONE.getHotelName());
        Assertions.assertThat(savedHotel.getCity()).isEqualTo(HOTEL_ONE.getCity());
        Assertions.assertThat(savedHotel.getImageName()).isEqualTo(HOTEL_ONE.getImageName());
        Assertions.assertThat(savedHotel.getAverageRating()).isEqualTo(HOTEL_ONE.getAverageRating());
    }

    @Test
    public void testFindByIdReturnsOptionalOfHotelWithProvidedId(){
        Hotel hotel = hotelRepository.save(HOTEL_ONE);

        Optional<Hotel> resultHotel = hotelRepository.findById(hotel.getId());

        Assertions.assertThat(resultHotel).isNotNull();
        Assertions.assertThat(resultHotel).isNotEmpty();
        Assertions.assertThat(resultHotel.get().getHotelName()).isEqualTo(hotel.getHotelName());
        Assertions.assertThat(resultHotel.get().getCity()).isEqualTo(hotel.getCity());
        Assertions.assertThat(resultHotel.get().getImageName()).isEqualTo(hotel.getImageName());
        Assertions.assertThat(resultHotel.get().getAverageRating()).isEqualTo(hotel.getAverageRating());
    }

    @Test
    public void testFindByIdReturnsEmptyOptionalWithNonexistentIdProvided(){

        Optional<Hotel> resultHotel = hotelRepository.findById(1l);

        Assertions.assertThat(resultHotel).isNotNull();
        Assertions.assertThat(resultHotel).isEmpty();
    }

    @Test
    public void testFindAllReturnsAllHotels(){
        Hotel hotel1 = hotelRepository.save(HOTEL_ONE);
        Hotel hotel2 = hotelRepository.save(HOTEL_TWO);


        List<Hotel> resultHotels = hotelRepository.findAll();

        Assertions.assertThat(resultHotels).isNotNull();
        Assertions.assertThat(resultHotels).isNotEmpty();
        Assertions.assertThat(resultHotels).isEqualTo(List.of(hotel1, hotel2));
    }

    @Test
    public void testFindAllReturnsEmptyListIfNoHotelsAdded(){

        List<Hotel> resultHotels = hotelRepository.findAll();

        Assertions.assertThat(resultHotels).isNotNull();
        Assertions.assertThat(resultHotels).isEmpty();
    }

    @Test
    public void testFindHotelByHotelNameReturnsOptionalOfHotelWithProvidedName(){
        Hotel hotel = hotelRepository.save(HOTEL_ONE);

        Optional<Hotel> resultHotel = hotelRepository.findHotelByHotelName(HOTEL_ONE.getHotelName());

        Assertions.assertThat(resultHotel).isNotNull();
        Assertions.assertThat(resultHotel).isNotEmpty();
        Assertions.assertThat(resultHotel.get().getHotelName()).isEqualTo(hotel.getHotelName());
        Assertions.assertThat(resultHotel.get().getCity()).isEqualTo(hotel.getCity());
        Assertions.assertThat(resultHotel.get().getImageName()).isEqualTo(hotel.getImageName());
        Assertions.assertThat(resultHotel.get().getAverageRating()).isEqualTo(hotel.getAverageRating());
    }

    @Test
    public void testFindHotelByHotelNameReturnsEmptyOptionalWithNonexistentNameProvided(){

        Optional<Hotel> resultHotel = hotelRepository.findHotelByHotelName(HOTEL_ONE.getHotelName());

        Assertions.assertThat(resultHotel).isNotNull();
        Assertions.assertThat(resultHotel).isEmpty();
    }
}
