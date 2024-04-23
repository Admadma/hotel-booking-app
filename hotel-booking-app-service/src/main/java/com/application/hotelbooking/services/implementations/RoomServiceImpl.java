package com.application.hotelbooking.services.implementations;

import com.application.hotelbooking.models.RoomModel;
import com.application.hotelbooking.dto.*;
import com.application.hotelbooking.services.AvailableRoomsFilterService;
import com.application.hotelbooking.services.ReservationService;
import com.application.hotelbooking.services.RoomService;
import com.application.hotelbooking.services.repositoryservices.RoomRepositoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Service
public class RoomServiceImpl implements RoomService {

    private static final Logger LOGGER = LoggerFactory.getLogger(RoomServiceImpl.class);

    @Autowired
    private RoomRepositoryService roomRepositoryService;

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private AvailableRoomsFilterService availableRoomsFilterService;

    private List<Long> filterAvailableRooms(RoomSearchFormServiceDTO roomSearchFormServiceDTO, List<Long> roomIds) {
        return availableRoomsFilterService.filterFreeRooms(roomIds, roomSearchFormServiceDTO.getStartDate(), roomSearchFormServiceDTO.getEndDate());
    }

    private UniqueReservableRoomOfHotelServiceDTO buildUniqueReservableRoomOfHotelServiceDTO(RoomModel roomModel, RoomSearchFormServiceDTO roomSearchFormServiceDTO) {
        UniqueReservableRoomOfHotelServiceDTO uniqueReservableRoomOfHotelServiceDTO = UniqueReservableRoomOfHotelServiceDTO.builder()
                .number(roomModel.getRoomNumber()) // Despite the UniqueReservableRoomOfHotelServiceDTO not containing actual rooms, but their common attributes, I still use the roomNumber of the first room added to the list in order to easily look up the room attributes later.
                .singleBeds(roomModel.getSingleBeds())
                .doubleBeds(roomModel.getDoubleBeds())
                .totalPrice(reservationService.calculateTotalPrice(
                        roomSearchFormServiceDTO.getStartDate(),
                        roomSearchFormServiceDTO.getEndDate(),
                        roomModel.getPricePerNight()))
                .roomType(roomModel.getRoomType())
                .startDate(roomSearchFormServiceDTO.getStartDate())
                .endDate(roomSearchFormServiceDTO.getEndDate())
                .build();
        return uniqueReservableRoomOfHotelServiceDTO;
    }

    private void addToHotelsListIfNotPresentYet(RoomModel roomModel, List<HotelWithReservableRoomsServiceDTO> hotelsWithReservableRoomsServiceDTO, UniqueReservableRoomOfHotelServiceDTO uniqueReservableRoomOfHotelServiceDTO) {
        Optional<HotelWithReservableRoomsServiceDTO> optionalHotelWithReservableRoomsServiceDTO = hotelsWithReservableRoomsServiceDTO.stream().filter(hotel -> hotel.getHotelName().equals(roomModel.getHotel().getHotelName())).findAny();

        if (optionalHotelWithReservableRoomsServiceDTO.isPresent()){
            HotelWithReservableRoomsServiceDTO hotelWithReservableRoomsServiceDTO = optionalHotelWithReservableRoomsServiceDTO.get();
            if (hotelWithReservableRoomsServiceDTO.getUniqueReservableRoomOfHotelServiceDTOList().stream().filter(room -> room.equals(uniqueReservableRoomOfHotelServiceDTO)).count() == 0){
                hotelWithReservableRoomsServiceDTO.getUniqueReservableRoomOfHotelServiceDTOList().add(uniqueReservableRoomOfHotelServiceDTO);
            }
        } else {
            List<UniqueReservableRoomOfHotelServiceDTO> uniqueReservableRoomOfHotelServiceDTOList = new LinkedList<>();
            uniqueReservableRoomOfHotelServiceDTOList.add(uniqueReservableRoomOfHotelServiceDTO);

            hotelsWithReservableRoomsServiceDTO.add(
                    new HotelWithReservableRoomsServiceDTO(
                            roomModel.getHotel().getHotelName(),
                            roomModel.getHotel().getCity(),
                            roomModel.getHotel().getImageName(),
                            roomModel.getHotel().getAverageRating(),
                            uniqueReservableRoomOfHotelServiceDTOList
                    ));
        }
    }

    private List<HotelWithReservableRoomsServiceDTO> assignUniqueRoomsToHotels(List<Long> availableRoomsIds, RoomSearchFormServiceDTO roomSearchFormServiceDTO) {
        List<HotelWithReservableRoomsServiceDTO> hotelsWithReservableRoomsServiceDTO = new LinkedList<>();
        for (Long availableRoomsId : availableRoomsIds){
            RoomModel roomModel = roomRepositoryService.getRoomById(availableRoomsId).get();
            addToHotelsListIfNotPresentYet(
                    roomModel,
                    hotelsWithReservableRoomsServiceDTO,
                    buildUniqueReservableRoomOfHotelServiceDTO(roomModel, roomSearchFormServiceDTO));
        }
        return hotelsWithReservableRoomsServiceDTO;
    }

    public List<HotelWithReservableRoomsServiceDTO> searchHotelsWithReservableRooms(RoomSearchFormServiceDTO roomSearchFormServiceDTO) {
        List<Long> roomIds = roomRepositoryService.getRoomsWithConditions(roomSearchFormServiceDTO);
        List<Long> availableRoomsIds = filterAvailableRooms(roomSearchFormServiceDTO, roomIds);

        return assignUniqueRoomsToHotels(availableRoomsIds, roomSearchFormServiceDTO);
    }

    public boolean isEndDateAfterStartDate(LocalDate startDate, LocalDate endDate){
        return endDate.isAfter(startDate);
    }
}
