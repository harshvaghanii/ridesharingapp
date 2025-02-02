package com.vaghani.project.ridesharing.ridesharingapp.strategies.impl;

import com.vaghani.project.ridesharing.ridesharingapp.entities.Driver;
import com.vaghani.project.ridesharing.ridesharingapp.entities.Payment;
import com.vaghani.project.ridesharing.ridesharingapp.entities.Rider;
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
public class WalletPaymentStrategy implements PaymentStrategy {

    private final WalletService walletService;
    private final PaymentRepository paymentRepository;

    @Override
    @Transactional
    public void processPaymet(Payment payment) {
        Driver driver = payment.getRide().getDriver();
        Rider rider = payment.getRide().getRider();

        walletService.deductMoneyFromWallet(rider.getUser(), payment.getAmount(), null, payment.getRide(), TransactionMethod.RIDE);

        double driversCut = payment.getAmount() * (1 - PLATFORM_COMMISSION);

        walletService.addMoneyToWallet(driver.getUser(), driversCut, null, payment.getRide(), TransactionMethod.RIDE);

        payment.setPaymentStatus(PaymentStatus.CONFIRMED);
        paymentRepository.save(payment);
    }
}
