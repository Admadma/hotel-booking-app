package com.application.hotelbooking.services;

import com.application.hotelbooking.services.implementations.RoomServiceImpl;
import com.application.hotelbooking.services.repositoryservices.RoomRepositoryService;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class RoomServiceImplTest {

    @InjectMocks
    private RoomServiceImpl roomService;

    @Mock
    private RoomRepositoryService roomRepositoryService;

    @Mock
    private ReservationService reservationService;
}
