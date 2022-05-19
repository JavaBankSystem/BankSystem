package pl.banksystem.logic.account.transaction.tracking.processes;


import pl.banksystem.logic.account.transaction.Transaction;


public interface StatusProcess {


    Transaction changeStatus(Transaction transaction);
}
