package com.vaghani.project.ridesharing.ridesharingapp.services.impl;

import com.vaghani.project.ridesharing.ridesharingapp.entities.Ride;
import com.vaghani.project.ridesharing.ridesharingapp.entities.User;
import com.vaghani.project.ridesharing.ridesharingapp.entities.Wallet;
import com.vaghani.project.ridesharing.ridesharingapp.entities.WalletTransaction;
import com.vaghani.project.ridesharing.ridesharingapp.entities.enums.TransactionMethod;
import com.vaghani.project.ridesharing.ridesharingapp.entities.enums.TransactionType;
import com.vaghani.project.ridesharing.ridesharingapp.exceptions.ResourceNotFoundException;
import com.vaghani.project.ridesharing.ridesharingapp.repositories.WalletRepository;
import com.vaghani.project.ridesharing.ridesharingapp.services.WalletService;
import com.vaghani.project.ridesharing.ridesharingapp.services.WalletTransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class WalletServiceImpl implements WalletService {

    private final WalletRepository walletRepository;
    private final WalletTransactionService walletTransactionService;

    @Override
    public Wallet addMoneyToWallet(User user, Double amount, String transactionId, Ride ride, TransactionMethod transactionMethod) {
        Wallet wallet = findByUser(user);
        wallet.setBalance(wallet.getBalance() + amount);

        WalletTransaction walletTransaction = WalletTransaction.builder()
                .transactionId(transactionId)
                .ride(ride)
                .wallet(wallet)
                .transactionType(TransactionType.CREDIT)
                .transactionMethod(transactionMethod)
                .amount(amount)
                .build();

        walletTransactionService.createNewWalletTransaction(walletTransaction);
        return walletRepository.save(wallet);
    }

    @Override
    @Transactional
    public Wallet deductMoneyFromWallet(User user, Double amount, String transactionId, Ride ride, TransactionMethod transactionMethod) {
        Wallet wallet = findByUser(user);
        wallet.setBalance(wallet.getBalance() - amount);

        WalletTransaction walletTransaction = WalletTransaction.builder()
                .transactionId(transactionId)
                .ride(ride)
                .wallet(wallet)
                .transactionType(TransactionType.DEBIT)
                .transactionMethod(transactionMethod)
                .amount(amount)
                .build();

        walletTransactionService.createNewWalletTransaction(walletTransaction);
        return walletRepository.save(wallet);
    }

    @Override
    public void withdrawAllMyMoneyFromWallet() {

    }

    @Override
    public Wallet findWalletById(Long walletId) {
        return walletRepository.findById(walletId)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Wallet with ID: %s not found!", walletId)));
    }

    @Override
    public Wallet createWallet(User user) {
        Wallet wallet = new Wallet();
        wallet.setUser(user);
        return walletRepository.save(wallet);
    }

    @Override
    public Wallet findByUser(User user) {
        return walletRepository.findByUser(user).orElseThrow(
                () -> new RuntimeException(String.format("Wallet not found for the User: %s", user))
        );
    }
}
