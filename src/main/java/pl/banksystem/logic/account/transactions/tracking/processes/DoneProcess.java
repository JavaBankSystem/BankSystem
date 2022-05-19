package pl.banksystem.logic.account.transactions.tracking.processes;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.banksystem.logic.account.transactions.Transaction;
import pl.banksystem.logic.account.transactions.TransactionStatus;
import pl.banksystem.logic.account.transactions.tracking.TransactionStatusType;

import java.util.Date;


import static pl.banksystem.logic.account.transactions.tracking.TransactionStatusType.DONE;

public class DoneProcess implements StatusProcess {
    public static final Logger logger = LoggerFactory.getLogger(DoneProcess.class);

    @Override
    public Transaction changeStatus(Transaction transaction) {
        logger.info("Setting process status to DONE");
        transaction.getTransactionHistory().add(new TransactionStatus(DONE, new Date()));
        return transaction;
    }
}
