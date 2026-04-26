package com.example.demo.dto;

import jakarta.validation.constraints.NotBlank;

public record AddressRequest(
    
    Integer id,
    @NotBlank(message = "El nombre es obligatorio")
     String name,
     @NotBlank(message = "La calle es obligatorio")
     String street,
     @NotBlank(message = "El codigo es obligatorio")
     String countryCode
    
    ) {

}
