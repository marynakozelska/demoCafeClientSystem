package com.example.democafeclientsystem.service;

import com.example.democafeclientsystem.dto.UserDTO;
import com.example.democafeclientsystem.entities.User;
import com.example.democafeclientsystem.enums.Role;
import com.example.democafeclientsystem.repositories.RoleRepository;
import com.example.democafeclientsystem.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@AllArgsConstructor
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }
        return user;
    }

    public UserDTO getUserByAuth(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return userToDto(user);
    }

//    ADMIN MANAGING

    public UserDTO manageGetUser(int id) {
        User user = userRepository.findById(id).get();
        return userToDto(user);
    }

    public UserDTO manageUpdateUser(int id, String roleInput) {

        var user = userRepository.findById(id).get();
        Role role = roleRepository.findByName(roleInput);

        user.setRole(role);
        userRepository.save(user);

        return userToDto(user);
    }

    public List<UserDTO> manageGetAllUsers() {
        List<User> volunteers = new ArrayList<>();
        userRepository.findAll().iterator().forEachRemaining(volunteers::add);

        return volunteers
                .stream()
                .map(this::userToDto)
                .toList();
    }


//    DTO

    private User dtoToUser(UserDTO userDTO) {
        User user = new User();
        user.setId(userDTO.getId());
        user.setFirstName(userDTO.getFirstName());
        user.setEmail(userDTO.getEmail());
        user.setPassword(userDTO.getPassword());
        user.setRole(
                roleRepository.findByName(userDTO.getRole())
        );

        return user;
    }

    private UserDTO userToDto(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setFirstName(user.getFirstName());
        userDTO.setEmail(user.getEmail());
        userDTO.setPassword(userDTO.getPassword());
        userDTO.setRole(user.getRole().getName());

        return userDTO;
    }
}
