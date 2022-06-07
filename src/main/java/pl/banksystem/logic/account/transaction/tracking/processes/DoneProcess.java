package pl.banksystem.logic.account.transaction.tracking.processes;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.banksystem.logic.account.transaction.Transaction;
import pl.banksystem.logic.account.transaction.TransactionStatus;

import java.util.Date;


import static pl.banksystem.logic.account.transaction.tracking.TransactionStatusType.DONE;

@Slf4j
public class DoneProcess implements StatusProcess {
    @Override
    public Transaction changeStatus(Transaction transaction) {
        log.info("Setting process status to DONE");
        transaction.getTransactionHistory().add(new TransactionStatus(DONE, new Date()));
        //TODO send message to client
        //Message.send("Twoj przelew przyjęty do realizacji" + dane...);
        return transaction;
    }
}
