package com.vaghani.project.ridesharing.ridesharingapp.strategies.impl;

import com.vaghani.project.ridesharing.ridesharingapp.entities.Driver;
import com.vaghani.project.ridesharing.ridesharingapp.entities.Payment;
import com.vaghani.project.ridesharing.ridesharingapp.entities.enums.PaymentStatus;
import com.vaghani.project.ridesharing.ridesharingapp.entities.enums.TransactionMethod;
import com.vaghani.project.ridesharing.ridesharingapp.repositories.PaymentRepository;
import com.vaghani.project.ridesharing.ridesharingapp.services.WalletService;
import com.vaghani.project.ridesharing.ridesharingapp.strategies.PaymentStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CashPaymentStrategy implements PaymentStrategy {

    private final WalletService walletService;
    private final PaymentRepository paymentRepository;

    @Override
    @Transactional
    public void processPayment(Payment payment) {
        Driver driver = payment.getRide().getDriver();
        double platformCommission = payment.getAmount() * PLATFORM_COMMISSION;

        walletService.deductMoneyFromWallet(driver.getUser(), platformCommission, null, payment.getRide(), TransactionMethod.RIDE);
        payment.setPaymentStatus(PaymentStatus.CONFIRMED);

        paymentRepository.save(payment);

    }
}
