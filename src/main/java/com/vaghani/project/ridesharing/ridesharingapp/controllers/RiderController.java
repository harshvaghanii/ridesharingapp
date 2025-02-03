package com.vaghani.project.ridesharing.ridesharingapp.controllers;

import com.vaghani.project.ridesharing.ridesharingapp.dto.RideDto;
import com.vaghani.project.ridesharing.ridesharingapp.dto.RideRequestDto;
import com.vaghani.project.ridesharing.ridesharingapp.services.RiderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/rider")
@RequiredArgsConstructor
@Slf4j
public class RiderController {

    private final RiderService riderService;

    @PostMapping(path = "/requestRide")
    public ResponseEntity<RideRequestDto> requestRide(@RequestBody RideRequestDto rideRequestDto) {
        return ResponseEntity.ok(riderService.requestRide(rideRequestDto));
    }

    @PostMapping(path = "/cancelRide/{rideId}")
    public ResponseEntity<RideDto> cancelRide(@PathVariable Long rideId) {
        log.info("The Ride id to cancel: {} ", rideId);
        RideDto rideDto = riderService.cancelRide(rideId);
        return ResponseEntity.ok(rideDto);
    }

}
