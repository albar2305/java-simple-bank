package albaradimassuntoro.simplebank.entitiy;


import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "transfers")
public class Transfer {

  @Id
  private String id;

  @Column(name = "from_account_id")
  private String fromAccount;

  @Column(name = "to_account_id")
  private String toAccount;

  private Long amount;

  @Column(name = "created_at")
  private Timestamp createdAt;
}
