package com.vaghani.project.ridesharing.ridesharingapp.services.impl;

import com.vaghani.project.ridesharing.ridesharingapp.dto.DriverDto;
import com.vaghani.project.ridesharing.ridesharingapp.dto.RideDto;
import com.vaghani.project.ridesharing.ridesharingapp.dto.RiderDto;
import com.vaghani.project.ridesharing.ridesharingapp.entities.Driver;
import com.vaghani.project.ridesharing.ridesharingapp.entities.Ride;
import com.vaghani.project.ridesharing.ridesharingapp.entities.RideRequest;
import com.vaghani.project.ridesharing.ridesharingapp.entities.Rider;
import com.vaghani.project.ridesharing.ridesharingapp.entities.enums.RideRequestStatus;
import com.vaghani.project.ridesharing.ridesharingapp.entities.enums.RideStatus;
import com.vaghani.project.ridesharing.ridesharingapp.exceptions.ResourceNotFoundException;
import com.vaghani.project.ridesharing.ridesharingapp.repositories.DriverRepository;
import com.vaghani.project.ridesharing.ridesharingapp.services.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class DriverServiceImpl implements DriverService {

    private final RideRequestService rideRequestService;
    private final DriverRepository driverRepository;
    private final RideService rideService;
    private final ModelMapper modelMapper;
    private final PaymentService paymentService;
    private final RiderService riderService;

    @Override
    @Transactional
    public RideDto acceptRide(Long rideRequestId) {
        RideRequest rideRequest = rideRequestService.findRideRequestById(rideRequestId);
        if (rideRequest.getRideRequestStatus() != RideRequestStatus.PENDING) {
            throw new RuntimeException("Ride Request already accepted by another Driver!");
        }
        Driver currentDriver = getCurrentDriver();
        if (!currentDriver.getAvailable()) {
            throw new RuntimeException("Driver not available!");
        }
        Driver updatedDriver = updateDriverAvailability(currentDriver, false);
        Ride ride = rideService.createNewRide(rideRequest, updatedDriver);
        return modelMapper.map(ride, RideDto.class);
    }

    @Override
    public RideDto cancelRide(Long rideId) {
        Ride ride = rideService.getRideById(rideId);

        Driver currentDriver = getCurrentDriver();
        if (!currentDriver.equals(ride.getDriver())) {
            throw new RuntimeException("Cannot cancel the ride since you're not the driver for this ride!");
        }

        if (ride.getRideStatus() != RideStatus.CONFIRMED) {
            throw new RuntimeException(new RuntimeException("Cannot cancel a ride once it has been started!"));
        }

        Ride updatedRide = rideService.updateRideStatus(ride, RideStatus.CANCELLED);
        Driver updatedDriver = updateDriverAvailability(currentDriver, true);

        return modelMapper.map(updatedRide, RideDto.class);
    }

    @Override
    @Transactional
    public RideDto startRide(Long rideId, String OTP) {
        Ride ride = rideService.getRideById(rideId);
        Driver driver = getCurrentDriver();

        if (!driver.equals(ride.getDriver())) {
            throw new RuntimeException("Driver cannot start this ride as he has not accepted this ride!");
        }

        if (ride.getRideStatus() != RideStatus.CONFIRMED) {
            throw new RuntimeException(STR."Ride status is not Confirmed, hence the ride cannot be started! \{ride.getRideStatus()}");
        }

        if (!OTP.equals(ride.getOtp())) {
            log.info("Error verifying the otp. Current otp: {} and Actual otp is : {}", OTP, ride.getOtp());
            throw new RuntimeException("otp is not valid!");
        }
        ride.setStartedAt(LocalDateTime.now());
        Ride savedRide = rideService.updateRideStatus(ride, RideStatus.ONGOING);
        paymentService.createNewPayment(savedRide);
        return modelMapper.map(savedRide, RideDto.class);
    }

    @Override
    @Transactional
    public RideDto endRide(Long rideId) {
        Driver currentDriver = getCurrentDriver();
        Ride ride = rideService.getRideById(rideId);

        if (!currentDriver.equals(ride.getDriver())) {
            throw new RuntimeException("Not authorized to cancel the ride!");
        }

        if (ride.getRideStatus() != RideStatus.ONGOING) {
            throw new RuntimeException("Cannot end ride, since it is not Ongoing!");
        }

        ride.setEndedAt(LocalDateTime.now());
        Ride updatedRide = rideService.updateRideStatus(ride, RideStatus.ENDED);
        updateDriverAvailability(currentDriver, true);

        paymentService.processPayment(updatedRide);

        return modelMapper.map(updatedRide, RideDto.class);

    }

    @Override
    public RiderDto rateRider(Long rideId, Integer rating) {
        Ride ride = rideService.getRideById(rideId);
        Rider rider = ride.getRider();
        Rider updatedRider = riderService.updateRiderRating(rider, rating);
        return modelMapper.map(updatedRider, RiderDto.class);
    }

    @Override
    public DriverDto getMyProfile() {
        Driver driver = getCurrentDriver();
        return modelMapper.map(driver, DriverDto.class);
    }

    @Override
    public Page<RideDto> getAllMyRides(PageRequest pageRequest) {
        Driver currentDriver = getCurrentDriver();
        return rideService.getAllRidesOfDriver(currentDriver, pageRequest).map(
                ride -> modelMapper.map(ride, RideDto.class)
        );
    }

    @Override
    public Driver updateDriverRating(Driver driver, Integer rating) {
        Driver currentDriver = getCurrentDriver();
        if (!driver.equals(currentDriver)) {
            throw new RuntimeException("Not authorized to update the Driver's rating!");
        }
        driver.setRating((driver.getRating() + rating) / 2.0);
        return driverRepository.save(driver);
    }

    @Override
    public Driver getCurrentDriver() {
        //TODO : Implement this after having the Spring Security in place
        return driverRepository.findById(2l).orElseThrow(() -> new ResourceNotFoundException("Driver with id " + 2L + " not found!"));
    }

    @Override
    public Driver updateDriverAvailability(Driver driver, Boolean available) {
        driver.setAvailable(available);
        return driverRepository.save(driver);
    }

}
