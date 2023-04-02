package com.dv.Lokana.service;

import com.dv.Lokana.dto.UserDto;
import com.dv.Lokana.entitys.User;
import com.dv.Lokana.exceptions.UserExistException;
import com.dv.Lokana.payload.request.SignupRequest;
import com.dv.Lokana.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;

import static com.dv.Lokana.entitys.enums.Role.USER;

@Service
@RequiredArgsConstructor
public class UserService {
    private static final Logger LOG = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public User createUser(SignupRequest request) {
        User user = new User();
        user.setEmail(request.getEmail());
        user.setFirstname(request.getFirstname());
        user.setLastname(request.getLastname());
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.getRoles().add(USER);

        try {
            LOG.info("Save user {}", request.getEmail());
            return userRepository.save(user);
        }catch (Exception e) {
            LOG.error("Error during registration {}", e.getMessage());
            throw new UserExistException("The user " + user.getUsername() + " already exist. Please check credentials");
        }
    }

    public User updateUser(UserDto userDto, Principal principal) {
       User user = findUserByPrincipal(principal);

       user.setFirstname(userDto.getFirstname());
       user.setLastname(userDto.getLastname());
       user.setUsername(userDto.getUsername());
       user.setBio(userDto.getBio());

       LOG.info("Update user");

       return userRepository.save(user);
    }

    public User findUserById(Long id) {
        return userRepository.findUserById(id)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    public User getCurrentUser(Principal principal) {
        return findUserByPrincipal(principal);
    }

    private User findUserByPrincipal(Principal principal) {
        return userRepository.findUserByUsername(principal.getName())
                .orElseThrow(() -> new UsernameNotFoundException("Username not found with username " + principal.getName()));
    }
}
