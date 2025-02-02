package com.vaghani.project.ridesharing.ridesharingapp.repositories;

import com.vaghani.project.ridesharing.ridesharingapp.entities.Payment;
import com.vaghani.project.ridesharing.ridesharingapp.entities.Ride;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    Optional<Payment> findByRide(Ride ride);
}
