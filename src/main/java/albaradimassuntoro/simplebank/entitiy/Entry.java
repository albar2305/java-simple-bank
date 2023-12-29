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

  private String accountId;

  private Long amount;

  @Column(name = "created_at")
  private Timestamp createdAt;
}
