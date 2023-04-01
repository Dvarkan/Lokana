package com.dv.Lokana.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CommentDto {
    private Long id;
    private String username;

    @NotEmpty
    private String message;
}
