package com.vaghani.project.ridesharing.ridesharingapp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class RiderDto {

    private UserDto user;

    private Double rating;
}
