package com.vaghani.project.ridesharing.ridesharingapp.strategies;

import com.vaghani.project.ridesharing.ridesharingapp.strategies.impl.DriverMatchingHighestRatedDriverStrategy;
import com.vaghani.project.ridesharing.ridesharingapp.strategies.impl.DriverMatchingNearestDriverStrategy;
import com.vaghani.project.ridesharing.ridesharingapp.strategies.impl.RideFareDefaultFareCalculationStrategy;
import com.vaghani.project.ridesharing.ridesharingapp.strategies.impl.RideFareSurgePricingFareCalculationStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalTime;

@Component
@RequiredArgsConstructor
public class RideStrategyManager {

    private final DriverMatchingHighestRatedDriverStrategy highestRatedDriverStrategy;
    private final DriverMatchingNearestDriverStrategy nearestDriverStrategy;
    private final RideFareDefaultFareCalculationStrategy defaultFareCalculationStrategy;
    private final RideFareSurgePricingFareCalculationStrategy surgePricingFareCalculationStrategy;

    public DriverMatchingStrategy driverMatchingStrategy(double riderRating) {
        return riderRating >= 4.8 ? highestRatedDriverStrategy : nearestDriverStrategy;
    }

    public RideFareCalculationStrategy rideFareCalculationStrategy() {
//        Peak Hour - 6PM to 9PM
        LocalTime surgeStartTime = LocalTime.of(18, 0);
        LocalTime surgeEndTime = LocalTime.of(21, 0);
        LocalTime currentTime = LocalTime.now();

        boolean isSurgeTime = currentTime.isAfter(surgeStartTime) && currentTime.isBefore(surgeEndTime);

        return isSurgeTime ? surgePricingFareCalculationStrategy : defaultFareCalculationStrategy;

    }

}
