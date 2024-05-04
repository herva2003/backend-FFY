package com.puccampinas.backendp5noname.controllers.auth;

import com.puccampinas.backendp5noname.domain.User;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
@SecurityRequirement(name = "bearer-key")
public class UserController {


    @GetMapping("/me")
    public ResponseEntity<User> currentUser(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(user);
    }
}
