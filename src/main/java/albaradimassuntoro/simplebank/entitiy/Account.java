package albaradimassuntoro.simplebank.entitiy;


import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "accounts")
public class Account {
  @Id
  private String id;
//
//  @ManyToOne
//  @JoinColumn(name = "owner",referencedColumnName = "username")
  private String owner;

  private Long balance;

  private String currency;

  @Column(name = "created_at")
  private Timestamp createdAt;

  @OneToMany(mappedBy = "fromAccount")
  private List<Transfer> transferFrom;

  @OneToMany(mappedBy = "toAccount")
  private List<Transfer> transferTo;

  @OneToMany(mappedBy = "account")
  private List<Entry> entries;
}