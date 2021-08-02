package me.hjhng125.taxiallocationapi.token;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;
import java.util.function.Function;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
public class JwtTokenProvider {

    @Value("${secret.key}")
    private String secretKey;

    static final long tokenValidTime = 60 * 60 * 1000L;

    public String createToken(String username) {
        return Jwts.builder()
            .setClaims(Jwts.claims())
            .setSubject(username)
            .setIssuedAt(new Date(System.currentTimeMillis()))
            .setExpiration(new Date(System.currentTimeMillis() + tokenValidTime))
            .signWith(SignatureAlgorithm.HS512, secretKey)
            .compact();
    }

}
