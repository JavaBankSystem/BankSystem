package pl.banksystem.logic.account.transaction;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.banksystem.logic.account.transaction.tracking.TransactionStatusType;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class TransactionStatus implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private TransactionStatusType transactionStatusType;
    private Date changeDate;

    public TransactionStatus(TransactionStatusType transactionStatusType, Date changeDate) {
        this.transactionStatusType = transactionStatusType;
        this.changeDate = changeDate;
    }
}
