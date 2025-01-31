package com.vaghani.project.ridesharing.ridesharingapp.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.geolatte.geom.Point;

@Entity
@Getter
@Setter
public class Driver {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    private Double rating;

    private String vehicleId;

    private Boolean available;

    @Column(columnDefinition = "Geometry(Point, 4326)")
    Point currentLocation;

}
