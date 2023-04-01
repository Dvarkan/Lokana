package com.dv.Lokana.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class CommentDto {
    private Long id;
    private String username;

    @NotEmpty
    private String message;
}
