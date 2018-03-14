package ru.sbt.jschool.session3.problem1;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 */
public class AccountServiceImpl implements AccountService {
    private FraudMonitoring fraudMonitoring;

    private Set<Long> operations = new HashSet<>();

    private Set<Long> existingAccounts = new HashSet<>();

    private Map<Long, Map<Long, Account>> accounts = new HashMap<>();

    public AccountServiceImpl(FraudMonitoring fraudMonitoring) {
        this.fraudMonitoring = fraudMonitoring;
    }

    @Override public Result create(long clientID, long accountID, float initialBalance, Currency currency) {
        if (fraudMonitoring.check(clientID))
            return Result.FRAUD;

        if (existingAccounts.contains(accountID))
            return Result.ALREADY_EXISTS;

        if (!accounts.containsKey(clientID))
            accounts.put(clientID, new HashMap<>());

        Map<Long, Account> clientAccounts = accounts.get(clientID);

        clientAccounts.put(accountID, new Account(clientID, accountID, currency, initialBalance));

        existingAccounts.add(accountID);

        return Result.OK;
    }

    @Override public Collection<Account> findForClient(long clientID) {
        return accounts.get(clientID).values();
    }

    @Override public Account find(long accountID) {
        for (Map<Long, Account> clientAccount : accounts.values()) {
            for (Account account : clientAccount.values()) {
                if (account.getAccountID() == accountID)
                    return account;
            }
        }

        return null;
    }

    @Override public Result doPayment(Payment payment) {
        if (operations.contains(payment.getOperationID()))
            return Result.ALREADY_EXISTS;

        if (fraudMonitoring.check(payment.getPayerID()))
            return Result.FRAUD;

        if (fraudMonitoring.check(payment.getRecipientID()))
            return Result.FRAUD;

        if (!accounts.containsKey(payment.getPayerID()))
            return Result.PAYER_NOT_FOUND;

        if (!accounts.containsKey(payment.getRecipientID()))
            return Result.RECIPIENT_NOT_FOUND;

        if (!existingAccounts.contains(payment.getPayerAccountID()))
            return Result.PAYER_NOT_FOUND;

        if (!existingAccounts.contains(payment.getRecipientAccountID()))
            return Result.RECIPIENT_NOT_FOUND;

        Account payer = find(payment.getPayerAccountID());

        Account recipient = find(payment.getRecipientAccountID());

        if (payer.getBalance() < payment.getAmount())
            return Result.INSUFFICIENT_FUNDS;

        float amount = payment.getAmount();

        if (payer.getCurrency() != recipient.getCurrency())
            amount = payer.getCurrency().to(amount, recipient.getCurrency());

        payer.setBalance(payer.getBalance() - payment.getAmount());

        recipient.setBalance(recipient.getBalance() + amount);

        return Result.OK;
    }
}
