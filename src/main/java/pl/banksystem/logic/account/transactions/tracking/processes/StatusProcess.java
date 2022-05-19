package pl.banksystem.logic.account.transactions.tracking.processes;


import pl.banksystem.logic.account.transactions.Transaction;
import pl.banksystem.logic.account.transactions.tracking.TransactionStatusType;


public interface StatusProcess {


    Transaction changeStatus(Transaction transaction);
}
