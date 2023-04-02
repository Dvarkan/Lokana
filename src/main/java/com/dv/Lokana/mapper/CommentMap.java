package com.dv.Lokana.mapper;

import com.dv.Lokana.dto.CommentDto;
import com.dv.Lokana.entitys.Comment;
import org.springframework.stereotype.Component;

@Component
public class CommentMap implements Mapper<Comment, CommentDto>{
    @Override
    public CommentDto map(Comment object) {
        return CommentDto.builder()
                .id(object.getId())
                .message(object.getMessage())
                .username(object.getUsername())
                .build();
    }
}
