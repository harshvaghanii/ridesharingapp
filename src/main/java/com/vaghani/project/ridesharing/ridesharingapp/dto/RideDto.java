package com.vaghani.project.ridesharing.ridesharingapp.dto;

import com.vaghani.project.ridesharing.ridesharingapp.entities.enums.PaymentMethod;
import com.vaghani.project.ridesharing.ridesharingapp.entities.enums.RideStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.locationtech.jts.geom.Point;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class RideDto {

    private PointDTO pickUpLocation;

    private PointDTO dropOffLocation;

    private RiderDto rider;

    private DriverDto driver;

    private PaymentMethod paymentMethod;

    private RideStatus rideStatus;

    private Double fare;

    private String otp;

    private LocalDateTime requestedTime;

    private LocalDateTime startedAt;

    private LocalDateTime endedAt;
}
