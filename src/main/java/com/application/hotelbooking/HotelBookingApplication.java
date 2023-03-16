package com.application.hotelbooking;

import com.application.hotelbooking.domain.Room;
import com.application.hotelbooking.repositories.RoomRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class HotelBookingApplication {

	public static void main(String[] args) {
		SpringApplication.run(HotelBookingApplication.class, args);
	}

//	//Code used to generate demo data
//	@Bean
//	public CommandLineRunner demo(RoomRepository repository) {
//		return (args) -> {
//			repository.save(new Room(2, true));
//			repository.save(new Room(1, true));
//			repository.save(new Room(4, false));
//		};
//	}

}
