package com.example.demo.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import com.example.demo.dto.CreateCustomerRequest;
import com.example.demo.dto.UpdateUserRequest;
import com.example.demo.dto.UserResponse;
import com.example.demo.model.User;
import com.example.demo.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {

  private final UserService userService;

  /**
   * Controller all users sortedBy
   * 
   * @param sortedBy
   * @return
   */
  @GetMapping
  public ResponseEntity<Object> getAllUsers(@RequestParam(required = false) String sortedBy) {
    log.info("GET -> /api/v1/users");
    return userService.findAll(sortedBy);
  }

  /**
   * Controller get User for ID
   * 
   * @param id
   * @return
   */
  @GetMapping("/{id}")
  public ResponseEntity<User> getUserById(@PathVariable String id) {
    log.info("GET -> /api/v1/users/{}", id);
    User user = userService.findById(id);
    return ResponseEntity.ok(user);
  }

  /**
   * Controller build new User
   * 
   * @param user
   * @return
   * @throws Exception
   */
  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public UserResponse createUser(@Valid @RequestBody CreateCustomerRequest request)
      throws Exception {
    log.info("POST -> /api/v1/users Request {}", request.toString());
    return userService.save(request);
  }

  /**
   * Controller delete User for ID
   * 
   * @param id
   * @return
   */
  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void deleteUser(@PathVariable String id) {
    log.info("DELETE -> /api/v1/users/{}", id);
    userService.delete(id);
  }

  /**
   * Controller update for ID
   * 
   * @param id
   * @param updates
   * @return
   */
  @PatchMapping("/{id}")
  public UserResponse patchUser(@PathVariable String id,
      @Valid @RequestBody UpdateUserRequest requet) {
    log.info("PATCH -> /api/v1/users/{}", id);
    return userService.update(id, requet);
  }

}
