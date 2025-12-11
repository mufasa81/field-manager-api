package net.service.fieldmanager.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import net.service.fieldmanager.user.Role;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtTokenProvider {

    // IMPORTANT: This is a hardcoded secret key for demonstration purposes only.
    // In a production environment, this key MUST be externalized and kept secret.
    // For example, load it from application.properties or environment variables.
    private final SecretKey secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS512);

    private final long validityInMilliseconds = 3600000; // 1 hour

    public String createToken(String username, String userId, String name, Role role) {
        Claims claims = Jwts.claims().setSubject(username);
        claims.put("name", name);
        claims.put("userId", userId);
        claims.put("role", role.name());

        Date now = new Date();
        Date validity = new Date(now.getTime() + validityInMilliseconds);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(secretKey)
                .compact();
    }
}
