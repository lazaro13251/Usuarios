package com.example.demo.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;
import com.example.demo.dto.AddressRequest;
import com.example.demo.dto.CreateCustomerRequest;
import com.example.demo.dto.UpdateUserRequest;
import com.example.demo.dto.UserResponse;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

  @InjectMocks
  private UserService userService;
  @Mock
  private UserRepository userRepository;

  private User user;
  private CreateCustomerRequest createUser;
  private UpdateUserRequest updateUser;
  private User resposeUser;
  private final String userId = "123";

  @BeforeEach
  void setUp() {
    user = new User();
    user.setId(userId);
    user.setEmail("test@example.com");
    user.setPassword("password123");
    user.setName("Test");
    user.setLastName1("User");
    user.setLastName2("One");
    user.setBirthDate(LocalDate.of(1990, 1, 1));
    user.setCreated_at(LocalDateTime.now());
    user.setUpdate_at(LocalDateTime.now());
    
    createUser = new CreateCustomerRequest(
        "test@example.com",
        "Test",
        "User",
        "One",
        LocalDate.of(1990, 1, 1),
        "+1234567890",
        "Password123!",
        List.of(new AddressRequest(1, "Home", "123 Main St", "US"))
    );

    updateUser = new UpdateUserRequest(
        "update@example.com",
        "Updated",
        "Name",
        "Two",
        LocalDate.of(1992, 2, 2),
        "+0987654321"
    );

    resposeUser = user;
  }

  @Test
  void save_ShouldEncryptPasswordAndSaveUser() throws Exception {
    when(userRepository.save(any(User.class))).thenReturn(resposeUser);
    UserResponse savedUser = userService.save(createUser);
    
    assertNotNull(savedUser);
    assertEquals(resposeUser.getId(), savedUser.id());
    verify(userRepository, times(1)).save(any(User.class));
  }

  @Test
  void findAll_SortedByEmail() {
    List<User> mockUsers = List.of(user, new User());
    Sort sort = Sort.by("email").ascending();
    when(userRepository.findAll(sort)).thenReturn(mockUsers);
    
    ResponseEntity<Object> result = userService.findAll("email");
    
    assertNotNull(result);
    assertEquals(HttpStatus.OK, result.getStatusCode());
    verify(userRepository, times(1)).findAll(sort);
  }

  @Test
  void findAll_Unsorted() {
    List<User> mockUsers = List.of(user, new User());
    when(userRepository.findAll()).thenReturn(mockUsers);
    
    ResponseEntity<Object> result = userService.findAll(null);
    
    assertNotNull(result);
    assertEquals(HttpStatus.OK, result.getStatusCode());
    verify(userRepository, times(1)).findAll();
  }

  @Test
  void findAll_EmptyList_ReturnsNoContent() {
    when(userRepository.findAll()).thenReturn(List.of());
    
    ResponseEntity<Object> result = userService.findAll("");
    
    assertNotNull(result);
    assertEquals(HttpStatus.NO_CONTENT, result.getStatusCode());
    verify(userRepository, times(1)).findAll();
  }

  @Test
  void findById_ShouldReturnUser_WhenUserExists() {
    when(userRepository.findById(userId)).thenReturn(Optional.of(user));
    
    User result = userService.findById(userId);
    
    assertNotNull(result);
    assertEquals(userId, result.getId());
    assertEquals("Test", result.getName());
  }

  @Test
  void findById_ShouldThrowException_WhenUserDoesNotExist() {
    String invalidId = "999";
    when(userRepository.findById(invalidId)).thenReturn(Optional.empty());
    
    ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
      userService.findById(invalidId);
    });
    
    assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
    assertEquals("User not found", exception.getReason());
  }

  @Test
  void update_ShouldUpdateAndReturnUser() {
    when(userRepository.findById(userId)).thenReturn(Optional.of(user));
    when(userRepository.save(any(User.class))).thenReturn(user);

    UserResponse result = userService.update(userId, updateUser);

    assertNotNull(result);
    verify(userRepository, times(1)).findById(userId);
    verify(userRepository, times(1)).save(any(User.class));
  }

  @Test
  void delete_ShouldDeactivateUser() {
    when(userRepository.findById(userId)).thenReturn(Optional.of(user));
    when(userRepository.save(any(User.class))).thenReturn(user);

    userService.delete(userId);

    verify(userRepository, times(1)).findById(userId);
    verify(userRepository, times(1)).save(any(User.class));
  }
}
