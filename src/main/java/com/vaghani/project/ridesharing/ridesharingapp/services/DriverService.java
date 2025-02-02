package com.vaghani.project.ridesharing.ridesharingapp.services;

import com.vaghani.project.ridesharing.ridesharingapp.dto.DriverDto;
import com.vaghani.project.ridesharing.ridesharingapp.dto.RideDto;
import com.vaghani.project.ridesharing.ridesharingapp.dto.RiderDto;
import com.vaghani.project.ridesharing.ridesharingapp.entities.Driver;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

public interface DriverService {

    RideDto acceptRide(Long rideRequestId);

    RideDto cancelRide(Long rideId);

    RideDto startRide(Long rideId, String OTP);

    RideDto endRide(Long rideId);

    RiderDto rateRider(Long rideId, Integer rating);

    DriverDto getMyProfile();

    Page<RideDto> getAllMyRides(PageRequest pageRequest);

    Driver getCurrentDriver();

    Driver updateDriverAvailability(Driver driver, Boolean available);
}
