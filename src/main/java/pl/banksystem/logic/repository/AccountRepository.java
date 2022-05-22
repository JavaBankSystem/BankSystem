package pl.banksystem.logic.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.banksystem.logic.account.Account;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    Optional<Account> findAccountByAccountID(Long accountID);

    Optional<Account> findAccountByClientID(Long clientID);

}
