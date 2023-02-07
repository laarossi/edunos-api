package com.api.utils;

import com.google.common.hash.Hashing;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class SecurityUtil {

    public static String hash(String data){
        return Hashing.sha256()
                .hashString(data, StandardCharsets.UTF_8)
                .toString();
    }

}
