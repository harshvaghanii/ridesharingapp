package com.vaghani.project.ridesharing.ridesharingapp.services.impl;

import com.vaghani.project.ridesharing.ridesharingapp.dto.DriverDto;
import com.vaghani.project.ridesharing.ridesharingapp.dto.RideDto;
import com.vaghani.project.ridesharing.ridesharingapp.dto.RideRequestDto;
import com.vaghani.project.ridesharing.ridesharingapp.dto.RiderDto;
import com.vaghani.project.ridesharing.ridesharingapp.entities.*;
import com.vaghani.project.ridesharing.ridesharingapp.entities.enums.RideRequestStatus;
import com.vaghani.project.ridesharing.ridesharingapp.entities.enums.RideStatus;
import com.vaghani.project.ridesharing.ridesharingapp.exceptions.ResourceNotFoundException;
import com.vaghani.project.ridesharing.ridesharingapp.repositories.RideRequestRepository;
import com.vaghani.project.ridesharing.ridesharingapp.repositories.RiderRepository;
import com.vaghani.project.ridesharing.ridesharingapp.services.DriverService;
import com.vaghani.project.ridesharing.ridesharingapp.services.RatingService;
import com.vaghani.project.ridesharing.ridesharingapp.services.RideService;
import com.vaghani.project.ridesharing.ridesharingapp.services.RiderService;
import com.vaghani.project.ridesharing.ridesharingapp.strategies.DriverMatchingStrategy;
import com.vaghani.project.ridesharing.ridesharingapp.strategies.RideFareCalculationStrategy;
import com.vaghani.project.ridesharing.ridesharingapp.strategies.RideStrategyManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class RiderServiceImpl implements RiderService {

    private final ModelMapper modelMapper;
    private final RideStrategyManager strategyManager;
    private final RideRequestRepository rideRequestRepository;
    private final RiderRepository riderRepository;
    private final RideService rideService;
    private final RatingService ratingService;
    private final DriverService driverService;

    @Override
    @Transactional
    public RideRequestDto requestRide(RideRequestDto rideRequestDto) {
        Rider rider = getCurrentRider();
        RideRequest rideRequest = modelMapper.map(rideRequestDto, RideRequest.class);
        rideRequest.setRideRequestStatus(RideRequestStatus.PENDING);
        RideFareCalculationStrategy rideFareCalculationStrategy = strategyManager.rideFareCalculationStrategy();
        Double fare = rideFareCalculationStrategy.calculateFare(rideRequest);
        rideRequest.setFare(fare);
        DriverMatchingStrategy driverMatchingStrategy = strategyManager.driverMatchingStrategy(rider.getRating());
        List<Driver> matchingDrivers = driverMatchingStrategy.findMatchingDriver(rideRequest);

        //TODO: Send notifications to the matching drivers

        rideRequest.setRider(rider);
        return modelMapper.map(rideRequestRepository.save(rideRequest), RideRequestDto.class);
    }

    @Override
    public RideDto cancelRide(Long rideId) {
        Rider rider = getCurrentRider();
        Ride ride = rideService.getRideById(rideId);
        if (!rider.equals(ride.getRider())) {
            throw new RuntimeException("Cannot cancel the ride as this ride does not belong to this rider!");
        }

        if (ride.getRideStatus() != RideStatus.CONFIRMED) {
            throw new RuntimeException("Cannot cancel this ride as this is not in the Confirmed State!");
        }

        Ride updatedRide = rideService.updateRideStatus(ride, RideStatus.CANCELLED);
        driverService.updateDriverAvailability(ride.getDriver(), true);
        return modelMapper.map(updatedRide, RideDto.class);
    }

    @Override
    public DriverDto rateDriver(Long rideId, Integer rating) {
        Ride ride = rideService.getRideById(rideId);
        return ratingService.rateDriver(ride, rating);
    }

    @Override
    public RiderDto getMyProfile() {
        return modelMapper.map(getCurrentRider(), RiderDto.class);
    }

    @Override
    public Page<RideDto> getAllMyRides(PageRequest pageRequest) {
        Rider currentRider = getCurrentRider();
        return rideService.getAllRidesOfRider(currentRider, pageRequest)
                .map(ride -> modelMapper.map(ride, RideDto.class));
    }

    @Override
    public Rider createNewRider(User user) {
        Rider rider = Rider
                .builder()
                .user(user)
                .rating(0.0)
                .build();
        return riderRepository.save(rider);
    }

    @Override
    public Rider getCurrentRider() {
        // TODO: Implement Spring Security
        return riderRepository.findById(1L).orElseThrow(() -> new ResourceNotFoundException("Rider with id : " + 1 + " not found!"));
    }

}
