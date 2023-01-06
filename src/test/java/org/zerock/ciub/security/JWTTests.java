package org.zerock.ciub.security;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.zerock.ciub.security.util.JWTUtil;

public class JWTTests {

    private JWTUtil jwtUtil;

    @BeforeEach
    public void testBefore(){
        jwtUtil = new JWTUtil();
    }
    @Test
    public void testEncode() throws Exception{
        String email = "user95@zerock.org";
        String token = jwtUtil.generateToken(email);
        System.out.println("token = " + token);
    }

    @Test
    public void testValidate()throws Exception{
        String email = "user95@zerock.org";
        String token = jwtUtil.generateToken(email);
        Thread.sleep(5000);
        String resultEmail = jwtUtil.validateAndExtract(token);
        System.out.println("resultEmail = " + resultEmail);
    }

}
