import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.ObjectWriter
import pl.banksystem.logic.account.Account
import pl.banksystem.logic.account.transaction.Transaction
import pl.banksystem.logic.account.transaction.TransactionType
import pl.banksystem.logic.account.transaction.tracking.TransactionStatusType
import spock.lang.Specification

class AccountTest extends Specification {

    def 'account class test'(Long clientID, double balance) {

        given:
        Account a

        when:
        a = new Account(clientID, balance)
        then:
        with(a) {
            getBalance() == balance
            getClientID() == clientID
        }

        where:
        clientID | balance
        1        | 100
        2        | 200
        3        | 50
        4        | 10
        5        | 25
    }

    def 'should #transaction.getTransactionType() #transaction.getAmount() money'(Transaction transaction) {

        given:
        Account a = new Account(1, 200)

        when:
        a.transaction(transaction)

        then:
        a.getBalance() == expected

        where:
        transaction                                                    || expected
        returnTransactionWithStatusDone(TransactionType.Deposit, 20)   || 220
        returnTransactionWithStatusDone(TransactionType.Withdraw, 20)  || 180
        returnTransactionWithStatusDone(TransactionType.Deposit, 2000) || 2200
        returnTransactionWithStatusDone(TransactionType.Withdraw, 180) || 20
        returnTransactionWithStatusDone(TransactionType.Withdraw, 200) || 0
    }

    static Transaction returnTransactionWithStatusDone(TransactionType transactionType, double amount) {
        return TransactionStatusType.DONE.proccessTransaction(TransactionStatusType.PENDING.addTransaction(0, amount, transactionType))
    }
}

