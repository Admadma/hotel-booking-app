package com.application.hotelbooking.repositories;

import com.application.hotelbooking.domain.Hotel;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

@DataJpaTest
public class HotelRepositoryTest {

    @Autowired
    private HotelRepository hotelRepository;

    @Test
    public void testSaveReturnsSavedHotel(){
        Hotel hotel = Hotel.builder()
                .hotelName("Hotel 1")
                .city("City 1")
                .build();

        Hotel savedHotel = hotelRepository.save(hotel);

        Assertions.assertThat(savedHotel).isNotNull();
        Assertions.assertThat(savedHotel.getId()).isNotNull();
        Assertions.assertThat(savedHotel.getHotelName()).isEqualTo(hotel.getHotelName());
        Assertions.assertThat(savedHotel.getCity()).isEqualTo(hotel.getCity());
    }

    @Test
    public void testFindByIdReturnsOptionalOfHotelWithProvidedId(){
        Hotel hotel = hotelRepository.save(Hotel.builder()
                .hotelName("Hotel 1")
                .city("City 1")
                .build());


        Optional<Hotel> resultHotel = hotelRepository.findById(hotel.getId());

        Assertions.assertThat(resultHotel).isNotNull();
        Assertions.assertThat(resultHotel).isNotEmpty();
        Assertions.assertThat(resultHotel.get()).isEqualTo(hotel);
    }

    @Test
    public void testFindByIdReturnsEmptyOptionalWithNonexistentIdProvided(){

        Optional<Hotel> resultHotel = hotelRepository.findById(1l);

        Assertions.assertThat(resultHotel).isNotNull();
        Assertions.assertThat(resultHotel).isEmpty();
    }

    @Test
    public void testFindAllReturnsAllHotels(){
        Hotel hotel1 = hotelRepository.save(Hotel.builder()
                .hotelName("Hotel 1")
                .city("City 1")
                .build());
        Hotel hotel2 = hotelRepository.save(Hotel.builder()
                .hotelName("Hotel 2")
                .city("City 2")
                .build());


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
        Hotel hotel = hotelRepository.save(Hotel.builder()
                .hotelName("Hotel 1")
                .city("City 1")
                .build());

        Optional<Hotel> resultHotel = hotelRepository.findHotelByHotelName("Hotel 1");

        Assertions.assertThat(resultHotel).isNotNull();
        Assertions.assertThat(resultHotel).isNotEmpty();
        Assertions.assertThat(resultHotel.get()).isEqualTo(hotel);
    }

    @Test
    public void testFindHotelByHotelNameReturnsEmptyOptionalWithNonexistentNameProvided(){

        Optional<Hotel> resultHotel = hotelRepository.findHotelByHotelName("Hotel 1");

        Assertions.assertThat(resultHotel).isNotNull();
        Assertions.assertThat(resultHotel).isEmpty();
    }
}
