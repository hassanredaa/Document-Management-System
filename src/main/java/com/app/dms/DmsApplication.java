package com.app.dms;

import io.jsonwebtoken.security.Keys;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.crypto.SecretKey;
import java.util.Base64;

@SpringBootApplication
public class DmsApplication {

    public static void main(String[] args) {

        SpringApplication.run(DmsApplication.class, args);
    }

}
