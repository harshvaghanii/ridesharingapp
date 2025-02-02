package com.vaghani.project.ridesharing.ridesharingapp.repositories;

import com.vaghani.project.ridesharing.ridesharingapp.entities.WalletTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WalletTransactionRepository extends JpaRepository<WalletTransaction, Long> {
}
