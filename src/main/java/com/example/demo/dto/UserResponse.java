package com.example.demo.dto;

import java.time.LocalDateTime;
import java.util.List;
import com.example.demo.model.Address;

public record UserResponse(
    
    String id, 
    String email,
    String name,
    String phone, 
    String getTaxId,
    LocalDateTime created_at,
    LocalDateTime update_at,
    List<Address> addresses

) {

}
