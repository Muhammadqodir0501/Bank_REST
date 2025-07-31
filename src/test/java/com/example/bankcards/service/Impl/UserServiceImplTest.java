package com.example.bankcards.service.Impl;

import com.example.bankcards.dto.response.UserResponse;
import com.example.bankcards.entity.User;
import com.example.bankcards.entity.enums.UserStatus;
import com.example.bankcards.repository.UserRepository;
import com.example.bankcards.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    private User testUser;


    @BeforeEach
    void setUp() {
        testUser = User.builder()
                .id(1L)
                .username("testuser")
                .status(UserStatus.ACTIVE)
                .build();
    }

    @Test
    void getAllUsers() {

        Page<User> userPage = new PageImpl<>(Collections.singletonList(testUser));
        when(userRepository.findAll(any(PageRequest.class))).thenReturn(userPage);

        Page<UserResponse> result = userService.findAllUsers(0, 5);

        assertNotNull(result);
        assertEquals(1, result.getContent().get(0).getUsername());
        assertEquals("testuser", result.getContent().get(0).getUsername());
    }

    @Test
    void changeUserStatus_shouldUpdateStatus() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));

        UserResponse result = userService.changeUserStatus(1L, UserStatus.BLOCKED);

        assertNotNull(result);
        assertEquals(UserStatus.BLOCKED, result.getStatus());
        assertEquals(UserStatus.BLOCKED, testUser.getStatus());

    }

    @Test
    void changeUserStatus_shouldThrowExceptionWhenUserNotFound() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        org.junit.jupiter.api.Assertions.assertThrows(RuntimeException.class, () -> {
            userService.changeUserStatus(99L, UserStatus.BLOCKED);
        });
    }
}
