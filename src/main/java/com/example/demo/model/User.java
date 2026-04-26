package com.example.demo.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Class User
 */
@Document(collection = "usersTest")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class User {

  @Id
  private String id;

  private String email;
  private String name;

  @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
  private String lastName1;

  @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
  private String lastName2;

  @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
  private LocalDate birthDate;

  private String phone;

  @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
  private String password;

  @Indexed(unique = true)
  @Field("tax_id")
  private String taxId;


  @CreatedDate
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm")
  @Field("created_at")
  private LocalDateTime created_at;

  @LastModifiedDate
  @JsonIgnore
  @Field("updated_at")
  private LocalDateTime update_at;

  private List<Address> addresses;
  
  private boolean active;
}
