package com.vaghani.project.ridesharing.ridesharingapp.controllers;

import com.vaghani.project.ridesharing.ridesharingapp.dto.*;
import com.vaghani.project.ridesharing.ridesharingapp.services.RiderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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

    @PostMapping(path = "/rateDriver")
    public ResponseEntity<DriverDto> rateDriver(@RequestBody RatingDto ratingDto) {
        DriverDto driver = riderService.rateDriver(ratingDto.getRideId(), ratingDto.getRating());
        return ResponseEntity.ok(driver);
    }

    @GetMapping("/myProfile")
    public ResponseEntity<RiderDto> getMyProfile() {
        RiderDto rider = riderService.getMyProfile();
        return ResponseEntity.ok(rider);
    }

    @GetMapping("/myRides")
    public ResponseEntity<Page<RideDto>> getAllMyRides(
            @RequestParam(defaultValue = "0") Integer pageOffSet,
            @RequestParam(defaultValue = "10") Integer pageSize
    ) {
        PageRequest pageRequest = PageRequest.of(pageOffSet,
                pageSize,
                Sort.by(
                        Sort.Direction.DESC,
                        "requestedTime", "id"
                ));
        Page<RideDto> page = riderService.getAllMyRides(pageRequest);
        return ResponseEntity.ok(page);
    }

}
