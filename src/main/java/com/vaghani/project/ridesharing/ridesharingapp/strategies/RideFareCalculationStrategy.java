package com.vaghani.project.ridesharing.ridesharingapp.strategies;

import com.vaghani.project.ridesharing.ridesharingapp.entities.RideRequest;

public interface RideFareCalculationStrategy {

    static final Double RIDE_FARE_MULTIPLIER = 10.0;

    double calculateFare(RideRequest rideRequest);

}
