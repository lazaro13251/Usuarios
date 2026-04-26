package com.example.demo.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

import com.example.demo.model.User;

class UserUtilsTest {

  @Test
  void testCalculateRFC() {
    User user = new User();
    user.setName("Juan");
    user.setLastName1("Perez");
    user.setLastName2("Gomez");
    user.setBirthDate(LocalDate.of(1990, 5, 15));

    // PE (Perez) + G (Gomez) + J (Juan) + 900515 (1990-05-15)
    String expectedRfcBase = "PEGJ900515";

    String result = UserUtils.calculateRFC(user);

    assertEquals(expectedRfcBase, result);
  }

  @Test
  void testEncryptAndDecrypt() throws Exception {
    String originalText = "SecretPassword123!";

    String encryptedText = UserUtils.encrypt(originalText);

    assertNotNull(encryptedText);
    assertNotEquals(originalText, encryptedText);

    String decryptedText = UserUtils.decrypt(encryptedText);

    assertEquals(originalText, decryptedText);
  }
}
