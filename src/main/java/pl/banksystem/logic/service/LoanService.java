package pl.banksystem.logic.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.banksystem.logic.account.Account;
import pl.banksystem.logic.account.loans.Installment;
import pl.banksystem.logic.account.loans.Loan;;
import pl.banksystem.logic.domain.AppUser;
import pl.banksystem.logic.repository.AppUserRepository;
import pl.banksystem.logic.repository.LoanRepository;

import javax.transaction.Transactional;
import java.util.LinkedList;
import java.util.List;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class LoanService {

    private final LoanRepository loanRepository;
    private final AppUserRepository appUserRepository;

    public void saveLoan(Loan loan) {
        loanRepository.save(loan);
    }

    public List<Loan> getAllUserLoans(String username) {
        List<Loan> loans = new LinkedList<>();
        appUserRepository.findUserByUsername(username).getAccounts()
                .forEach(account -> loans.addAll(account.getCurrentLoans()));

        return loans;
    }

    public Loan getLoan(Long loanId) {
        return loanRepository.findLoanById(loanId);
    }

    public double lol(AppUser user) {

        double points = 0;
        int curLoans = 0;

        for (Account account : user.getAccounts()) {

            curLoans += account.getCurrentLoans().size();

            for (Loan loan : account.getCurrentLoans()) {
                for (Installment installment : loan.getInstallmentList()) {
                    
                }
            }
        }

        return 0;
    }

    public void payLoan(Long loanId, double amount) {
        Loan loan = loanRepository.findLoanById(loanId);
        loan.payLoan(amount);
    }
}
