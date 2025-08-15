package com.cdac.eventnexus.security;

import java.security.Key;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

    //this must be STANDARD Base64 (A–Z a–z 0–9 + / with = padding)
    @Value("${app.jwt.secret}")
    private String secret;

    @Value("${app.jwt.expMinutes:120}")
    private long expMinutes;

    private Key key() {
        // If your secret was generated with Base64 (PowerShell/Java), keep BASE64 here.
        // If you used URL-safe Base64 with '-'/'_', switch to Decoders.BASE64URL.decode(secret)
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
    }
    
    public String generateToken(UserDetails user) {
        Date now = new Date();
        Date exp = new Date(now.getTime() + 60 * 60 * 1000); // 1 hour

        return Jwts.builder()
            .setSubject(user.getUsername())     // should be the EMAIL if  authenticate by email
            .setIssuedAt(now)
            .setExpiration(exp)                 // <-- required by your UI
            .signWith(key(), SignatureAlgorithm.HS256)
            .compact();
    }


    /** Called by your JwtAuthFilter */
    public String extractUsername(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    /** Called by your JwtAuthFilter */
    public boolean validate(String token, UserDetails userDetails) {
        var claims = Jwts.parserBuilder()
                .setSigningKey(key())
                .build()
                .parseClaimsJws(token)
                .getBody();

        boolean notExpired = claims.getExpiration().after(new Date());
        boolean sameUser = userDetails.getUsername().equals(claims.getSubject());
        return notExpired && sameUser;
    }
}
