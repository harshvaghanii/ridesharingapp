package com.vaghani.project.ridesharing.ridesharingapp.dto;

import com.vaghani.project.ridesharing.ridesharingapp.entities.enums.PaymentMethod;
import com.vaghani.project.ridesharing.ridesharingapp.entities.enums.RideRequestStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RideRequestDto {

    private Long id;

    private PointDTO pickUpLocation;

    private PointDTO dropOffLocation;

    private LocalDateTime requestedTime;

    private RiderDto rider;

    private PaymentMethod paymentMethod;

    private Double fare;

    private RideRequestStatus rideRequestStatus;

}
