package vn.uit.sangSoftwareDesgin.softwareDesginProject.Util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import vn.uit.sangSoftwareDesgin.softwareDesginProject.Entity.Enums.TokenType;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import static vn.uit.sangSoftwareDesgin.softwareDesginProject.Constants.JwtConstants.*;

@Slf4j
@Component
public class JwtTokenUtil {


    // Generate a token (access or refresh) based on the token type
    public String generateToken(String userName, TokenType tokenType) {
        Map<String, Object> claims = new HashMap<>();
        long validity = tokenType == TokenType.ACCESS ? ACCESS_TOKEN_VALIDITY_SECONDS : REFRESH_TOKEN_VALIDITY_SECONDS;
        return createToken(claims, userName, validity);
    }

    public Boolean validateToken(String token, UserDetails userDetails, TokenType tokenType) {
        try {
            final String username = extractUsername(token);
            boolean isCorrectType = validateTokenType(token, tokenType);

            if (userDetails != null) {
                return (username.equals(userDetails.getUsername()) && !isTokenExpired(token) && isCorrectType);
            }

            return (!isTokenExpired(token) && isCorrectType);

        } catch (MalformedJwtException e) {
            log.error("Malformed JWT token: {}", token, e);
            return false;
        } catch (UnsupportedJwtException e) {
            log.error("Unsupported JWT token: {}", token, e);
            return false;
        } catch (IllegalArgumentException e) {
            log.error("Empty or null JWT token: {}", token, e);
            return false;
        }
    }

    // Validate token type by inspecting claims (optional enhancement)
    private boolean validateTokenType(String token, TokenType tokenType) {
        Claims claims = extractAllClaims(token);
        String tokenTypeClaim = claims.get("tokenType", String.class); // Ensure tokenType is set during creation
        return tokenType.name().equals(tokenTypeClaim);
    }

    // Create a JWT token with specified claims, subject (username), and validity duration
    private String createToken(Map<String, Object> claims, String userName, long validitySeconds) {
        log.warn("setExpiration for token: {}", validitySeconds);
        claims.put("tokenType", validitySeconds == ACCESS_TOKEN_VALIDITY_SECONDS ? TokenType.ACCESS.name() : TokenType.REFRESH.name());
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userName)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + validitySeconds))
                .signWith(getSignKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    // Extract the username from the token
    public String extractUsername(String token) {
        if (token == null || token.isEmpty()) {
            throw new IllegalArgumentException("Token is null or empty");
        }
        return extractClaim(token, Claims::getSubject);
    }

    // Extract the expiration date from the token
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    // Extract a claim from the token
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    // Extract all claims from the token
    private Claims extractAllClaims(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(getSignKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (JwtException e) {
            log.error("Error parsing JWT: {}", token, e);
            throw e;
        }
    }

    // Check if the token is expired
    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    // Get the signing key using the constant secret
    private Key getSignKey() {
        byte[] keyBytes = Decoders.BASE64URL.decode(SIGNING_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
