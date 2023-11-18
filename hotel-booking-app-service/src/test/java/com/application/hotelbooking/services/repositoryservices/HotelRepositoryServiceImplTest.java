package com.application.hotelbooking.services.repositoryservices;

import com.application.hotelbooking.repositories.HotelRepository;
import com.application.hotelbooking.services.repositoryservices.implementations.HotelRepositoryServiceImpl;
import com.application.hotelbooking.transformers.HotelTransformer;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class HotelRepositoryServiceImplTest {

    @Mock
    private HotelRepository hotelRepository;

    @Mock
    private HotelTransformer hotelTransformer;

    @InjectMocks
    private HotelRepositoryServiceImpl hotelRepositoryService;

    @Test
    public void testDemo(){
        Assertions.assertThat(hotelRepositoryService.testMethod()).isTrue();
    }
}
