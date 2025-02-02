package com.vaghani.project.ridesharing.ridesharingapp.services;

import com.vaghani.project.ridesharing.ridesharingapp.entities.Driver;
import com.vaghani.project.ridesharing.ridesharingapp.entities.Ride;
import com.vaghani.project.ridesharing.ridesharingapp.entities.RideRequest;
import com.vaghani.project.ridesharing.ridesharingapp.entities.Rider;
import com.vaghani.project.ridesharing.ridesharingapp.entities.enums.RideStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

public interface RideService {

    Ride getRideById(Long rideId);

    Ride createNewRide(RideRequest rideRequest, Driver driver);

    Ride updateRideStatus(Ride ride, RideStatus rideStatus);

    Page<Ride> getAllRidesOfRider(Rider rider, PageRequest pageRequest);

    Page<Ride> getAllRidesOfDriver(Driver driver, PageRequest pageRequest);

}
