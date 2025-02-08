package com.vaghani.project.ridesharing.ridesharingapp.services.impl;

import com.vaghani.project.ridesharing.ridesharingapp.dto.DriverDto;
import com.vaghani.project.ridesharing.ridesharingapp.dto.RiderDto;
import com.vaghani.project.ridesharing.ridesharingapp.entities.Driver;
import com.vaghani.project.ridesharing.ridesharingapp.entities.Rating;
import com.vaghani.project.ridesharing.ridesharingapp.entities.Ride;
import com.vaghani.project.ridesharing.ridesharingapp.entities.Rider;
import com.vaghani.project.ridesharing.ridesharingapp.repositories.DriverRepository;
import com.vaghani.project.ridesharing.ridesharingapp.repositories.RatingRepository;
import com.vaghani.project.ridesharing.ridesharingapp.repositories.RiderRepository;
import com.vaghani.project.ridesharing.ridesharingapp.services.RatingService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class RatingServiceImpl implements RatingService {

    private final RatingRepository ratingRepository;
    private final DriverRepository driverRepository;
    private final RiderRepository riderRepository;
    private final ModelMapper modelMapper;

    @Override
    public DriverDto rateDriver(Ride ride, Integer rating) {
        Driver driver = ride.getDriver();
        Rating ratingObj = ratingRepository.findByRide(ride)
                .orElseThrow(() -> new RuntimeException("Rating object not found!"));
        validateRating(rating);
        if (ratingObj.getDriverRating() != null) {
            throw new RuntimeException("You have already rated the driver for this ride!");
        }
        ratingObj.setDriverRating(rating);
        ratingRepository.save(ratingObj);
        driver.setRating((driver.getRating() + rating) / 2.0);
        Driver updatedDriver = driverRepository.save(driver);
        return modelMapper.map(updatedDriver, DriverDto.class);
    }

    @Override
    public RiderDto rateRider(Ride ride, Integer rating) {
        Rider rider = ride.getRider();
        Rating ratingObj = ratingRepository.findByRide(ride)
                .orElseThrow(() -> new RuntimeException("Rating object not found!"));
        validateRating(rating);
        if (ratingObj.getRiderRating() != null) {
            throw new RuntimeException("You have already rated the rider for this ride!");
        }
        ratingObj.setRiderRating(rating);
        ratingRepository.save(ratingObj);

        rider.setRating((rider.getRating() + rating) / 2.0);
        Rider updatedRider = riderRepository.save(rider);
        return modelMapper.map(updatedRider, RiderDto.class);
    }

    @Override
    public void createNewRating(Ride ride) {
        Rating rating = Rating.builder()
                .rider(ride.getRider())
                .driver(ride.getDriver())
                .ride(ride)
                .build();
        ratingRepository.save(rating);
    }

    @Override
    public void validateRating(Integer rating) {
        if (!(rating <= 0 || rating > 5)) {
            return;
        }
        throw new RuntimeException("Invalid Rating Provided!");
    }
}
