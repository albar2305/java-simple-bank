package albaradimassuntoro.simplebank.repository;

import albaradimassuntoro.simplebank.entitiy.Account;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, String> {
  Page<Account> findAllByOwner(String owner, Pageable pageable);

  Optional<Account> findFirstByIdAndOwner(String id, String owner);
}
