package com.patika.Atm.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {

    @NotNull(message = "First name is not be null")
    private String firstname;

    @NotNull(message = "First name is not be null")
    private String lastname;

    @Email(message = "Invalid email")
    private String email;

    @Size(min = 6, max = 8, message = "Password length must be between 6 and 8")
    private String password;
}
