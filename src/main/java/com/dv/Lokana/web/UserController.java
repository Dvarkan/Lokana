package com.dv.Lokana.web;

import com.dv.Lokana.dto.UserDto;
import com.dv.Lokana.entitys.User;
import com.dv.Lokana.mapper.UserMap;
import com.dv.Lokana.service.UserService;
import com.dv.Lokana.validations.ResponseErrorValidation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
@CrossOrigin
public class UserController {
    private final UserService userService;
    private final UserMap userMap;
    private final ResponseErrorValidation errorValidation;

    @GetMapping("/")
    public ResponseEntity<UserDto> getCurrentUser(Principal principal) {
        User user = userService.getCurrentUser(principal);
        UserDto userDto = userMap.map(user);
        return new ResponseEntity<>(userDto , HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getUserProfile(@PathVariable("id") String id) {
        User user = userService.findUserById(Long.parseLong(id));
        UserDto userDto = userMap.map(user);
        return new ResponseEntity<>(userDto , HttpStatus.OK);
    }

    @PostMapping("/update")
    public ResponseEntity<Object> updateUser(@Valid @RequestBody UserDto userDto,
                                             BindingResult bindingResult,
                                             Principal principal) {
        var error = errorValidation.mapValidationService(bindingResult);
        if (!ObjectUtils.isEmpty(error)) return error;

        User user = userService.updateUser(userDto, principal);
        UserDto updatedUser = userMap.map(user);
        return new ResponseEntity<>(updatedUser , HttpStatus.OK);
    }
}
