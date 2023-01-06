package org.zerock.ciub.security.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.log4j.Log4j2;

import java.security.Key;
import java.time.ZonedDateTime;
import java.util.Date;

@Log4j2
public class JWTUtil {

    private Key secretKey;
    //1 month
    private long expire = 60*24*30;

    public JWTUtil() {
        byte[] keyBytes = Decoders.BASE64.decode("zerock12345678zerock12345678zerock12345678zerock12345678");
        this.secretKey = Keys.hmacShaKeyFor(keyBytes);
    }

    public  String generateToken(String content) throws Exception{

        return Jwts.builder()
                .setIssuedAt(new Date())
                .setExpiration(Date.from(ZonedDateTime.now().plusMinutes(expire).toInstant()))
                .claim("sub", content)
                .signWith(secretKey,SignatureAlgorithm.HS256)
                .compact();
    }

    public String validateAndExtract(String tokenStr) throws Exception{
        String contentValue = null;

        try {
            Jws<Claims> claimsJws = Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(tokenStr);
            log.info(claimsJws);
            log.info(claimsJws.getBody().getClass());
            Claims claims = claimsJws.getBody();
            log.info("--------------------------");
            contentValue = claims.getSubject();
        }catch (Exception e){
            e.printStackTrace();
            log.error(e.getMessage());
            contentValue = null;
        }
        return contentValue;
    }

}
