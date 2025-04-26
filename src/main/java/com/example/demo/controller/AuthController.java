package com.example.demo.controller;

import com.example.demo.dto.LoginDto;
import com.example.demo.dto.RegisterDto;
import com.example.demo.filter.JwtUtil;
import com.example.demo.model.UserModel;
import com.example.demo.repository.AuthRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

import java.util.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthRepository authRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthController(AuthRepository authRepository, PasswordEncoder passwordEncoder) {
        this.authRepository = authRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/register")
    public String registerUser(@Valid @RequestBody RegisterDto registerDto) {
        Optional<UserModel> existingUser = authRepository.findByUsername(registerDto.getUsername());
        if (existingUser.isPresent()) {
            return "Użytkownik już istnieje!";
        }

        String encryptedPassword = passwordEncoder.encode(registerDto.getPassword());

        UserModel newUser = new UserModel(null, registerDto.getUsername(), registerDto.getEmail(), encryptedPassword);

        authRepository.save(newUser);

        return "Rejestracja zakończona sukcesem!";
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody Map<String, String> user, HttpServletResponse response) {
        String username = user.get("username");
        String password = user.get("password");
        Map<String, Object> resMap = new HashMap<>();

        Optional<UserModel> userData = authRepository.findByUsername(username);
        if (userData.isEmpty() || !passwordEncoder.matches(password, userData.get().getPassword())) {
            resMap.put("message", "Błędny email lub hasło");
            resMap.put("code", "400");
            resMap.put("status", "error");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resMap);
        }

        String token = Jwts
                .builder()
                .setSubject(userData.get().getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 86400000))
                .signWith(SignatureAlgorithm.HS256, JwtUtil.SECRET_KEY)
                .compact();

        resMap.put("message", "Pomyślnie zalogowano");
        resMap.put("code", "200");
        resMap.put("status", "success");
        resMap.put("token", token);

        return ResponseEntity.ok(resMap);
    }
}
