package com.example.demo.dto;

import java.time.LocalDate;
import java.util.List;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record CreateCustomerRequest(


    @NotBlank(message = "El email es obligatorio") @Email(
        message = "El formato del email no es válido") String email,

    @NotBlank(message = "El nombre es obligatorio") @Size(min = 2, max = 50) String name,

    @NotBlank(message = "El primer apellido es obligatorio") String lastName1,

    String lastName2,

    @NotNull(message = "La fecha de nacimiento es obligatoria") @Past(
        message = "La fecha de nacimiento debe ser una fecha pasada") LocalDate birthDate,

    @Pattern(regexp = "^\\+?[0-9]{7,15}$",
        message = "El teléfono debe tener un formato válido") String phone,

    @NotBlank(message = "La contraseña es obligatoria") @Size(min = 8,
        message = "La contraseña debe tener al menos 8 caracteres") @Pattern(
            regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?]).*$",
            message = "La contraseña debe contener al menos una mayúscula, una minúscula, un número y un carácter especial") String password,

    @NotNull(
        message = "La lista de direcciones no puede ser nula") @Valid List<AddressRequest> AddressRequest

) {

}
