package com.application.hotelbooking.services;

import com.application.hotelbooking.domain.RoomModel;
import com.application.hotelbooking.domain.RoomType;
import com.application.hotelbooking.dto.*;
import com.application.hotelbooking.exceptions.InvalidRoomException;
import com.application.hotelbooking.repositories.RoomRepository;
import com.application.hotelbooking.transformers.RoomCreationServiceDTOTransformer;
import com.application.hotelbooking.transformers.RoomTransformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

@Service
public class RoomService {

    private static final Logger LOGGER = LoggerFactory.getLogger(RoomService.class);
    public static final long DEFAULT_STARTING_VERSION = 1l;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private NewReservationService reservationService;

    @Autowired
    private HotelService hotelService;

    @Autowired
    private RoomTransformer roomTransformer;

    @Autowired
    private RoomCreationServiceDTOTransformer roomCreationServiceDTOTransformer;

    public List<RoomModel> getAllRooms(){
        return roomTransformer.transformToRoomModels(roomRepository.findAll());
    }

    public RoomDTO getRoomDTO(Long roomId){
        return roomTransformer.transformToRoomDTO(roomRepository.findById(roomId).get());
    }

    public RoomModel getRoom(Long roomId){
        return roomTransformer.transformToRoomModel(roomRepository.findRoomById(roomId));
    }

    public List<RoomModel> findAllRoomsOfGivenType(String roomType){
        return roomTransformer.transformToRoomModels(roomRepository.findAllByRoomType(RoomType.valueOf(roomType)));
    }

    private List<RoomSearchResultDTO> createRoomSearchResultDTOs(List<Long> roomIds, RoomSearchFormServiceDTO roomSearchFormServiceDTO){
        List<RoomSearchResultDTO> roomSearchResultDTOs = new LinkedList<>();
        for (Long roomId : roomIds) {
            RoomDTO room = getRoomDTO(roomId);
            roomSearchResultDTOs.add(createRoomSearchResultDTO(room, roomSearchFormServiceDTO));
        }
        return roomSearchResultDTOs;
    }

    private static RoomSearchResultDTO createRoomSearchResultDTO(RoomDTO room, RoomSearchFormServiceDTO roomSearchFormServiceDTO) {
        return new RoomSearchResultDTO(room.getRoomNumber(),
                room.getSingleBeds(),
                room.getDoubleBeds(),
                room.getPricePerNight(),
                room.getRoomType(),
                room.getHotel().getHotelName(),
                room.getHotel().getCity(),
                roomSearchFormServiceDTO.getStartDate(),
                roomSearchFormServiceDTO.getEndDate());
    }

    public List<RoomSearchResultDTO> searchRooms(RoomSearchFormServiceDTO roomSearchFormServiceDTO){
        List<Long> roomIds = getRoomsWithConditions(roomSearchFormServiceDTO);
        List<Long> availableRooms = filterAvailableRooms(roomSearchFormServiceDTO, roomIds);

        return createRoomSearchResultDTOs(availableRooms, roomSearchFormServiceDTO);
    }

    private List<Long> filterAvailableRooms(RoomSearchFormServiceDTO roomSearchFormServiceDTO, List<Long> roomIds) {
        return reservationService.filterFreeRooms(roomIds, roomSearchFormServiceDTO.getStartDate(), roomSearchFormServiceDTO.getEndDate());
    }

    private List<Long> getRoomsWithConditions(RoomSearchFormServiceDTO roomSearchFormServiceDTO) {
//        System.out.println(roomRepository.findRoomByRoomNumberAndHotelHotelName(77, "asd Hotel").getRoomNumber());
        LOGGER.info(String.valueOf(roomRepository.findRoomBySingleBedsAndHotelHotelName(0, "asd Hotel").getRoomNumber()));

        return roomRepository.findRoomsWithConditions(roomSearchFormServiceDTO.getSingleBeds(),
                roomSearchFormServiceDTO.getDoubleBeds(),
                roomSearchFormServiceDTO.getRoomType(),
                roomSearchFormServiceDTO.getHotelName(),
                roomSearchFormServiceDTO.getCity());
    }

    public boolean isRoomTypeNotAvailable(String roomType){
        return findAllRoomsOfGivenType(roomType).isEmpty();
    }

    public boolean isRoomNumberFree(int roomNumber){
        return roomRepository.findRoomByRoomNumber(roomNumber).size() == 0;
    }

    public void createRoomFromDTO(RoomCreationServiceDTO roomCreationServiceDTO){
        if (isRoomNumberFree(roomCreationServiceDTO.getRoomNumber())) {
            roomCreationServiceDTO.setVersion(DEFAULT_STARTING_VERSION);
            roomRepository.save(roomCreationServiceDTOTransformer.transformToRoom(roomCreationServiceDTO));
        } else {
            throw new InvalidRoomException("That roomNumber is already taken.");
        }
    }

    public void updateRoomVersion(RoomModel roomModel){
        roomModel.setVersion(roomModel.getVersion() + 1);
        roomRepository.save(roomTransformer.transformToRoom(roomModel));
    }

    public boolean roomVersionMatches(RoomModel roomModel) {
        return getRoom(roomModel.getId()).getVersion().equals(roomModel.getVersion());
    }

//    private Room roomFactoryCreate(String roomType,){
//        Room room;
//        if (roomType == "familyRoom"){
//            room = new FamilyRoom();
//            room.set
//        }
//    }

//    public void createFamilyRoom(int roomNumber, int singleBeds, int doubleBeds) {
//    public void createFamilyRoom(FamilyRoomModel familyRoomModel) {
//        roomRepository.save(familyRoomTransformer.transformToFamilyRoom(familyRoomModel));
//    }
}
