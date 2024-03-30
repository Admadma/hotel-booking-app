package com.application.hotelbooking.services.implementations;

import com.application.hotelbooking.domain.RoomModel;
import com.application.hotelbooking.dto.*;
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

    private List<ReservableRoomDTO> createRoomSearchResultDTOs(List<Long> roomIds, RoomSearchFormServiceDTO roomSearchFormServiceDTO){
        List<ReservableRoomDTO> reservableRoomDTOS = new LinkedList<>();
        for (Long roomId : roomIds) {
            RoomModel room = roomRepositoryService.getRoomById(roomId).get();
            reservableRoomDTOS.add(createReservableRoomDTO(room, roomSearchFormServiceDTO));
        }
        return reservableRoomDTOS;
    }

    private ReservableRoomDTO createReservableRoomDTO(RoomModel room, RoomSearchFormServiceDTO roomSearchFormServiceDTO) {
        return new ReservableRoomDTO(room.getRoomNumber(),
                room.getSingleBeds(),
                room.getDoubleBeds(),
                reservationService.calculateTotalPrice(
                        roomSearchFormServiceDTO.getStartDate(),
                        roomSearchFormServiceDTO.getEndDate(),
                        room.getPricePerNight()),
                room.getRoomType(),
                room.getHotel().getHotelName(),
                room.getHotel().getCity(),
                room.getHotel().getImageName(),
                roomSearchFormServiceDTO.getStartDate(),
                roomSearchFormServiceDTO.getEndDate());
    }

    private List<Long> filterAvailableRooms(RoomSearchFormServiceDTO roomSearchFormServiceDTO, List<Long> roomIds) {
        return reservationService.filterFreeRooms(roomIds, roomSearchFormServiceDTO.getStartDate(), roomSearchFormServiceDTO.getEndDate());
    }

    public List<ReservableRoomDTO> searchRooms(RoomSearchFormServiceDTO roomSearchFormServiceDTO){
        List<Long> roomIds = roomRepositoryService.getRoomsWithConditions(roomSearchFormServiceDTO);
        List<Long> availableRoomsIds = filterAvailableRooms(roomSearchFormServiceDTO, roomIds);

        return createRoomSearchResultDTOs(availableRoomsIds, roomSearchFormServiceDTO);
    }

    @Override
    public List<HotelWithReservableRoomsServiceDTO> searchHotelsWithReservableRooms(RoomSearchFormServiceDTO roomSearchFormServiceDTO) {
        List<Long> roomIds = roomRepositoryService.getRoomsWithConditions(roomSearchFormServiceDTO);
        List<Long> availableRoomsIds = filterAvailableRooms(roomSearchFormServiceDTO, roomIds);

        return assignUniqueRoomsToHotels(availableRoomsIds, roomSearchFormServiceDTO);
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
                            uniqueReservableRoomOfHotelServiceDTOList
                           ));
        }

        // if (list contains roomModel.getHotel){
        //  if (this hotel doesn't already have a room that equals the currently observed RoomModel){
        //      add it to that room list of that hotel
        //   }
        // else {
        //   add hotel of roomModel
        //   add roomModel to that hotel
        // }

    }

    private UniqueReservableRoomOfHotelServiceDTO buildUniqueReservableRoomOfHotelServiceDTO(RoomModel roomModel, RoomSearchFormServiceDTO roomSearchFormServiceDTO) {
        UniqueReservableRoomOfHotelServiceDTO uniqueReservableRoomOfHotelServiceDTO = UniqueReservableRoomOfHotelServiceDTO.builder()
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

    public boolean isEndDateAfterStartDate(LocalDate startDate, LocalDate endDate){
        return endDate.isAfter(startDate);
    }
}
