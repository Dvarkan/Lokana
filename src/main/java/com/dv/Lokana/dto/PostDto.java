package com.dv.Lokana.dto;

import lombok.Builder;
import lombok.Data;

import java.util.Set;

@Data
@Builder
public class PostDto {
    private Long id;
    private String title;
    private String caption;
    private String username;
    private String location;
    private Integer likes;
    private Set<String> usersLiked;
}
