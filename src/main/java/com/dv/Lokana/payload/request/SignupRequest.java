package com.dv.Lokana.payload.request;

import com.dv.Lokana.annotations.PasswordMatches;
import com.dv.Lokana.annotations.ValidEmail;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@PasswordMatches
public class SignupRequest {

    @Email(message = "It should have email format")
    @NotBlank(message = "User rmail is required")
    @ValidEmail
    private String email;

    @NotEmpty(message = "Please entry your name")
    private String firstname;

    @NotEmpty(message = "Please entry your lastname")
    private String lastname;

    @NotEmpty(message = "Please entry your username")
    private String username;

    @NotEmpty(message = "Password is required")
    @Size(min = 8)
    private String password;
    private String confirmPassword;
}
