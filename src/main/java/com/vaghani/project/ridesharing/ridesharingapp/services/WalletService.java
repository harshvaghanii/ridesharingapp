package com.vaghani.project.ridesharing.ridesharingapp.services;

import com.vaghani.project.ridesharing.ridesharingapp.entities.Ride;
import com.vaghani.project.ridesharing.ridesharingapp.entities.User;
import com.vaghani.project.ridesharing.ridesharingapp.entities.Wallet;
import com.vaghani.project.ridesharing.ridesharingapp.entities.enums.TransactionMethod;

public interface WalletService {

    Wallet addMoneyToWallet(User user, Double amount, String transactionId, Ride ride, TransactionMethod transactionMethod);

    Wallet deductMoneyFromWallet(User user, Double amount, String transactionId, Ride ride, TransactionMethod transactionMethod);

    void withdrawAllMyMoneyFromWallet();

    Wallet findWalletById(Long walletId);

    Wallet createWallet(User user);

    Wallet findByUser(User user);

}
