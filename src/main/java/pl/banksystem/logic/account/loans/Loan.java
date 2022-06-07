package pl.banksystem.logic.account.loans;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "account"})
public class Loan {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private double interestPercentage;
    private double commissionPercentage;
    private int numberOfMonths;
    private double loanAmount;

    private final Date loanDate = new Date();
    private LoanStatus loanStatus = LoanStatus.ACTIVE;
    @OneToMany(cascade = {CascadeType.ALL})
    private final List<Installment> installmentList = new ArrayList<>();

    /**
     * Construct a loan with specified annual interest rate,
     * number of years, and loan amount
     */
    public Loan(double interestPercentage, double commissionPercentage, int numberOfMonths,
                double loanAmount) {
        this.interestPercentage = interestPercentage;
        this.commissionPercentage = commissionPercentage;
        this.numberOfMonths = numberOfMonths;
        this.loanAmount = loanAmount;

        double installmentCost = getMonthlyPayment();
        for (int i = 0; i < numberOfMonths; i++) {
            installmentList.add(new Installment(installmentCost));
        }
    }

    public LoanStatus payLoan(double transferAmount) {
        double transferAmountLeft = transferAmount;
        for (Installment installment : installmentList) {
            if (installment.getPaidDate() == null) {
                if (installment.getAmountLeft() - transferAmountLeft <= 0) {
                    transferAmountLeft -= installment.getAmountLeft();
                    installment.setAmountLeft(0);
                    installment.setPaidDate(new Date());
                    if (installmentList.lastIndexOf(installment) == installmentList.size() - 1) {
                        loanStatus = LoanStatus.ENDED;
                        return LoanStatus.ENDED;
                    }
                } else {
                    installment.setAmountLeft(installment.getAmountLeft() - transferAmountLeft);
                    transferAmountLeft = 0;
                }
                if (transferAmountLeft == 0) {
                    return LoanStatus.ACTIVE;
                }
            }
        }
        return LoanStatus.ACTIVE;
    }

    /**
     * Find monthly payment
     */
    public double getMonthlyPayment() {
        return (loanAmount / (double)numberOfMonths) * (1.0 + interestPercentage) + (loanAmount * commissionPercentage / (double)numberOfMonths);
    }

    /**
     * Find total payment
     */
    public double getTotalPayment() {
        return getMonthlyPayment() * numberOfMonths;
    }
}