package com.vaghani.project.ridesharing.ridesharingapp.repositories;

import com.vaghani.project.ridesharing.ridesharingapp.TestContainerConfiguration;
import com.vaghani.project.ridesharing.ridesharingapp.entities.User;
import com.vaghani.project.ridesharing.ridesharingapp.entities.Wallet;
import com.vaghani.project.ridesharing.ridesharingapp.entities.enums.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(TestContainerConfiguration.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class WalletRepositoryTest {

    @Autowired
    private WalletRepository walletRepository;

    @Autowired
    private UserRepository userRepository;

    private User testUser;

    @BeforeEach
    void setUp() {
        // Create and save a user before each test
        testUser = new User();
        testUser.setName("Test User");
        testUser.setEmail("testuser@gmail.com");
        testUser.setPassword("password123");
        testUser.setRoles(Set.of(Role.RIDER));

        testUser = userRepository.save(testUser);
    }

    @Test
    void findByUser_whenWalletExists_shouldReturnWallet() {
        // Arrange
        Wallet wallet = new Wallet();
        wallet.setUser(testUser);
        wallet.setBalance(100.0);
        walletRepository.save(wallet);

        // Act
        Optional<Wallet> retrievedWallet = walletRepository.findByUser(testUser);

        // Assert
        assertThat(retrievedWallet).isPresent();
        assertThat(retrievedWallet.get().getUser().getId()).isEqualTo(testUser.getId());
        assertThat(retrievedWallet.get().getBalance()).isEqualTo(100.0);
    }

    @Test
    void findByUser_whenWalletDoesNotExist_shouldReturnEmptyOptional() {
        // Act
        Optional<Wallet> retrievedWallet = walletRepository.findByUser(testUser);

        // Assert
        assertThat(retrievedWallet).isEmpty();
    }
}