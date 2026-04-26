package com.example.demo.service;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.utils.UserUtils;
import lombok.extern.slf4j.Slf4j;

/**
 * Service
 */
@Service
@Slf4j
public class UserService {

  @Autowired
  private UserRepository userRepository;

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
        .orElseThrow(() -> new RuntimeException("Usuario no encontrado con id: " + id));
  }



  /**
   * Save User
   * 
   * @param user
   * @return
   * @throws Exception
   */
  public ResponseEntity<Object> save(User user) throws Exception {
    String encryptedPassword = UserUtils.encrypt(user.getPassword());
    user.setPassword(encryptedPassword);
    log.info("Request: {}", user.toString());
    User savedUser = userRepository.save(user);
    log.info("Documento guardado");
    return ResponseEntity.status(201).body(savedUser);
  }

  /**
   * Delete for ID
   * 
   * @param id
   * @return
   */
  public ResponseEntity<Object> deleteById(String id) {
    try {
      if (userRepository.existsById(id)) {
        userRepository.deleteById(id);
        log.info("Usuario eliminado exitosamente: {}", id);
        return ResponseEntity.ok("Usuario eliminado correctamente");
      } else {
        log.warn("Intento de eliminar usuario inexistente: {}", id);
        return ResponseEntity.notFound().build();
      }
    } catch (IllegalArgumentException e) {
      log.error("Formato de ID inválido para borrado: {}", id);
      return ResponseEntity.badRequest().body("ID con formato inválido");
    }
  }


  /**
   * Save where id Obj request
   * 
   * @param id
   * @param updates
   * @return
   */
  public ResponseEntity<User> save(String id, Map<String, Object> updates) {
    log.info("--- PATCH Dinámico para ID: {} ---", id);
    try {
      return userRepository.findById(id).map(existingUser -> {
        updates.forEach((key, value) -> {
          try {
            Field field = User.class.getDeclaredField(key);
            field.setAccessible(true);
            field.set(existingUser, value);
            log.info("Campo '{}' actualizado a: {}", key, value);
          } catch (NoSuchFieldException e) {
            log.warn("El campo '{}' no existe en el objeto User. Ignorando.", key);
          } catch (IllegalAccessException e) {
            log.error("No se pudo acceder al campo '{}'", key);
          }
        });
        User updatedUser = userRepository.save(existingUser);
        return ResponseEntity.ok(updatedUser);
      }).orElse(ResponseEntity.notFound().build());

    } catch (IllegalArgumentException e) {
      return ResponseEntity.badRequest().build();
    }
  }
}
