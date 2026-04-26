package com.example.demo.controller;

import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.example.demo.model.User;
import com.example.demo.service.UserService;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/v1/users")
@Slf4j
public class UserController {

  @Autowired
  private UserService userService;

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
  public ResponseEntity<Object> createUser(@RequestBody User user) throws Exception {
    log.info("POST -> /api/v1/users Request {}", user.toString());
    return userService.save(user);
  }

  /**
   * Controller delete User for ID
   * 
   * @param id
   * @return
   */
  @DeleteMapping("/{id}")
  public ResponseEntity<Object> deleteUser(@PathVariable String id) {
    log.info("DELETE -> /api/v1/users/{}", id);
    return userService.deleteById(id);
  }

  /**
   * Controller update for ID
   * 
   * @param id
   * @param updates
   * @return
   */
  @PatchMapping("/{id}")
  public ResponseEntity<User> patchUser(@PathVariable String id,
      @RequestBody Map<String, Object> updates) {
    log.info("PATCH -> /api/v1/users/{}", id);
    return userService.save(id, updates);
  }

}
