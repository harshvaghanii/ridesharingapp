package com.vaghani.project.ridesharing.ridesharingapp.services;

import com.vaghani.project.ridesharing.ridesharingapp.entities.User;
import com.vaghani.project.ridesharing.ridesharingapp.exceptions.ResourceNotFoundException;
import com.vaghani.project.ridesharing.ridesharingapp.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public final class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Please check the credentials and try again!"));
    }

    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("User not found with id: %d", id)));
    }
}
