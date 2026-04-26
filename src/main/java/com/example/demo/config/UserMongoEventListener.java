package com.example.demo.config;

import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertEvent;
import org.springframework.stereotype.Component;
import com.example.demo.model.User;
import com.example.demo.utils.UserUtils;

/**
 * Create event
 */
@Component
public class UserMongoEventListener extends AbstractMongoEventListener<User> {

  /**
   * Envent create RFC
   */
  @Override
  public void onBeforeConvert(BeforeConvertEvent<User> event) {
    User user = event.getSource();
    if (user.getTaxId() == null || user.getTaxId().isEmpty()) {
      String generatedRfc = UserUtils.calculateRFC(user);
      user.setTaxId(generatedRfc);
    }
  }

}
