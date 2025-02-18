package com.vaghani.project.ridesharing.ridesharingapp.services.impl;

import com.vaghani.project.ridesharing.ridesharingapp.entities.Payment;
import com.vaghani.project.ridesharing.ridesharingapp.entities.Ride;
import com.vaghani.project.ridesharing.ridesharingapp.entities.enums.PaymentStatus;
import com.vaghani.project.ridesharing.ridesharingapp.exceptions.ResourceNotFoundException;
import com.vaghani.project.ridesharing.ridesharingapp.repositories.PaymentRepository;
import com.vaghani.project.ridesharing.ridesharingapp.services.PaymentService;
import com.vaghani.project.ridesharing.ridesharingapp.strategies.PaymentStrategyManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final PaymentStrategyManager paymentStrategyManager;

    @Override
    public void processPayment(Ride ride) {
        Payment payment = paymentRepository.findByRide(ride)
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format("Payment not found with Ride %s", ride)
                ));
        paymentStrategyManager.paymentStrategy(payment.getPaymentMethod()).processPayment(payment);
    }

    @Override
    public Payment createNewPayment(Ride ride) {
        Payment payment = Payment.builder()
                .ride(ride)
                .paymentMethod(ride.getPaymentMethod())
                .amount(ride.getFare())
                .paymentStatus(PaymentStatus.PENDING)
                .build();
        return paymentRepository.save(payment);
    }

    @Override
    public void updatePaymentStatus(Payment payment, PaymentStatus status) {
        payment.setPaymentStatus(status);
        paymentRepository.save(payment);
    }
}
