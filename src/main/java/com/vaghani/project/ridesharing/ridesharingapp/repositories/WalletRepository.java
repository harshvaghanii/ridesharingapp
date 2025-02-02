package com.vaghani.project.ridesharing.ridesharingapp.repositories;

import com.vaghani.project.ridesharing.ridesharingapp.entities.User;
import com.vaghani.project.ridesharing.ridesharingapp.entities.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WalletRepository extends JpaRepository<Wallet, Long> {
    Optional<Wallet> findByUser(User user);
}
