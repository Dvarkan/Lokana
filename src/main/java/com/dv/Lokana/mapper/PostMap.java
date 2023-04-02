package com.dv.Lokana.mapper;

import com.dv.Lokana.dto.PostDto;
import com.dv.Lokana.entitys.Post;
import org.springframework.stereotype.Component;

@Component
public class PostMap implements Mapper<Post, PostDto>{
    @Override
    public PostDto map(Post object) {
        return PostDto.builder()
                .caption(object.getCaption())
                .id(object.getId())
                .likes(object.getLikes())
                .location(object.getLocation())
                .title(object.getTitle())
                .username(object.getUser().getUsername())
                .usersLiked(object.getLikedUsers())
                .build();
    }
}
