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

  @JoinColumn(name = "from_account_id",referencedColumnName = "id")
  @ManyToOne
  private Account fromAccount;

  @JoinColumn(name = "to_account_id",referencedColumnName = "id")
  @ManyToOne
  private Account toAccount;

  private Long amount;

  @Column(name = "created_at")
  private Timestamp createdAt;
}
