package pl.banksystem.logic.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.banksystem.logic.account.loans.Loan;


@Repository
public interface LoanRepository extends JpaRepository<Loan, Long> {
    Loan findLoanById(Long id);
}
