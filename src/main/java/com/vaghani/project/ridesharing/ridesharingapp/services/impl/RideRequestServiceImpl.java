package com.vaghani.project.ridesharing.ridesharingapp.services.impl;

import com.vaghani.project.ridesharing.ridesharingapp.entities.RideRequest;
import com.vaghani.project.ridesharing.ridesharingapp.exceptions.ResourceNotFoundException;
import com.vaghani.project.ridesharing.ridesharingapp.repositories.RideRequestRepository;
import com.vaghani.project.ridesharing.ridesharingapp.services.RideRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RideRequestServiceImpl implements RideRequestService {

    private final RideRequestRepository rideRequestRepository;

    @Override
    public RideRequest findRideRequestById(Long rideRequestId) {
        return rideRequestRepository.findById(rideRequestId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format("Ride Request with id: %d not found!", rideRequestId)
                ));
    }

    @Override
    public void update(RideRequest rideRequest) {
        rideRequestRepository.findById(rideRequest.getId())
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Ride Request with ID: %s not found!", rideRequest.getId())));
        rideRequestRepository.save(rideRequest);
    }
}
