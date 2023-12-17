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
        Optional<User> userOptional = userRepository.findById(id);

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            return userToDto(user);
        } else {
            throw new UsernameNotFoundException(String.format("User ID = %s doesn't exist.", id));
        }
    }

    public UserDTO manageUpdateUser(int id, String roleInput) {
        Optional<User> userOptional = userRepository.findById(id);

        if (userOptional.isPresent()) {
            var user = userOptional.get();
            Role role = roleRepository.findByName(roleInput);

            user.setRole(role);
            userRepository.save(user);

            return userToDto(user);
        } else {
            throw new UsernameNotFoundException(String.format("User ID = %s doesn't exist.", id));
        }
    }

    public List<UserDTO> manageGetAllUsers() {
        List<User> users = new ArrayList<>();
        userRepository.findAll().iterator().forEachRemaining(users::add);

        return users
                .stream()
                .map(this::userToDto)
                .toList();
    }


//    DTO

    private UserDTO userToDto(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setFirstName(user.getFirstName());
        userDTO.setEmail(user.getEmail());
        userDTO.setPassword(user.getPassword());
        userDTO.setRole(user.getRole().getName());

        return userDTO;
    }
}
