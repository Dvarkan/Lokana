package com.dv.Lokana.mapper;

import com.dv.Lokana.dto.UserDto;
import com.dv.Lokana.entitys.User;

public class UserMapper implements Mapper<User, UserDto>{
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
