package com.vaghani.project.ridesharing.ridesharingapp.dto;


import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SignupDto {

    private String name;

    private String email;

    private String password;

}
