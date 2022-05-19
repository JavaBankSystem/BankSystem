package pl.banksystem.logic.account.transactions.tracking.processes;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.banksystem.logic.account.transactions.Transaction;
import pl.banksystem.logic.account.transactions.TransactionStatus;
import pl.banksystem.logic.account.transactions.tracking.TransactionStatusType;


import java.util.Date;

import static pl.banksystem.logic.account.transactions.tracking.TransactionStatusType.CANCELED;

public class CanceledProcess implements StatusProcess {

    public static final Logger logger = LoggerFactory.getLogger(CanceledProcess.class);

    @Override
    public Transaction changeStatus(Transaction transaction) {
        logger.info("Setting process status to CANCELED");
        transaction.getTransactionHistory().add(new TransactionStatus(CANCELED, new Date()));
        return transaction;
    }
}
