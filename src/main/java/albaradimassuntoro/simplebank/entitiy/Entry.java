package albaradimassuntoro.simplebank.entitiy;


import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "entries")
public class Entry {

  @Id
  private String id;

  @ManyToOne
  @JoinColumn(name = "account_id",referencedColumnName = "id")
  private Account account;

  private Long amount;

  @Column(name = "created_at")
  private Timestamp createdAt;
}
