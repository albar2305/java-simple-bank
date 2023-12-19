package albaradimassuntoro.simplebank.repository;

import albaradimassuntoro.simplebank.entitiy.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<Account,String> {

}
