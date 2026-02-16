package com.openclassrooms.mddapi.dto.request;

import com.openclassrooms.mddapi.validation.UserConstraints.ValidEmail;
import com.openclassrooms.mddapi.validation.UserConstraints.ValidPassword;
import com.openclassrooms.mddapi.validation.UserConstraints.ValidUsername;

/**
 * DTO for updating user profile. All fields are optional.
 */
public record UpdateProfileRequest(
    @ValidUsername
    String username,

    @ValidEmail
    String email,

    @ValidPassword
    String password
) {

}
