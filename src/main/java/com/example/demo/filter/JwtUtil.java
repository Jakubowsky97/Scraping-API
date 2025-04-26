package com.example.demo.filter;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;

@Component
public class JwtUtil {

    public static final Key SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256);
}
