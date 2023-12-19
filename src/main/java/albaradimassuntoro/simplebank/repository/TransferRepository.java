package albaradimassuntoro.simplebank.repository;

import albaradimassuntoro.simplebank.entitiy.Transfer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransferRepository extends JpaRepository<Transfer,String> {
}
