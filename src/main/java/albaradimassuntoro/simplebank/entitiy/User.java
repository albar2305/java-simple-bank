package albaradimassuntoro.simplebank.entitiy;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User {

  @Id
  private String username;

  @Column(name = "hashed_password")
  @JsonIgnore
  private String hashedPassword;

  @Column(name = "full_name")
  private String fullName;

  private String email;

  @Column(name = "password_changed_at")
  private Timestamp passwordChangedAt;

  @Column(name = "created_at")
  private Timestamp createdAt;

  @OneToMany(mappedBy = "owner")
  private List<Account> accounts;

  @ManyToMany(fetch = FetchType.EAGER)
  private Set<UserRole> roles = new HashSet<>();
}
