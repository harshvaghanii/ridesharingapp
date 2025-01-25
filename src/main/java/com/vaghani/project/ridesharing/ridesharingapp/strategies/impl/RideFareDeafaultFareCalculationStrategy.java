package com.vaghani.project.ridesharing.ridesharingapp.strategies.impl;

import com.vaghani.project.ridesharing.ridesharingapp.dto.RideRequestDto;
import com.vaghani.project.ridesharing.ridesharingapp.strategies.RideFareCalculationStrategy;
import org.springframework.stereotype.Service;

@Service
public class RideFareDeafaultFareCalculationStrategy implements RideFareCalculationStrategy {
    @Override
    public double calculateFare(RideRequestDto rideRequestDto) {
        return 0;
    }
}
