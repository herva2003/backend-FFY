package com.puccampinas.backendp5noname.controllers.auth;

import com.puccampinas.backendp5noname.domain.RefreshToken;
import com.puccampinas.backendp5noname.domain.User;
import com.puccampinas.backendp5noname.dtos.ApiResponse;
import com.puccampinas.backendp5noname.dtos.LoginDTO;
import com.puccampinas.backendp5noname.dtos.SignupDTO;
import com.puccampinas.backendp5noname.dtos.TokenDTO;
import com.puccampinas.backendp5noname.repositories.RefreshTokenRepository;
import com.puccampinas.backendp5noname.repositories.UserRepository;
import com.puccampinas.backendp5noname.services.UserService;
import com.puccampinas.backendp5noname.services.auth.TokenService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthenticationController {
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    RefreshTokenRepository refreshTokenRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    TokenService jwtHelper;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    UserService userService;
    @Autowired
    private TokenService tokenService;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<TokenDTO>> login(@Valid @RequestBody LoginDTO dto) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(dto.getLogin(), dto.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        User user = (User) authentication.getPrincipal();

        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(HttpStatus.NOT_FOUND, "User not found", null));
        }

        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setOwner(user);
        refreshTokenRepository.save(refreshToken);

        String accessToken = jwtHelper.generateAccessToken(user);
        String refreshTokenString = jwtHelper.generateRefreshToken(user, refreshToken);

        TokenDTO tokenDTO = new TokenDTO(accessToken, refreshTokenString);

        return ResponseEntity
                .ok(new ApiResponse<>(HttpStatus.OK, "Login successful", tokenDTO));
    }

    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<TokenDTO>> signup(@Valid @RequestBody SignupDTO dto) {
        System.out.println(dto.toString());
        User user = new User(dto, passwordEncoder.encode(dto.getPassword()));
        userRepository.save(user);

        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setOwner(user);
        refreshTokenRepository.save(refreshToken);

        String accessToken = jwtHelper.generateAccessToken(user);
        String refreshTokenString = jwtHelper.generateRefreshToken(user, refreshToken);

        TokenDTO tokenDTO = new TokenDTO(accessToken, refreshTokenString);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ApiResponse<>(HttpStatus.CREATED, "User registered successfully", tokenDTO));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestBody TokenDTO dto) {
        String refreshTokenString = dto.getRefreshToken();
        if (jwtHelper.validateRefreshToken(refreshTokenString) &&
                refreshTokenRepository.existsById(jwtHelper.getTokenIdFromRefreshToken(refreshTokenString))) {
            // valid and exists in db
            refreshTokenRepository.deleteById(jwtHelper.getTokenIdFromRefreshToken(refreshTokenString));
            return ResponseEntity.ok().build();
        }

        throw new BadCredentialsException("invalid token");
    }

    @PostMapping("/logout-all")
    public ResponseEntity<?> logoutAll(@RequestBody TokenDTO dto) {
        String refreshTokenString = dto.getRefreshToken();
        if (jwtHelper.validateRefreshToken(refreshTokenString) &&
                refreshTokenRepository.existsById(jwtHelper.getTokenIdFromRefreshToken(refreshTokenString))) {
            // valid and exists in db
            refreshTokenRepository.deleteByOwner_Id(jwtHelper.getUserIdFromRefreshToken(refreshTokenString));
            return ResponseEntity.ok().build();
        }

        throw new BadCredentialsException("invalid token");
    }

    @PostMapping("/access-token")
    public ResponseEntity<?> accessToken(@RequestBody TokenDTO dto) {
        String refreshTokenString = dto.getRefreshToken();
        if (jwtHelper.validateRefreshToken(refreshTokenString) &&
                refreshTokenRepository.existsById(jwtHelper.getTokenIdFromRefreshToken(refreshTokenString))) {
            // valid and exists in db

            User user = userService.findById(jwtHelper.getUserIdFromRefreshToken(refreshTokenString));
            String accessToken = jwtHelper.generateAccessToken(user);

            return ResponseEntity.ok(new TokenDTO(accessToken, refreshTokenString));
        }

        throw new BadCredentialsException("invalid token");
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<?> refreshToken(@RequestBody TokenDTO dto) {
        String refreshTokenString = dto.getRefreshToken();
        if (jwtHelper.validateRefreshToken(refreshTokenString) &&
                refreshTokenRepository.existsById(jwtHelper.getTokenIdFromRefreshToken(refreshTokenString))) {
            // valid and exists in db

            refreshTokenRepository.deleteById(jwtHelper.getTokenIdFromRefreshToken(refreshTokenString));

            User user = userService.findById(jwtHelper.getUserIdFromRefreshToken(refreshTokenString));

            RefreshToken refreshToken = new RefreshToken();
            refreshToken.setOwner(user);
            refreshTokenRepository.save(refreshToken);

            String accessToken = jwtHelper.generateAccessToken(user);
            String newRefreshTokenString = jwtHelper.generateRefreshToken(user, refreshToken);

            return ResponseEntity.ok(new TokenDTO(accessToken, newRefreshTokenString));
        }

        throw new BadCredentialsException("invalid token");
    }
}