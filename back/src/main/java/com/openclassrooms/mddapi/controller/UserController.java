package com.openclassrooms.mddapi.controller;

import com.openclassrooms.mddapi.assembler.UserAssembler;
import com.openclassrooms.mddapi.dto.request.UpdateProfileRequest;
import com.openclassrooms.mddapi.dto.response.UserProfileResponse;
import com.openclassrooms.mddapi.model.User;
import com.openclassrooms.mddapi.security.SecurityUtils;
import com.openclassrooms.mddapi.service.UserService;
import com.openclassrooms.mddapi.service.UserService.UpdateResult;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for user profile endpoints.
 */
@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

  private final UserService userService;
  private final UserAssembler userAssembler;

  /**
   * Gets current user's profile with subscriptions.
   *
   * @param authentication current user authentication
   * @return user profile with subscription list
   */
  @GetMapping("/profile")
  @Transactional(readOnly = true)
  public ResponseEntity<UserProfileResponse> getProfile(Authentication authentication) {
    Long userId = SecurityUtils.getUserId(authentication);
    User user = userService.getById(userId);

    return ResponseEntity.ok(userAssembler.assembleUserProfile(user));
  }

  /**
   * Updates current user's profile. All fields are optional; only provided fields will be updated.
   *
   * @param request        profile update data
   * @param authentication current user authentication
   * @return 200 with updated profile if changes made, 204 if no changes
   */
  @PutMapping("/profile")
  public ResponseEntity<UserProfileResponse> updateProfile(
      @Valid @RequestBody UpdateProfileRequest request,
      Authentication authentication
  ) {
    Long userId = SecurityUtils.getUserId(authentication);

    UpdateResult result = userService.updateProfile(
        userId,
        request.username(),
        request.email(),
        request.password()
    );

    if (result.hasChanges()) {
      return ResponseEntity.ok(userAssembler.assembleUserProfile(result.user()));
    } else {
      return ResponseEntity.noContent().build();
    }
  }
}