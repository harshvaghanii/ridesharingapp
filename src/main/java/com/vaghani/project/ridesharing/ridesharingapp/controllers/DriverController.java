package com.vaghani.project.ridesharing.ridesharingapp.controllers;

import com.vaghani.project.ridesharing.ridesharingapp.dto.*;
import com.vaghani.project.ridesharing.ridesharingapp.services.DriverService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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

    @PostMapping(path = "/startRide/{rideId}")
    public ResponseEntity<RideDto> startRide(@PathVariable Long rideId,
                                             @RequestBody RideStartDto rideStartDto) {
        log.info("Here is the otp sent in the body: {} ", rideStartDto.getOtp());
        RideDto rideDto = driverService.startRide(rideId, rideStartDto.getOtp());
        return ResponseEntity.ok(rideDto);
    }

    @PostMapping(path = "/endRide/{rideId}")
    public ResponseEntity<RideDto> endRide(@PathVariable Long rideId) {
        log.info("The Ride id to end: {} ", rideId);
        RideDto rideDto = driverService.endRide(rideId);
        return ResponseEntity.ok(rideDto);
    }

    @PostMapping(path = "/cancelRide/{rideId}")
    public ResponseEntity<RideDto> cancelRide(@PathVariable Long rideId) {
        log.info("The Ride id to cancel: {} ", rideId);
        RideDto rideDto = driverService.cancelRide(rideId);
        return ResponseEntity.ok(rideDto);
    }

    @PostMapping("/rateRider")
    public ResponseEntity<RiderDto> rateRider(@RequestBody RatingDto ratingDto) {
        return ResponseEntity.ok(driverService.rateRider(ratingDto.getRideId(), ratingDto.getRating()));
    }

    @GetMapping("/myProfile")
    public ResponseEntity<DriverDto> getMyProfile() {
        return ResponseEntity.ok(driverService.getMyProfile());
    }

    @GetMapping("/myRides")
    public ResponseEntity<Page<RideDto>> getAllMyRides(@RequestParam(defaultValue = "0") Integer pageOffset,
                                                       @RequestParam(defaultValue = "10", required = false) Integer pageSize) {
        PageRequest pageRequest = PageRequest.of(pageOffset, pageSize,
                Sort.by(Sort.Direction.DESC, "requestedTime", "id"));
        return ResponseEntity.ok(driverService.getAllMyRides(pageRequest));
    }

}
