package com.vaghani.project.ridesharing.ridesharingapp.repositories;

import com.vaghani.project.ridesharing.ridesharingapp.entities.Rider;
import com.vaghani.project.ridesharing.ridesharingapp.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RiderRepository extends JpaRepository<Rider, Long> {

    Optional<Rider> findByUser(User user);
}
