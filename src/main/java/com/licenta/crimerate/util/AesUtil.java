package com.licenta.crimerate.util;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

public class AesUtil {

    // Cheia secretă trebuie să aibă fix 16 caractere pentru AES-128
    private static final String SECRET_KEY = "LicentaCrima2024";

    public static String encrypt(String parola) {
        try {
            SecretKeySpec secretKey = new SecretKeySpec(SECRET_KEY.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);

            // Criptăm și transformăm în text (Base64) ca să poată fi salvat în baza de date
            return Base64.getEncoder().encodeToString(cipher.doFinal(parola.getBytes("UTF-8")));
        } catch (Exception e) {
            throw new RuntimeException("Eroare la criptarea parolei cu AES!", e);
        }
    }

    public static String decrypt(String parolaCriptata) {
        try {
            SecretKeySpec secretKey = new SecretKeySpec(SECRET_KEY.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, secretKey);

            // Decriptăm din Base64 înapoi în textul original
            return new String(cipher.doFinal(Base64.getDecoder().decode(parolaCriptata)));
        } catch (Exception e) {
            throw new RuntimeException("Eroare la decriptarea parolei cu AES!", e);
        }
    }
}