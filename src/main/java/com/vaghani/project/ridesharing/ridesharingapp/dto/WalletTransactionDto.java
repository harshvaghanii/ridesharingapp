package com.vaghani.project.ridesharing.ridesharingapp.dto;

import com.vaghani.project.ridesharing.ridesharingapp.entities.enums.TransactionMethod;
import com.vaghani.project.ridesharing.ridesharingapp.entities.enums.TransactionType;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class WalletTransactionDto {

    private Long id;

    private Double amount;

    private TransactionType transactionType;

    private TransactionMethod transactionMethod;

    private RideDto rideDto;

    private String transactionId;

    private WalletDto wallet;

    private LocalDateTime timeStamp;

}
