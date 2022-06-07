package pl.banksystem.logic.account.loans;

import lombok.Data;

@Data
public class NewLoanForm {
    private Long accountId;
    private double installmentPercentage;
    private double commissionPercentage;
    private int numberOfMonths;
    private double loanAmount;

    public NewLoanForm(Long accountId, double installmentPercentage, double commissionPercentage, int numberOfMonths, double loanAmount) {
        this.accountId = accountId;
        this.installmentPercentage = installmentPercentage;
        this.commissionPercentage = commissionPercentage;
        this.numberOfMonths = numberOfMonths;
        this.loanAmount = loanAmount;
    }
}
