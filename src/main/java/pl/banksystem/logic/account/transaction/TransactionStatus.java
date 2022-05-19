package pl.banksystem.logic.account.transaction;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.banksystem.logic.account.transaction.tracking.TransactionStatusType;

import java.io.Serializable;
import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TransactionStatus implements Serializable {
    private TransactionStatusType transactionStatusType;
    private Date changeDate;
}
