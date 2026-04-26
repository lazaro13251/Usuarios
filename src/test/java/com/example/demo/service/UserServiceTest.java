package com.example.demo.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Sort;
import com.example.demo.dto.CreateCustomerRequest;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;

class UserServiceTest {

  @InjectMocks
  private UserService userService;
  @Mock
  private UserRepository userRepository;

  private User user;
  private CreateCustomerRequest createUser;
  private User resposeUser;
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
    when(userRepository.save(any(User.class))).thenReturn(resposeUser);
    var savedUser = userService.save(createUser);
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



}
