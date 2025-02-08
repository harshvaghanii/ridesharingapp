package com.vaghani.project.ridesharing.ridesharingapp.services.impl;

import com.vaghani.project.ridesharing.ridesharingapp.dto.DriverDto;
import com.vaghani.project.ridesharing.ridesharingapp.dto.SignupDto;
import com.vaghani.project.ridesharing.ridesharingapp.dto.UserDto;
import com.vaghani.project.ridesharing.ridesharingapp.entities.Driver;
import com.vaghani.project.ridesharing.ridesharingapp.entities.User;
import com.vaghani.project.ridesharing.ridesharingapp.entities.enums.Role;
import com.vaghani.project.ridesharing.ridesharingapp.exceptions.ResourceNotFoundException;
import com.vaghani.project.ridesharing.ridesharingapp.exceptions.RuntimeConflictException;
import com.vaghani.project.ridesharing.ridesharingapp.repositories.UserRepository;
import com.vaghani.project.ridesharing.ridesharingapp.services.AuthService;
import com.vaghani.project.ridesharing.ridesharingapp.services.DriverService;
import com.vaghani.project.ridesharing.ridesharingapp.services.RiderService;
import com.vaghani.project.ridesharing.ridesharingapp.services.WalletService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final ModelMapper modelMapper;
    private final UserRepository userRepository;
    private final RiderService riderService;
    private final WalletService walletService;
    private final DriverService driverService;

    @Override
    public String login(String email, String password) {
        return "";
    }

    @Override
    @Transactional
    public UserDto signup(SignupDto signupDto) {

        User user = userRepository.findByEmail(signupDto.getEmail()).orElse(null);
        if (user != null) {
            throw new RuntimeConflictException(STR."User with Email address \{signupDto.getEmail()} already exists!");
        }
        User mappedUser = modelMapper.map(signupDto, User.class);
        mappedUser.setRoles(Set.of(Role.RIDER));
        User savedUser = userRepository.save(mappedUser);

        riderService.createNewRider(savedUser);
        walletService.createWallet(savedUser);
        return modelMapper.map(savedUser, UserDto.class);
    }

    @Override
    @Transactional
    public DriverDto onboardNewDriver(Long userId, String vehicleId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(STR."User with ID: \{userId} not found!"));
        if (user.getRoles().contains(Role.DRIVER)) {
            throw new RuntimeException(STR."User with ID: \{userId} is already a Driver!");
        }

        Driver createdDriver = Driver.builder()
                .user(user)
                .rating(0.0)
                .vehicleId(vehicleId)
                .available(true)
                .build();
        user.getRoles().add(Role.DRIVER);
        userRepository.save(user);
        Driver savedDriver = driverService.createNewDriver(createdDriver);
        return modelMapper.map(savedDriver, DriverDto.class);
    }
}
