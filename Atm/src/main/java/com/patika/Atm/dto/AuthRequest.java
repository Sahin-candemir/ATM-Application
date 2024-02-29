package com.patika.Atm.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AuthRequest{

       @Email
       private String email;
       @Size(min = 6, max = 8, message = "Password length must be between 6 and 8")
       private String password;

}
