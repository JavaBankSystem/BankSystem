import pl.banksystem.logic.account.transaction.Transaction
import pl.banksystem.logic.account.transaction.TransactionType
import pl.banksystem.logic.account.transaction.tracking.TransactionStatusType
import spock.lang.Specification

import java.util.stream.Collectors


class TransactionTest extends Specification {

    def 'transaction constructor test'() {
        given:
        Transaction t

        when:
        t = TransactionStatusType.PENDING.addTransaction(accountID, amount, transactionType)

        then:
        with(t){
            getTransactionID() == 0
            getActualTransactionStatus().transactionStatusType == TransactionStatusType.PENDING
            getTransactionHistory().size() == 1
            getAccountID() == accountID
            getAmount() == amount
            getTransactionType() == transactionType
        }


        where:
        accountID | amount | transactionType
        1         | 10     | TransactionType.Deposit
        2         | 20     | TransactionType.Withdraw
        3         | 30     | TransactionType.Withdraw
        4         | 40     | TransactionType.Deposit
        5         | 50     | TransactionType.Deposit
    }

    def 'change transaction status to: #newTransactionStatusType'(TransactionStatusType newTransactionStatusType) {

        given:
        Transaction t = TransactionStatusType.PENDING.addTransaction(1, 20, TransactionType.Deposit)

        when:
        t = newTransactionStatusType.proccessTransaction(t)

        then:
        with(t){
            actualTransactionStatus.transactionStatusType == newTransactionStatusType
            getTransactionHistory()
                    .stream()
                    .map(x -> x.getTransactionStatusType()
                            .toString()).collect(Collectors.toList()) == expectedHistory
        }


        where:
        newTransactionStatusType      || expectedHistory
        TransactionStatusType.DONE || List.of("PENDING", "DONE")
        TransactionStatusType.CANCELED || List.of("PENDING", "CANCELED")
    }

    def 'incorrect status proccess'(TransactionStatusType newTransactionStatusType){

        given:
        Transaction t = TransactionStatusType.PENDING.addTransaction(1, 20, TransactionType.Deposit)
        t = startingTransactionStatusType.proccessTransaction(t)

        when:
        t = newTransactionStatusType.proccessTransaction(t)

        then:
        t == null

        where:
        startingTransactionStatusType |newTransactionStatusType
        TransactionStatusType.CANCELED |TransactionStatusType.DONE
        TransactionStatusType.CANCELED |TransactionStatusType.CANCELED
        TransactionStatusType.DONE | TransactionStatusType.CANCELED
        TransactionStatusType.DONE | TransactionStatusType.DONE

    }


}
