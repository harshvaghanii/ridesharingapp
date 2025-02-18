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
import com.vaghani.project.ridesharing.ridesharingapp.security.JWTService;
import com.vaghani.project.ridesharing.ridesharingapp.services.DriverService;
import com.vaghani.project.ridesharing.ridesharingapp.services.RiderService;
import com.vaghani.project.ridesharing.ridesharingapp.services.WalletService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private JWTService jwtService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private RiderService riderService;

    @Mock
    private WalletService walletService;

    @Mock
    private DriverService driverService;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private AuthServiceImpl authService;

    private User mockUser;
    private SignupDto mockSignupDto;

    @BeforeEach
    void setUp() {
        mockUser = new User();
        mockUser.setId(1L);
        mockUser.setEmail("test@example.com");
        mockUser.setPassword("hashedPassword");
        mockUser.setRoles(new HashSet<>(Set.of(Role.RIDER)));  // ✅ Mutable set

        mockSignupDto = new SignupDto();
        mockSignupDto.setEmail("test@example.com");
        mockSignupDto.setPassword("plainPassword");
    }

    @Test
    void login_whenValidCredentials_thenReturnsTokens() {
        // Arrange
        String email = "test@example.com";
        String password = "password";
        Authentication auth = mock(Authentication.class);

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(auth);
        when(auth.getPrincipal()).thenReturn(mockUser);
        when(jwtService.generateAccessToken(mockUser)).thenReturn("accessToken");
        when(jwtService.generateRefreshToken(mockUser)).thenReturn("refreshToken");

        // Act
        String[] tokens = authService.login(email, password);

        // Assert
        assertThat(tokens).containsExactly("accessToken", "refreshToken");
        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(jwtService).generateAccessToken(mockUser);
        verify(jwtService).generateRefreshToken(mockUser);
    }

    @Test
    void signup_whenNewUser_thenReturnsUserDto() {
        // Arrange
        UserDto userDto = new UserDto();
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.empty());
        when(passwordEncoder.encode(anyString())).thenReturn("hashedPassword");
        when(userRepository.save(any(User.class))).thenReturn(mockUser);
        when(modelMapper.map(any(User.class), eq(UserDto.class))).thenReturn(userDto);
        when(modelMapper.map(any(SignupDto.class), eq(User.class))).thenReturn(mockUser);

        // Act
        UserDto result = authService.signup(mockSignupDto);

        // Assert
        assertThat(result).isNotNull();
        verify(userRepository).save(any(User.class));
        verify(riderService).createNewRider(any(User.class));
        verify(walletService).createWallet(any(User.class));
    }

    @Test
    void signup_whenEmailAlreadyExists_thenThrowsException() {
        // Arrange
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(mockUser));

        // Act & Assert
        assertThatThrownBy(() -> authService.signup(mockSignupDto))
                .isInstanceOf(RuntimeConflictException.class)
                .hasMessage("User with Email address test@example.com already exists!");

        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void onboardNewDriver_whenValidUser_thenReturnsDriverDto() {
        // Arrange
        Driver mockDriver = new Driver();
        mockDriver.setUser(mockUser);
        DriverDto driverDto = new DriverDto();

        when(userRepository.findById(1L)).thenReturn(Optional.of(mockUser));
        when(driverService.createNewDriver(any(Driver.class))).thenReturn(mockDriver);
        when(modelMapper.map(any(Driver.class), eq(DriverDto.class))).thenReturn(driverDto);

        // Act
        DriverDto result = authService.onboardNewDriver(1L, "vehicle123");

        // Assert
        assertThat(result).isNotNull();
        verify(userRepository).save(mockUser);
        verify(driverService).createNewDriver(any(Driver.class));
    }

    @Test
    void onboardNewDriver_whenUserNotFound_thenThrowsException() {
        // Arrange
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> authService.onboardNewDriver(1L, "vehicle123"))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("User with ID: 1 not found!");
    }

    @Test
    void onboardNewDriver_whenUserAlreadyDriver_thenThrowsException() {
        // Arrange
        mockUser.getRoles().add(Role.DRIVER);
        when(userRepository.findById(1L)).thenReturn(Optional.of(mockUser));

        // Act & Assert
        assertThatThrownBy(() -> authService.onboardNewDriver(1L, "vehicle123"))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("User with ID: 1 is already a Driver!");
    }

    @Test
    void refreshToken_whenValid_thenReturnsNewAccessToken() {
        // Arrange
        when(jwtService.getUserIdFromToken("refreshToken")).thenReturn(1L);
        when(userRepository.findById(1L)).thenReturn(Optional.of(mockUser));
        when(jwtService.generateAccessToken(mockUser)).thenReturn("newAccessToken");

        // Act
        String newToken = authService.refreshToken("refreshToken");

        // Assert
        assertThat(newToken).isEqualTo("newAccessToken");
    }

    @Test
    void refreshToken_whenUserNotFound_thenThrowsException() {
        // Arrange
        when(jwtService.getUserIdFromToken("refreshToken")).thenReturn(1L);
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> authService.refreshToken("refreshToken"))
                .isInstanceOf(ResourceNotFoundException.class);
    }
}
