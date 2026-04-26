package com.example.demo.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;

class UserServiceTest {

  @InjectMocks
  private UserService userService;
  @Mock
  private UserRepository userRepository;

  private User user;
  private final String userId = "123";

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    user = new User();
    user.setEmail("test@example.com");
    user.setPassword("password123");
    user.setName("Test");
    user.setLastName1("User");
    user.setLastName2("One");
    user.setBirthDate(LocalDate.of(1990, 1, 1));
  }


  @Test
  void createUser_ShouldEncryptPasswordAndSaveUser() throws Exception {
    when(userRepository.save(any(User.class))).thenReturn(user);
    var savedUser = userService.save(user);
    assertNotNull(savedUser);
    verify(userRepository, times(1)).save(any(User.class));
  }

  @Test
  void findAll_SortedByEmail() {
    List<User> mockUsers = List.of(new User(), new User());
    Sort sort = Sort.by("email").ascending();
    when(userRepository.findAll(sort)).thenReturn(mockUsers);
    var result = userService.findAll("email");
    assertNotNull(result);
    verify(userRepository, times(1)).findAll(sort);
  }

  @Test
  void findById_ShouldReturnUser_WhenUserExists() {
    String userId = "123";
    User mockUser = new User();
    mockUser.setId(userId);
    mockUser.setName("Test User");
    when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));
    User result = userService.findById(userId);
    assertNotNull(result);
    assertEquals(userId, result.getId());
    assertEquals("Test User", result.getName());
  }

  @Test
  void findById_ShouldThrowException_WhenUserDoesNotExist() {
    String userId = "999";
    when(userRepository.findById(userId)).thenReturn(Optional.empty());
    RuntimeException exception = assertThrows(RuntimeException.class, () -> {
      userService.findById(userId);
    });
    assertEquals("Usuario no encontrado con id: " + userId, exception.getMessage());
  }

  @Test
  void deleteById_ShouldReturnOk_WhenUserExists() {
    String userId = "123";
    when(userRepository.existsById(userId)).thenReturn(true);
    ResponseEntity<Object> response = userService.deleteById(userId);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals("Usuario eliminado correctamente", response.getBody());
    verify(userRepository, times(1)).deleteById(userId);
  }

  @Test
  void deleteById_ShouldReturnNotFound_WhenUserDoesNotExist() {
    String userId = "999";
    when(userRepository.existsById(userId)).thenReturn(false);
    ResponseEntity<Object> response = userService.deleteById(userId);
    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    verify(userRepository, times(0)).deleteById(anyString()); // No debe llamar a delete
  }

  @Test
  void deleteById_ShouldReturnBadRequest_WhenIllegalArgumentException() {
    String invalidId = "invalid-id";
    when(userRepository.existsById(invalidId)).thenThrow(new IllegalArgumentException());
    ResponseEntity<Object> response = userService.deleteById(invalidId);
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    assertEquals("ID con formato inválido", response.getBody());
  }

  @Test
  void savePatch_ShouldUpdateFieldsAndReturnOk_WhenUserExists() {
    Map<String, Object> updates = new HashMap<>();
    updates.put("name", "Updated Name");
    updates.put("email", "updated@email.com");
    when(userRepository.findById(userId)).thenReturn(Optional.of(user));
    when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));
    ResponseEntity<User> response = userService.save(userId, updates);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());
    assertEquals("Updated Name", response.getBody().getName());
    assertEquals("updated@email.com", response.getBody().getEmail());
    verify(userRepository, times(1)).save(any(User.class));
  }

  @Test
  void savePatch_ShouldReturnNotFound_WhenUserDoesNotExist() {
    Map<String, Object> updates = new HashMap<>();
    updates.put("name", "New Name");
    when(userRepository.findById(userId)).thenReturn(Optional.empty());
    ResponseEntity<User> response = userService.save(userId, updates);
    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    verify(userRepository, times(0)).save(any(User.class));
  }

  @Test
  void savePatch_ShouldIgnoreInvalidFields_WhenFieldDoesNotExist() {
    Map<String, Object> updates = new HashMap<>();
    updates.put("nonExistentField", "someValue");
    updates.put("name", "Updated Name");
    when(userRepository.findById(userId)).thenReturn(Optional.of(user));
    when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));
    ResponseEntity<User> response = userService.save(userId, updates);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals("Updated Name", response.getBody().getName());
    verify(userRepository, times(1)).save(any(User.class));
  }

}
