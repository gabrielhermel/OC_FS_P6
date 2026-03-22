package com.openclassrooms.mddapi.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Composed validation constraints for user-related fields.
 */
public final class UserConstraints {

  private UserConstraints() {
  }

  @Documented
  @Constraint(validatedBy = {})
  @Target({ElementType.FIELD, ElementType.PARAMETER})
  @Retention(RetentionPolicy.RUNTIME)
  @Size(min = 8, message = "Le mot de passe doit contenir au moins 8 caractères")
  @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!]).*$",
      message = "Le mot de passe doit contenir au moins un chiffre, une lettre minuscule, "
          + "une lettre majuscule et un caractère spécial")
  public @interface ValidPassword {

    String message() default "Mot de passe non valide";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
  }

  @Documented
  @Constraint(validatedBy = {})
  @Target({ElementType.FIELD, ElementType.PARAMETER})
  @Retention(RetentionPolicy.RUNTIME)
  @Size(min = 1, max = 50, message = "Le nom d'utilisateur doit contenir entre 1 et 50 caractères")
  public @interface ValidUsername {

    String message() default "Nom d'utilisateur non valide";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
  }

  @Documented
  @Constraint(validatedBy = {})
  @Target({ElementType.FIELD, ElementType.PARAMETER})
  @Retention(RetentionPolicy.RUNTIME)
  @Email(message = "L'email doit être valide")
  @Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$",
      message = "L'email doit contenir un domaine valide avec un point")
  @Size(max = 255, message = "L'email ne doit pas dépasser 255 caractères")
  public @interface ValidEmail {

    String message() default "Email non valide";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
  }
}