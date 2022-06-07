package pl.banksystem.logic.account;

import io.restassured.RestAssured;
import org.junit.jupiter.api.Test;
import pl.banksystem.logic.account.loans.Installment;
import pl.banksystem.logic.account.loans.Loan;
import pl.banksystem.logic.account.loans.LoanStatus;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class LoanTest {

    @Test
    public void payInstallment() {
        // given
        Loan loan = new Loan(0.08, 0.05, 3, 1200);
        double monthlyPayment = loan.getMonthlyPayment();

        //when
        LoanStatus loanStatus = loan.payLoan(monthlyPayment);
        List<Installment> installmentList = loan.getInstallmentList();

        //then
        assertEquals(installmentList.get(0).getAmountLeft(), 0);
        installmentList.remove(0);
        for (Installment installment : installmentList) {
            assertEquals(installment.getAmountLeft(), monthlyPayment);
        }
        assertEquals(loanStatus, LoanStatus.ACTIVE);
    }

    @Test
    public void payInstallmentOneAndHalf() {
        // given
        Loan loan = new Loan(0.08, 0.05, 3, 1200);
        double monthlyPayment = loan.getMonthlyPayment();

        //when
        LoanStatus loanStatus = loan.payLoan(monthlyPayment * 1.5);
        List<Installment> installmentList = loan.getInstallmentList();

        //then
        assertEquals(installmentList.get(0).getAmountLeft(), 0);
        assertEquals(installmentList.get(1).getAmountLeft(), monthlyPayment / 2);
        assertEquals(installmentList.get(2).getAmountLeft(), monthlyPayment);

        assertEquals(loanStatus, LoanStatus.ACTIVE);
    }

    @Test
    public void payWholeLoan() {
        // given
        Loan loan = new Loan(0.08, 0.05, 3, 1200);
        double monthlyPayment = loan.getMonthlyPayment();

        //when
        LoanStatus loanStatus = loan.payLoan(monthlyPayment * 3);
        List<Installment> installmentList = loan.getInstallmentList();

        //then
        for (Installment installment : installmentList) {
            assertEquals(installment.getAmountLeft(), 0);
        }
        assertEquals(loanStatus, LoanStatus.ENDED);
    }
}
