package com.vaghani.project.ridesharing.ridesharingapp.strategies;

import com.vaghani.project.ridesharing.ridesharingapp.entities.enums.PaymentMethod;
import com.vaghani.project.ridesharing.ridesharingapp.strategies.impl.CashPaymentStrategy;
import com.vaghani.project.ridesharing.ridesharingapp.strategies.impl.WalletPaymentStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PaymentStrategyManager {

    private final WalletPaymentStrategy walletPaymentStrategy;
    private final CashPaymentStrategy cashPaymentStrategy;

    public PaymentStrategy paymentStrategy(PaymentMethod paymentMethod) {
        return switch (paymentMethod) {
            case WALLET -> walletPaymentStrategy;
            case CASH -> cashPaymentStrategy;
        };
    }

}
