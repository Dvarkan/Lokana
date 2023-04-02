package com.dv.Lokana.mapper;

import com.dv.Lokana.dto.UserDto;
import com.dv.Lokana.entitys.User;
import org.springframework.stereotype.Component;

@Component
public class UserMap implements Mapper<User, UserDto>{
    @Override
    public UserDto map(User object) {
        return UserDto.builder()
                .bio(object.getBio())
                .id(object.getId())
                .firstname(object.getFirstname())
                .lastname(object.getLastname())
                .build();
    }
}
