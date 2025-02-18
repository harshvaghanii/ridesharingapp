package com.vaghani.project.ridesharing.ridesharingapp.services.impl;

import com.vaghani.project.ridesharing.ridesharingapp.dto.DriverDto;
import com.vaghani.project.ridesharing.ridesharingapp.dto.RideDto;
import com.vaghani.project.ridesharing.ridesharingapp.dto.RiderDto;
import com.vaghani.project.ridesharing.ridesharingapp.entities.Driver;
import com.vaghani.project.ridesharing.ridesharingapp.entities.Ride;
import com.vaghani.project.ridesharing.ridesharingapp.entities.RideRequest;
import com.vaghani.project.ridesharing.ridesharingapp.entities.User;
import com.vaghani.project.ridesharing.ridesharingapp.entities.enums.RideRequestStatus;
import com.vaghani.project.ridesharing.ridesharingapp.entities.enums.RideStatus;
import com.vaghani.project.ridesharing.ridesharingapp.repositories.DriverRepository;
import com.vaghani.project.ridesharing.ridesharingapp.services.PaymentService;
import com.vaghani.project.ridesharing.ridesharingapp.services.RatingService;
import com.vaghani.project.ridesharing.ridesharingapp.services.RideRequestService;
import com.vaghani.project.ridesharing.ridesharingapp.services.RideService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DriverServiceImplTest {

    @Mock
    private RideRequestService rideRequestService;
    @Mock
    private DriverRepository driverRepository;
    @Mock
    private RideService rideService;
    @Mock
    private ModelMapper modelMapper;
    @Mock
    private PaymentService paymentService;
    @Mock
    private RatingService ratingService;

    @InjectMocks
    private DriverServiceImpl driverService;

    private Driver driver;
    private RideRequest rideRequest;
    private Ride ride;
    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);

        driver = new Driver();
        driver.setId(1L);
        driver.setUser(user);
        driver.setAvailable(true);

        rideRequest = new RideRequest();
        rideRequest.setId(1L);
        rideRequest.setRideRequestStatus(RideRequestStatus.PENDING);

        ride = new Ride();
        ride.setId(1L);
        ride.setDriver(driver);
        ride.setRideStatus(RideStatus.CONFIRMED);
        ride.setOtp("1234");
    }

    void setMockSecurityContext() {
        // Mock Security Context to return our user
        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(user);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    void acceptRide_Success() {
        setMockSecurityContext();
        when(rideRequestService.findRideRequestById(1L)).thenReturn(rideRequest);
        when(driverRepository.findByUser(user)).thenReturn(Optional.of(driver));
        when(rideService.createNewRide(any(), any())).thenReturn(ride);
        when(modelMapper.map(any(Ride.class), eq(RideDto.class))).thenReturn(new RideDto());

        RideDto result = driverService.acceptRide(1L);

        assertNotNull(result);
        verify(rideRequestService).findRideRequestById(1L);
        verify(rideService).createNewRide(any(), any());
    }

    @Test
    void cancelRide_Success() {
        setMockSecurityContext();
        when(rideService.getRideById(1L)).thenReturn(ride);
        when(driverRepository.findByUser(user)).thenReturn(Optional.of(driver));
        when(rideService.updateRideStatus(any(), eq(RideStatus.CANCELLED))).thenReturn(ride);
        when(modelMapper.map(any(Ride.class), eq(RideDto.class))).thenReturn(new RideDto());

        RideDto result = driverService.cancelRide(1L);

        assertNotNull(result);
        verify(rideService).updateRideStatus(ride, RideStatus.CANCELLED);
    }

    @Test
    void startRide_WithCorrectOTP() {
        setMockSecurityContext();
        when(rideService.getRideById(1L)).thenReturn(ride);
        when(driverRepository.findByUser(user)).thenReturn(Optional.of(driver));
        when(rideService.updateRideStatus(any(), eq(RideStatus.ONGOING))).thenReturn(ride);
        when(modelMapper.map(any(Ride.class), eq(RideDto.class))).thenReturn(new RideDto());

        RideDto result = driverService.startRide(1L, "1234");

        assertNotNull(result);
        verify(paymentService).createNewPayment(ride);
        verify(ratingService).createNewRating(ride);
    }

    @Test
    void endRide_Success() {
        setMockSecurityContext();
        ride.setRideStatus(RideStatus.ONGOING);
        when(rideService.getRideById(1L)).thenReturn(ride);
        when(driverRepository.findByUser(user)).thenReturn(Optional.of(driver));
        when(rideService.updateRideStatus(any(), eq(RideStatus.ENDED))).thenReturn(ride);
        when(modelMapper.map(any(Ride.class), eq(RideDto.class))).thenReturn(new RideDto());

        RideDto result = driverService.endRide(1L);

        assertNotNull(result);
        verify(paymentService).processPayment(ride);
    }

    @Test
    void rateRider_Success() {
        setMockSecurityContext();
        ride.setRideStatus(RideStatus.ENDED);
        when(rideService.getRideById(1L)).thenReturn(ride);
        when(driverRepository.findByUser(user)).thenReturn(Optional.of(driver));
        when(ratingService.rateRider(any(), anyInt())).thenReturn(new RiderDto());

        RiderDto result = driverService.rateRider(1L, 5);

        assertNotNull(result);
        verify(ratingService).rateRider(ride, 5);
    }

    @Test
    void getMyProfile_Success() {
        setMockSecurityContext();
        when(driverRepository.findByUser(user)).thenReturn(Optional.of(driver));
        when(modelMapper.map(any(Driver.class), eq(DriverDto.class))).thenReturn(new DriverDto());

        DriverDto result = driverService.getMyProfile();

        assertNotNull(result);
    }

    @Test
    void getAllMyRides_Success() {
        setMockSecurityContext();
        Page<Ride> ridePage = new PageImpl<>(List.of(ride));
        when(driverRepository.findByUser(user)).thenReturn(Optional.of(driver));
        when(rideService.getAllRidesOfDriver(any(), any())).thenReturn(ridePage);
        when(modelMapper.map(any(Ride.class), eq(RideDto.class))).thenReturn(new RideDto());

        Page<RideDto> result = driverService.getAllMyRides(PageRequest.of(0, 5));

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
    }

    @Test
    void getCurrentDriver_Success() {
        setMockSecurityContext();
        when(driverRepository.findByUser(user)).thenReturn(Optional.of(driver));

        Driver result = driverService.getCurrentDriver();

        assertNotNull(result);
        assertEquals(driver, result);
    }

    @Test
    void updateDriverAvailability_Success() {
        when(driverRepository.save(any())).thenReturn(driver);

        Driver result = driverService.updateDriverAvailability(driver, false);

        assertNotNull(result);
        assertFalse(result.getAvailable());
    }

    @Test
    void createNewDriver_Success() {
        when(driverRepository.save(any())).thenReturn(driver);

        Driver result = driverService.createNewDriver(driver);

        assertNotNull(result);
        assertEquals(driver, result);
    }
}
