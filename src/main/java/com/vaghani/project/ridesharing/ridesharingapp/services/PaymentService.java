package com.vaghani.project.ridesharing.ridesharingapp.services;

import com.vaghani.project.ridesharing.ridesharingapp.entities.Payment;
import com.vaghani.project.ridesharing.ridesharingapp.entities.Ride;
import com.vaghani.project.ridesharing.ridesharingapp.entities.enums.PaymentStatus;

public interface PaymentService {

    void processPayment(Ride ride);

    Payment createNewPayment(Ride ride);

    void updatePaymentStatus(Payment payment, PaymentStatus status);

}
