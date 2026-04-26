package com.example.demo.service;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import com.example.demo.dto.CreateCustomerRequest;
import com.example.demo.dto.UpdateUserRequest;
import com.example.demo.dto.UserResponse;
import com.example.demo.model.Address;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.utils.UserUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Service
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

  private final UserRepository userRepository;

  /**
   * @param sortedBy
   * @return
   */
  public ResponseEntity<Object> findAll(String sortedBy) {
    List<User> users;
    if (sortedBy != null && !sortedBy.isEmpty()) {
      Sort sort = Sort.by(sortedBy).ascending();
      users = userRepository.findAll(sort);
    } else {
      users = userRepository.findAll();
    }

    if (users.isEmpty()) {
      log.info("No se encontraron usuarios en la base de datos");
      return ResponseEntity.noContent().build();
    }

    return ResponseEntity.ok(users);
  }

  /**
   * Find by ID
   * 
   * @param id
   * @return
   */
  public User findById(String id) {
    return userRepository.findById(id)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
  }



  /**
   * Save User
   * 
   * @param user
   * @return
   * @throws Exception
   */
  @Transactional
  public UserResponse save(CreateCustomerRequest request) throws Exception {

    String encryptedPassword = UserUtils.encrypt(request.password());


    List<Address> userAddresses =
        request.AddressRequest().stream().map(addrReq -> Address.builder().name(addrReq.name())
            .street(addrReq.street()).countryCode(addrReq.countryCode()).build()).toList();

    User user = User.builder()
        .name(request.name())
        .lastName1(request.lastName1())
        .lastName2(request.lastName2())
        .birthDate(request.birthDate())
        .phone(request.phone())
        .password(encryptedPassword)
        .created_at(LocalDateTime.now())
        .update_at(LocalDateTime.now())
        .active(true)
        .email(request.email())
        .addresses(userAddresses).build();


    log.info("Request: {}", request.toString());

    return toResponse(userRepository.save(user));
  }


  /**
   * Save where id Obj request
   * 
   * @param id
   * @param updates
   * @return
   */
  @Transactional
  public UserResponse update(String id, UpdateUserRequest request) {
    User user = findById(id);
    user.setEmail(request.email());
    user.setName(request.name());
    user.setLastName1(request.lastName1());
    user.setLastName2(request.lastName2());
    user.setBirthDate(request.birthDate());
    user.setPhone(request.phone());
    user.setUpdate_at(LocalDateTime.now());
    return toResponse(userRepository.save(user));
  }

  @Transactional
  public void delete(String id) {
    User user = findById(id);
    user.setActive(false);
    userRepository.save(user);
    log.warn("Usuario Eliminado: {}", id);
  }

  /**
   * @param customer
   * @return
   */
  private UserResponse toResponse(User response) {
    String name = String.format("%s %s %s", response.getName(), response.getLastName1(),
        response.getLastName2());
    return new UserResponse(response.getId(), response.getEmail(), name, response.getPhone(),
        response.getTaxId(), response.getCreated_at(), response.getUpdate_at(),
        response.getAddresses());
  }
}
