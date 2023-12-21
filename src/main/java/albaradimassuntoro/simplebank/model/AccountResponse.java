package albaradimassuntoro.simplebank.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AccountResponse {
  private String id;

  private String owner;

  private Long balance;

  private String currency;

  private Timestamp createdAt;
}
