package vn.uit.sangSoftwareDesgin.softwareDesginProject.Util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import static vn.uit.sangSoftwareDesgin.softwareDesginProject.Constants.JwtConstants.*;

@Component
public class JwtTokenUtil {

    // Generate access token with a given username
    public String generateAccessToken(String userName) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, userName, ACCESS_TOKEN_VALIDITY_SECONDS);
    }

    // Generate refresh token with a given username
    public String generateRefreshToken(String userName) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, userName, REFRESH_TOKEN_VALIDITY_SECONDS);
    }

    // Create a JWT token with specified claims, subject (username), and validity duration
    private String createToken(Map<String, Object> claims, String userName, long validitySeconds) {
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
        return Jwts.parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // Check if the token is expired
    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    // Validate the token against user details and expiration
    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    // Get the signing key using the constant secret
    private Key getSignKey() {
        byte[] keyBytes = Decoders.BASE64URL.decode(SIGNING_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
