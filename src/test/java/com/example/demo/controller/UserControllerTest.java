package com.example.demo.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.example.demo.dto.AddressRequest;
import com.example.demo.dto.CreateCustomerRequest;
import com.example.demo.dto.UpdateUserRequest;
import com.example.demo.dto.UserResponse;
import com.example.demo.model.User;
import com.example.demo.service.UserService;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

  @Mock
  private UserService userService;

  @InjectMocks
  private UserController userController;

  private User user;
  private UserResponse userResponse;
  private CreateCustomerRequest createCustomerRequest;
  private UpdateUserRequest updateUserRequest;
  private final String userId = "123";

  @BeforeEach
  void setUp() {
    user = new User();
    user.setId(userId);
    user.setEmail("test@example.com");
    user.setName("Test User");

    userResponse = new UserResponse(
        userId,
        "test@example.com",
        "Test User One",
        "+1234567890",
        "TAX123",
        LocalDateTime.now(),
        LocalDateTime.now(),
        List.of()
    );

    createCustomerRequest = new CreateCustomerRequest(
        "test@example.com",
        "Test",
        "User",
        "One",
        LocalDate.of(1990, 1, 1),
        "+1234567890",
        "Password123!",
        List.of(new AddressRequest(1, "Home", "123 Main St", "US"))
    );

    updateUserRequest = new UpdateUserRequest(
        "update@example.com",
        "Updated",
        "Name",
        "Two",
        LocalDate.of(1992, 2, 2),
        "+0987654321"
    );
  }

  @Test
  void getAllUsers_ReturnsResponseEntity() {
    when(userService.findAll(null)).thenReturn(ResponseEntity.ok(List.of(user)));

    ResponseEntity<Object> response = userController.getAllUsers(null);

    assertNotNull(response);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    verify(userService, times(1)).findAll(null);
  }

  @Test
  void getUserById_ReturnsUser() {
    when(userService.findById(userId)).thenReturn(user);

    ResponseEntity<User> response = userController.getUserById(userId);

    assertNotNull(response);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(userId, response.getBody().getId());
    verify(userService, times(1)).findById(userId);
  }

  @Test
  void createUser_ReturnsUserResponse() throws Exception {
    when(userService.save(any(CreateCustomerRequest.class))).thenReturn(userResponse);

    UserResponse response = userController.createUser(createCustomerRequest);

    assertNotNull(response);
    assertEquals(userId, response.id());
    verify(userService, times(1)).save(any(CreateCustomerRequest.class));
  }

  @Test
  void patchUser_ReturnsUserResponse() {
    when(userService.update(eq(userId), any(UpdateUserRequest.class))).thenReturn(userResponse);

    UserResponse response = userController.patchUser(userId, updateUserRequest);

    assertNotNull(response);
    assertEquals(userId, response.id());
    verify(userService, times(1)).update(eq(userId), any(UpdateUserRequest.class));
  }

  @Test
  void deleteUser_ExecutesSuccessfully() {
    doNothing().when(userService).delete(userId);

    userController.deleteUser(userId);

    verify(userService, times(1)).delete(userId);
  }
}
