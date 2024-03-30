package com.application.hotelbooking.dto;

import com.application.hotelbooking.domain.RoomType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Objects;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UniqueReservableRoomOfHotelServiceDTO {

    private int singleBeds;
    private int doubleBeds;
    private int totalPrice;
    private RoomType roomType;
    private LocalDate startDate;
    private LocalDate endDate;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UniqueReservableRoomOfHotelServiceDTO that = (UniqueReservableRoomOfHotelServiceDTO) o;
        return singleBeds == that.singleBeds && doubleBeds == that.doubleBeds && totalPrice == that.totalPrice && roomType == that.roomType && Objects.equals(startDate, that.startDate) && Objects.equals(endDate, that.endDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(singleBeds, doubleBeds, totalPrice, roomType, startDate, endDate);
    }
}
//
//[
//        ReservableRoomViewDTO(version=null, roomNumber=1, singleBeds=1, doubleBeds=0, totalPrice=30, roomType=FAMILY_ROOM, hotelName=Hotel 1, city=Debrecen, imageName=da11728d-4c83-4a57-930c-d8e85925cc77.png, startDate=2024-04-01, endDate=2024-04-04),
//        ReservableRoomViewDTO(version=null, roomNumber=2, singleBeds=1, doubleBeds=0, totalPrice=30, roomType=FAMILY_ROOM, hotelName=Hotel 1, city=Debrecen, imageName=da11728d-4c83-4a57-930c-d8e85925cc77.png, startDate=2024-04-01, endDate=2024-04-04),
//        ReservableRoomViewDTO(version=null, roomNumber=1, singleBeds=1, doubleBeds=0, totalPrice=30, roomType=FAMILY_ROOM, hotelName=Hotel 2, city=Debrecen, imageName=02e7052d-2a3e-42e1-a20e-9c07d3513899.png, startDate=2024-04-01, endDate=2024-04-04),
//        ReservableRoomViewDTO(version=null, roomNumber=1, singleBeds=1, doubleBeds=0, totalPrice=30, roomType=FAMILY_ROOM, hotelName=Hotel 3, city=Budapest, imageName=c04aa952-a12f-4c8d-bf38-a7ed757f4e0f.png, startDate=2024-04-01, endDate=2024-04-04)]
//
//[
//        ReservableRoomViewDTO(version=null, roomNumber=1, singleBeds=1, doubleBeds=0, totalPrice=30, roomType=FAMILY_ROOM, hotelName=Hotel 1, city=Debrecen, imageName=da11728d-4c83-4a57-930c-d8e85925cc77.png, startDate=2024-04-01, endDate=2024-04-04),
//        ReservableRoomViewDTO(version=null, roomNumber=2, singleBeds=1, doubleBeds=0, totalPrice=30, roomType=FAMILY_ROOM, hotelName=Hotel 1, city=Debrecen, imageName=da11728d-4c83-4a57-930c-d8e85925cc77.png, startDate=2024-04-01, endDate=2024-04-04),
//        ReservableRoomViewDTO(version=null, roomNumber=1, singleBeds=1, doubleBeds=0, totalPrice=30, roomType=FAMILY_ROOM, hotelName=Hotel 2, city=Debrecen, imageName=02e7052d-2a3e-42e1-a20e-9c07d3513899.png, startDate=2024-04-01, endDate=2024-04-04),
//        ReservableRoomViewDTO(version=null, roomNumber=1, singleBeds=1, doubleBeds=0, totalPrice=30, roomType=FAMILY_ROOM, hotelName=Hotel 3, city=Budapest, imageName=c04aa952-a12f-4c8d-bf38-a7ed757f4e0f.png, startDate=2024-04-01, endDate=2024-04-04)]
//
//
//        result: [
//        HotelWithReservableRoomsServiceDTO(hotelName=Hotel 1, city=Debrecen, imageName=da11728d-4c83-4a57-930c-d8e85925cc77.png, uniqueReservableRoomOfHotelServiceDTOList=[
//                UniqueReservableRoomOfHotelServiceDTO(roomNumber=1, singleBeds=1, doubleBeds=0, totalPrice=20, roomType=FAMILY_ROOM, startDate=2024-04-01, endDate=2024-04-03)]),
//        HotelWithReservableRoomsServiceDTO(hotelName=Hotel 2, city=Debrecen, imageName=02e7052d-2a3e-42e1-a20e-9c07d3513899.png, uniqueReservableRoomOfHotelServiceDTOList=[
//                UniqueReservableRoomOfHotelServiceDTO(roomNumber=1, singleBeds=1, doubleBeds=0, totalPrice=20, roomType=FAMILY_ROOM, startDate=2024-04-01, endDate=2024-04-03)]),
//        HotelWithReservableRoomsServiceDTO(hotelName=Hotel 3, city=Budapest, imageName=c04aa952-a12f-4c8d-bf38-a7ed757f4e0f.png, uniqueReservableRoomOfHotelServiceDTOList=[
//                UniqueReservableRoomOfHotelServiceDTO(roomNumber=1, singleBeds=1, doubleBeds=0, totalPrice=20, roomType=FAMILY_ROOM, startDate=2024-04-01, endDate=2024-04-03)])]
//
//result: [
//        HotelWithReservableRoomsServiceDTO(hotelName=Hotel 1, city=Debrecen, imageName=da11728d-4c83-4a57-930c-d8e85925cc77.png, uniqueReservableRoomOfHotelServiceDTOList=[
//                UniqueReservableRoomOfHotelServiceDTO(roomNumber=1, singleBeds=1, doubleBeds=0, totalPrice=40, roomType=FAMILY_ROOM, startDate=2024-04-01, endDate=2024-04-05),
//                UniqueReservableRoomOfHotelServiceDTO(roomNumber=4, singleBeds=1, doubleBeds=1, totalPrice=40, roomType=FAMILY_ROOM, startDate=2024-04-01, endDate=2024-04-05)]),
//        HotelWithReservableRoomsServiceDTO(hotelName=Hotel 2, city=Debrecen, imageName=02e7052d-2a3e-42e1-a20e-9c07d3513899.png, uniqueReservableRoomOfHotelServiceDTOList=[
//                UniqueReservableRoomOfHotelServiceDTO(roomNumber=1, singleBeds=1, doubleBeds=0, totalPrice=40, roomType=FAMILY_ROOM, startDate=2024-04-01, endDate=2024-04-05)]),
//        HotelWithReservableRoomsServiceDTO(hotelName=Hotel 3, city=Budapest, imageName=c04aa952-a12f-4c8d-bf38-a7ed757f4e0f.png, uniqueReservableRoomOfHotelServiceDTOList=[
//                UniqueReservableRoomOfHotelServiceDTO(roomNumber=1, singleBeds=1, doubleBeds=0, totalPrice=40, roomType=FAMILY_ROOM, startDate=2024-04-01, endDate=2024-04-05)])]
//
//result: [
//        HotelWithReservableRoomsServiceDTO(hotelName=Hotel 1, city=Debrecen, imageName=da11728d-4c83-4a57-930c-d8e85925cc77.png, uniqueReservableRoomOfHotelServiceDTOList=[
//                UniqueReservableRoomOfHotelServiceDTO(roomNumber=1, singleBeds=1, doubleBeds=0, totalPrice=30, roomType=FAMILY_ROOM, startDate=2024-04-01, endDate=2024-04-04),
//                UniqueReservableRoomOfHotelServiceDTO(roomNumber=4, singleBeds=1, doubleBeds=1, totalPrice=30, roomType=FAMILY_ROOM, startDate=2024-04-01, endDate=2024-04-04),
//                UniqueReservableRoomOfHotelServiceDTO(roomNumber=5, singleBeds=1, doubleBeds=0, totalPrice=30, roomType=SINGLE_ROOM, startDate=2024-04-01, endDate=2024-04-04)]),
//        HotelWithReservableRoomsServiceDTO(hotelName=Hotel 2, city=Debrecen, imageName=02e7052d-2a3e-42e1-a20e-9c07d3513899.png, uniqueReservableRoomOfHotelServiceDTOList=[
//                UniqueReservableRoomOfHotelServiceDTO(roomNumber=1, singleBeds=1, doubleBeds=0, totalPrice=30, roomType=FAMILY_ROOM, startDate=2024-04-01, endDate=2024-04-04),
//                UniqueReservableRoomOfHotelServiceDTO(roomNumber=2, singleBeds=1, doubleBeds=0, totalPrice=30, roomType=SINGLE_ROOM, startDate=2024-04-01, endDate=2024-04-04)]),
//        HotelWithReservableRoomsServiceDTO(hotelName=Hotel 3, city=Budapest, imageName=c04aa952-a12f-4c8d-bf38-a7ed757f4e0f.png, uniqueReservableRoomOfHotelServiceDTOList=[
//                UniqueReservableRoomOfHotelServiceDTO(roomNumber=1, singleBeds=1, doubleBeds=0, totalPrice=30, roomType=FAMILY_ROOM, startDate=2024-04-01, endDate=2024-04-04)])]

