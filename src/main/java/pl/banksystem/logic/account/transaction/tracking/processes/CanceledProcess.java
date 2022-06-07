package pl.banksystem.logic.account.transaction.tracking.processes;


import lombok.extern.slf4j.Slf4j;

import pl.banksystem.logic.account.transaction.Transaction;
import pl.banksystem.logic.account.transaction.TransactionStatus;


import java.util.Date;

import static pl.banksystem.logic.account.transaction.tracking.TransactionStatusType.CANCELED;

@Slf4j
public class CanceledProcess implements StatusProcess {

    @Override
    public Transaction changeStatus(Transaction transaction) {
        log.info("Setting process status to CANCELED");
        transaction.getTransactionHistory().add(new TransactionStatus(CANCELED, new Date()));
        //TODO send message to client
        //Message.send("Twoj przelew przyjęty do realizacji" + dane...);
        return transaction;
    }
}
