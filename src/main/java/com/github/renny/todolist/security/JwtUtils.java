package com.github.renny.todolist.security;

import com.github.renny.todolist.config.JwtProperties;
import com.github.renny.todolist.modules.user.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtUtils {
    private final JwtProperties jwtProperties;
    private final SecretKey jjwtSecretKey;

    public JwtUtils(JwtProperties jwtProperties){
        this.jwtProperties = jwtProperties;
        byte[] keyBytes = Decoders.BASE64.decode(jwtProperties.getSecret());
        this.jjwtSecretKey = Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateAccessToken(User user){
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtProperties.getExpiration());

        return Jwts.builder()
                .subject(user.getId().toString())
                .claim("userName",user.getUserName())
                .issuer(jwtProperties.getIssuer())
                .audience().add(jwtProperties.getAudience()).and()
                .issuedAt(now)
                .expiration(expiryDate)
                .signWith(jjwtSecretKey)
                .compact();
    }

    public String generateRefreshToken(User user){
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtProperties.getRefreshExpiration());

        return Jwts.builder()
                .subject(user.getId().toString())
                .issuedAt(now)
                .expiration(expiryDate)
                .signWith(jjwtSecretKey)
                .compact();
    }

    public Claims validateAndParseToken(String token){
        try {
            return Jwts.parser()
                    .verifyWith(jjwtSecretKey)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        }catch (SignatureException e){
            throw new JwtException("無效的 Token");
        }catch (ExpiredJwtException e){
            throw new JwtException("過期的 Token");
        }catch (MalformedJwtException e){
            throw new JwtException("錯誤的 Token");
        }catch (Exception e){
            throw new JwtException("Token 驗證失敗");
        }

    }

    public String getUserIdFromClaims(Claims claims){
        return claims.getSubject();
    }

//    public long get
}
