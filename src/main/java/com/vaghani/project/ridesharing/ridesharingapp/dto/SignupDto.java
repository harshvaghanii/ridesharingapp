package com.vaghani.project.ridesharing.ridesharingapp.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SignupDto {

    private String name;

    private String email;

    private String password;

}
