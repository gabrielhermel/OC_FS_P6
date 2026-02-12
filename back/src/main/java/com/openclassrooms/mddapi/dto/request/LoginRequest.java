package com.openclassrooms.mddapi.dto.request;

import jakarta.validation.constraints.NotBlank;

/**
 * DTO for login requests.
 */
public record LoginRequest(
    @NotBlank(message = "Le nom d'utilisateur ou l'email est requis")
    String usernameOrEmail,

    @NotBlank(message = "Le mot de passe est requis")
    String password
) {

}