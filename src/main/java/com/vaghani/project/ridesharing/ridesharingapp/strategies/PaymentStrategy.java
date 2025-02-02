package com.vaghani.project.ridesharing.ridesharingapp.strategies;

import com.vaghani.project.ridesharing.ridesharingapp.entities.Payment;

public interface PaymentStrategy {
    Double PLATFORM_COMMISSION = 0.3;

    void processPaymet(Payment payment);
}
