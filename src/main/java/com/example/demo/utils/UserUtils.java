package com.example.demo.utils;

import java.time.format.DateTimeFormatter;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import com.example.demo.model.User;

public class UserUtils {

  /**
   * Metodo calculate RFC User
   * 
   * @param userInfo
   * @return
   */
  public static String calculateRFC(User user) {
    String name = user.getName().trim().toUpperCase();
    String lastName1 = user.getLastName1().trim().toUpperCase();
    String lastName2 = user.getLastName2().trim().toUpperCase();
    String formattedDate =
        user.getBirthDate().format(DateTimeFormatter.ofPattern(ConstUser.RFC_DATE_FORMAT));
    String rfcBase = lastName1.substring(0, 2) + lastName2.substring(0, 1) + name.substring(0, 1)
        + formattedDate;
    return rfcBase;
  }

  /**
   * Metod encrypt AES256
   * 
   * @param plainText
   * @return
   * @throws Exception
   */
  public static String encrypt(String plainText) throws Exception {
    SecretKeySpec secretKey =
        new SecretKeySpec(ConstUser.SECRET_KEY.getBytes(), ConstUser.ALGORITHM);
    Cipher cipher = Cipher.getInstance(ConstUser.ALGORITHM);
    cipher.init(Cipher.ENCRYPT_MODE, secretKey);
    byte[] encryptedBytes = cipher.doFinal(plainText.getBytes());
    return Base64.getEncoder().encodeToString(encryptedBytes);
  }

  /**
   * Metod decrypt AES256
   * 
   * @param encryptedText
   * @return
   * @throws Exception
   */
  public static String decrypt(String encryptedText) throws Exception {
    SecretKeySpec secretKey =
        new SecretKeySpec(ConstUser.SECRET_KEY.getBytes(), ConstUser.ALGORITHM);
    Cipher cipher = Cipher.getInstance(ConstUser.ALGORITHM);
    cipher.init(Cipher.DECRYPT_MODE, secretKey);
    byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(encryptedText));
    return new String(decryptedBytes);
  }

}
