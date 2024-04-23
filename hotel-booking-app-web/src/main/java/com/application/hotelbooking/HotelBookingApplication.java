package com.application.hotelbooking;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.Clock;

@SpringBootApplication
public class HotelBookingApplication {

	public static void main(String[] args) {
		SpringApplication.run(HotelBookingApplication.class, args);
	}

	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}

	@Bean
	public Clock clock() {
		return Clock.systemDefaultZone();
	}


//	@Bean
//	public JavaMailSender javaMailSender(){
//		return new JavaMailSenderImpl();
//	}

////	//Code used to generate demo data
//	@Bean
//	public CommandLineRunner demo(RoomRepository roomRepository, UserRepository userRepository) {
//		return (args) -> {
////			roomRepository.save(new SingleRoom(101, 1));
////			roomRepository.save(new SingleRoom(102, 1));
////			roomRepository.save(new SingleRoom(103, 1));
////			roomRepository.save(new SingleRoom(104, 1));
////			roomRepository.save(new SingleRoom(111, 1));
////			roomRepository.save(new FamilyRoom(211, 2, 1));
////			roomRepository.save(new FamilyRoom(201, 2, 1));
////			roomRepository.save(new FamilyRoom(202, 3, 1));
////			roomRepository.save(new FamilyRoom(203, 1, 1));
////
////			userRepository.save(new User("First User", "42"));
////			userRepository.save(new User("Second User", "1234"));
////			userRepository.save(new User("Third User", "333"));
////			userRepository.save(new User("Fourth User", "4444"));
//
////			singleRoomRepository.save(new SingleRoom(101, 1));
////			singleRoomRepository.save(new SingleRoom(102, 1));
////			singleRoomRepository.save(new SingleRoom(103, 1));
////			singleRoomRepository.save(new SingleRoom(104, 1));
////			familyRoomRepository.save(new FamilyRoom(201, 2, 1));
////			familyRoomRepository.save(new FamilyRoom(202, 3, 1));
////			familyRoomRepository.save(new FamilyRoom(203, 1, 1));
////
////			userRepository.save(new User("First User", "42"));
////			userRepository.save(new User("Second User", "1234"));
//
////			roomRepository.save(new Room(3, true));
////			roomRepository.save(new Room(4, true));
////			roomRepository.save(new Room(1, true));
////			roomRepository.save(new Room(3, false));
////
////			Room room = new Room(2, true);
////			User user = new User("First User", "42");
////			Reservation reservation = new Reservation(room, user, LocalDate.now(), LocalDate.now().plusDays(7));
////
////			roomRepository.save(room);
////			userRepository.save(user);
////			reservationRepository.save(reservation);
////
////			Room room2 = new Room(4, false);
////			User user2 = new User("Second User", "1234");
////			Reservation reservation2 = new Reservation(room2, user2, LocalDate.now().plusDays(1), LocalDate.now().plusDays(8));
////
////			roomRepository.save(room2);
////			userRepository.save(user2);
////			reservationRepository.save(reservation2);
//		};
//	}

}
