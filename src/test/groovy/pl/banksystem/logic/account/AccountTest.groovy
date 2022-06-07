package pl.banksystem.logic.account


import pl.banksystem.logic.account.transaction.Transaction
import pl.banksystem.logic.account.transaction.TransactionType
import pl.banksystem.logic.account.transaction.tracking.TransactionStatusType
import pl.banksystem.logic.domain.AppUser
import spock.lang.Specification

class AccountTest extends Specification {

    def 'account class test'(AppUser client, double balance) {

        given:
        Account a

        when:
        a = new Account(client, balance)

        then:
        with(a) {
            getBalance() == balance
        }

        where:
        client                                         | balance
        new AppUser(1, "test", "password", null, null) | 100
        new AppUser(2, "asd", "123123", null, null)    | 200
        new AppUser(3, "kolhr", "534", null, null)     | 50
        new AppUser(4, "zxcasd", "ert", null, null)    | 10
        new AppUser(5, "cvbdf", "dfg", null, null)     | 25
    }

    def 'DEPOSIT + WITHDRAW - should #transaction.getTransactionType() #transaction.getAmount() money'(Transaction transaction) {

        given:
        Account a = new Account(new AppUser("test", "password"), 200)

        when:
        a.transaction(transaction)

        then:
        a.getBalance() == expected

        where:
        transaction                                                            || expected
        returnTransactionTransferWithStatusDone(TransactionType.Deposit, 20)   || 220
        returnTransactionTransferWithStatusDone(TransactionType.Withdraw, 20)  || 180
        returnTransactionTransferWithStatusDone(TransactionType.Deposit, 2000) || 2200
        returnTransactionTransferWithStatusDone(TransactionType.Withdraw, 180) || 20
        returnTransactionTransferWithStatusDone(TransactionType.Withdraw, 200) || 0
    }

    def 'Transfer test'(Transaction transaction) {

        given:
        Account receiver = new Account(1, new AppUser(1, "test", "password", null, null), null, null, 200)
        Account sender = new Account(2, new AppUser(2, "test", "password", null, null), null, null, 250)

        when:
        receiver.transaction(transaction)
        sender.transaction(transaction)

        then:
        receiver.getBalance() == expectedReceiver
        sender.getBalance() == expectedSender

        where:
        transaction                                        || expectedReceiver || expectedSender
        returnTransactionTransferWithStatusDone(1, 2, 20)  || 220              || 230
        returnTransactionTransferWithStatusDone(1, 2, 40)  || 240              || 210
        returnTransactionTransferWithStatusDone(1, 2, 100) || 300              || 150
        returnTransactionTransferWithStatusDone(1, 2, 200) || 400              || 50
        returnTransactionTransferWithStatusDone(1, 2, 1)   || 201              || 249
    }


    static Transaction returnTransactionTransferWithStatusDone(TransactionType transactionType, double amount) {
        return TransactionStatusType.DONE.proccessTransaction(TransactionStatusType.PENDING.addTransaction(0, amount, transactionType))
    }

    static Transaction returnTransactionTransferWithStatusDone(Long receiverAccountID, Long senderAccountID, double amount) {
        return TransactionStatusType.DONE.proccessTransaction(TransactionStatusType.PENDING.addTransaction(receiverAccountID, senderAccountID, amount, TransactionType.Transfer))
    }
}

