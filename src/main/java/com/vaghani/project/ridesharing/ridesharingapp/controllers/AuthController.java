package com.vaghani.project.ridesharing.ridesharingapp.controllers;

import com.vaghani.project.ridesharing.ridesharingapp.dto.DriverDto;
import com.vaghani.project.ridesharing.ridesharingapp.dto.OnboardDriverDto;
import com.vaghani.project.ridesharing.ridesharingapp.dto.SignupDto;
import com.vaghani.project.ridesharing.ridesharingapp.dto.UserDto;
import com.vaghani.project.ridesharing.ridesharingapp.services.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<UserDto> signup(@RequestBody SignupDto signupDto) {
        UserDto user = authService.signup(signupDto);
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }

    @PostMapping("/onboardNewDriver/{userId}")
    public ResponseEntity<DriverDto> onboardNewDriver(@PathVariable Long userId, @RequestBody OnboardDriverDto onboardDriverDto) {
        return new ResponseEntity<>(authService.onboardNewDriver(userId,
                onboardDriverDto.getVehicleId()), HttpStatus.CREATED);
    }

}
