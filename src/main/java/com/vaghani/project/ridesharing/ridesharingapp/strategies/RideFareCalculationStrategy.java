package com.vaghani.project.ridesharing.ridesharingapp.strategies;

import com.vaghani.project.ridesharing.ridesharingapp.dto.RideRequestDto;

public interface RideFareCalculationStrategy {

    double calculateFare(RideRequestDto rideRequestDto);

}
