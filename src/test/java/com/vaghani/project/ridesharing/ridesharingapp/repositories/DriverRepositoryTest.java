package com.vaghani.project.ridesharing.ridesharingapp.repositories;

import com.vaghani.project.ridesharing.ridesharingapp.TestContainerConfiguration;
import com.vaghani.project.ridesharing.ridesharingapp.entities.Driver;
import com.vaghani.project.ridesharing.ridesharingapp.entities.User;
import com.vaghani.project.ridesharing.ridesharingapp.entities.enums.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(TestContainerConfiguration.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class DriverRepositoryTest {

    @Autowired
    private DriverRepository driverRepository;

    @Autowired
    private UserRepository userRepository;

    private final GeometryFactory geometryFactory = new GeometryFactory();

    private User user;
    private Driver driver;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setName("John Doe");
        user.setEmail("driver@example.com");
        user.setPassword("password123");
        user.setRoles(Set.of(Role.DRIVER));

        user = userRepository.save(user);

        Point location = geometryFactory.createPoint(new Coordinate(-73.935242, 40.730610)); // Example NYC location
        location.setSRID(4326);
        driver = new Driver();
        driver.setUser(user);
        driver.setCurrentLocation(location);
        driver.setAvailable(true);
        driver.setRating(4.8);

        driver = driverRepository.save(driver);
    }

    @Test
    void findByUser_whenDriverExists_thenReturnDriver() {
        // Act
        Optional<Driver> retrievedDriver = driverRepository.findByUser(user);

        // Assert
        assertThat(retrievedDriver).isPresent();
        assertThat(retrievedDriver.get().getUser().getEmail()).isEqualTo(user.getEmail());
    }

    @Test
    void findByUser_whenDriverDoesNotExist_thenReturnEmpty() {
        // Arrange
        User anotherUser = new User();
        anotherUser.setName("Random User");
        anotherUser.setEmail("random@example.com");
        anotherUser.setPassword("securePass");
        anotherUser.setRoles(Set.of(Role.RIDER));

        anotherUser = userRepository.save(anotherUser);

        // Act
        Optional<Driver> retrievedDriver = driverRepository.findByUser(anotherUser);

        // Assert
        assertThat(retrievedDriver).isEmpty();
    }

    @Test
    void findTenNearestDrivers_whenDriversExist_thenReturnNearest() {
        // Arrange
        Point pickupLocation = geometryFactory.createPoint(new Coordinate(-73.935242, 40.730610));
        pickupLocation.setSRID(4326);
        // Act
        List<Driver> nearbyDrivers = driverRepository.findTenNearestDrivers(pickupLocation);

        // Assert
        assertThat(nearbyDrivers).isNotEmpty();
        assertThat(nearbyDrivers.get(0).getUser().getEmail()).isEqualTo(user.getEmail());
    }

    @Test
    void findTenNearbyTopRatedDrivers_whenDriversExist_thenReturnTopRated() {
        // Arrange
        Point pickupLocation = geometryFactory.createPoint(new Coordinate(-73.935242, 40.730610));
        pickupLocation.setSRID(4326);
        // Act
        List<Driver> topRatedDrivers = driverRepository.findTenNearbyTopRatedDrivers(pickupLocation);

        // Assert
        assertThat(topRatedDrivers).isNotEmpty();
        assertThat(topRatedDrivers.get(0).getRating()).isGreaterThanOrEqualTo(4.8);
    }
}
