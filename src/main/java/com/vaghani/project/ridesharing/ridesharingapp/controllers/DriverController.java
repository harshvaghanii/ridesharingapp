package com.vaghani.project.ridesharing.ridesharingapp.controllers;

import com.vaghani.project.ridesharing.ridesharingapp.dto.RideDto;
import com.vaghani.project.ridesharing.ridesharingapp.dto.RideStartDto;
import com.vaghani.project.ridesharing.ridesharingapp.services.DriverService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/driver")
@RequiredArgsConstructor
@Slf4j
public class DriverController {

    private final DriverService driverService;

    @PostMapping(path = "/acceptRide/{rideRequestId}")
    public ResponseEntity<RideDto> acceptRide(@PathVariable Long rideRequestId) {
        RideDto rideDto = driverService.acceptRide(rideRequestId);
        return ResponseEntity.ok(rideDto);
    }

    @PostMapping(path = "/startRide/{rideId}/{otp}")
    public ResponseEntity<RideDto> startRide(@PathVariable Long rideId,
                                             @RequestBody RideStartDto rideStartDto) {
        log.info("Here is the otp sent in the body: {} ", rideStartDto.getOtp());
        RideDto rideDto = driverService.startRide(rideId, rideStartDto.getOtp());
        return ResponseEntity.ok(rideDto);
    }

}
