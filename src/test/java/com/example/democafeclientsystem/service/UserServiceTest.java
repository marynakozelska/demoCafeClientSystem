package com.example.democafeclientsystem.service;

import com.example.democafeclientsystem.dto.UserDTO;
import com.example.democafeclientsystem.entities.User;
import com.example.democafeclientsystem.enums.Role;
import com.example.democafeclientsystem.repositories.RoleRepository;
import com.example.democafeclientsystem.repositories.UserRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    UserRepository repository;
    @Mock
    RoleRepository roleRepository;
    @InjectMocks
    private UserService underTest;

    static User user;
    static Role role;
    static Authentication authentication;

    @BeforeAll
    static void beforeAll() {
        role = new Role();
        role.setName("USER");

        user = User
                .builder()
                .id(1)
                .firstName("Andriy")
                .email("andriy@gmail.com")
                .role(role)
                .build();
        authentication = new UsernamePasswordAuthenticationToken(user, null);
    }

    @Test
    void loadUserByUsername() {
        when(repository.findByEmail("andriy@gmail.com")).thenReturn(user);

        UserDetails result = underTest.loadUserByUsername("andriy@gmail.com");

        assertThat(result).isEqualTo(user);
    }

    @Test
    void getUserByAuth() {
        UserDTO result = underTest.getUserByAuth(authentication);

        assertThat(result.getId()).isEqualTo(user.getId());
        assertThat(result.getFirstName()).isEqualTo(user.getFirstName());
        assertThat(result.getRole()).isEqualTo(user.getRole().getName());
        assertThat(result.getPassword()).isEqualTo(user.getPassword());
    }

    @Test
    void manageGetUser() {
        when(repository.findById(1)).thenReturn(Optional.ofNullable(user));

        UserDTO result = underTest.manageGetUser(1);

        assertThat(result.getId()).isEqualTo(user.getId());
        assertThat(result.getFirstName()).isEqualTo(user.getFirstName());
        assertThat(result.getRole()).isEqualTo(user.getRole().getName());
        assertThat(result.getPassword()).isEqualTo(user.getPassword());
    }

    @Test
    void manageUpdateUser() {
        Role adminRole = new Role();
        adminRole.setName("ADMIN");

        when(repository.findById(1)).thenReturn(Optional.ofNullable(user));
        when(roleRepository.findByName("ADMIN")).thenReturn(adminRole);

        UserDTO result = underTest.manageUpdateUser(1, "ADMIN");

        assertThat(result.getId()).isEqualTo(user.getId());
        assertThat(result.getFirstName()).isEqualTo(user.getFirstName());
        assertThat(result.getRole()).isEqualTo("ADMIN");
        assertThat(result.getPassword()).isEqualTo(user.getPassword());
    }

    @Test
    void manageGetAllUsers() {
        List<User> userList = new ArrayList<>();
        userList.add(User
                .builder()
                .id(2)
                .firstName("User1")
                .email("user1@gmail.com")
                .role(role)
                .password("passwordd12")
                .build());
        userList.add(User
                .builder()
                .id(3)
                .firstName("User2")
                .email("user2@gmail.com")
                .role(role)
                .password("passWord1")
                .build());

        when(repository.findAll()).thenReturn(userList);

        List<UserDTO> result = underTest.manageGetAllUsers();

        assertThat(result.size()).isEqualTo(userList.size());
        assertThat(result.get(0).getId()).isEqualTo(userList.get(0).getId());
        assertThat(result.get(0).getFirstName()).isEqualTo(userList.get(0).getFirstName());
        assertThat(result.get(0).getEmail()).isEqualTo(userList.get(0).getEmail());
        assertThat(result.get(0).getPassword()).isEqualTo(userList.get(0).getPassword());
        assertThat(result.get(1).getId()).isEqualTo(userList.get(1).getId());
        assertThat(result.get(1).getFirstName()).isEqualTo(userList.get(1).getFirstName());
        assertThat(result.get(1).getEmail()).isEqualTo(userList.get(1).getEmail());
        assertThat(result.get(1).getPassword()).isEqualTo(userList.get(1).getPassword());
    }
}