package albaradimassuntoro.simplebank.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserResponse {
  private String username;

  private String fullName;

  private String email;

  private Timestamp passwordChangedAt;

  private Timestamp createdAt;
}
