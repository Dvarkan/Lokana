package com.dv.Lokana.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserDto {
    private Long id;
    private String bio;

    @NotEmpty
    private String firstname;
    @NotEmpty
    private String lastname;
}
