package com.api.utils;

import com.api.entities.User;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.util.Date;

public class JWTUtil {

    public static String generateToken(User user){
        return JWT.create()
                .withSubject("auth")
                .withClaim("id", user.getId())
                .withClaim("email", user.getEmail())
                .withIssuedAt(new Date())
                .sign(Algorithm.HMAC256("secret"));
    }

    public static String validateToken(String token){
        JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256("secret"))
                .withSubject("auth")
                .build();
        DecodedJWT decodedJWT = jwtVerifier.verify(token);
        return decodedJWT.getClaim("id").toString();
    }

}
