package ru.sbt.jschool.session3.problem1;

import java.util.*;
import java.util.stream.Collectors;

/**
 */
public class AccountServiceImpl implements AccountService {
    protected FraudMonitoring fraudMonitoring;
    private Map<Long, Account> accounts = new HashMap<>();
    private Set<Long> operationIds = new HashSet<>();

    public AccountServiceImpl(FraudMonitoring fraudMonitoring) {
        this.fraudMonitoring = fraudMonitoring;
    }

    @Override
    public Result create(long clientID, long accountID, float initialBalance, Currency currency) {
        if (accounts.containsKey(accountID)) {
            return Result.ALREADY_EXISTS;
        } else {
            if (fraudMonitoring.check(clientID)) {
                return Result.FRAUD;
            } else {
                accounts.put(accountID, new Account(clientID, accountID, currency, initialBalance));
                return Result.OK;
            }
        }

    }

    @Override
    public List<Account> findForClient(long clientID) {
        return accounts.values()
                .stream()
                .filter(acc -> acc.getClientID() == clientID)
                .collect(Collectors.toList());
    }

    @Override
    public Account find(long accountID) {
        return accounts.get(accountID);
    }

    @Override
    public Result doPayment(Payment payment) {
        if (operationIds.contains(payment.getOperationID())) {
            return Result.ALREADY_EXISTS;
        }
        else {
            operationIds.add(payment.getOperationID());
        }
        if (findForClient(payment.getPayerID()).size() == 0 || find(payment.getPayerAccountID()) == null) {
            return Result.PAYER_NOT_FOUND;
        }
        if (findForClient(payment.getRecipientID()).size() == 0 || find(payment.getRecipientAccountID()) == null) {
            return Result.RECIPIENT_NOT_FOUND;
        }
        if (fraudMonitoring.check(payment.getPayerID()) || fraudMonitoring.check(payment.getPayerAccountID())) {
            return Result.FRAUD;
        }
        return checkCurrencyAndDoPayment(payment);

    }

    private Result checkCurrencyAndDoPayment(Payment payment){
        Account payer = find(payment.getPayerAccountID());
        Account recipient = find(payment.getRecipientAccountID());
        if (payer.getCurrency() == recipient.getCurrency()) {
            makeAPayment(payer, recipient, payment, 1);
        } else {
            switch (payer.getCurrency()) {
                case RUR: {
                    switch (recipient.getCurrency()) {
                        case USD: makeAPayment(payer, recipient, payment, 1f / Currency.RUR_TO_USD); break;
                        case EUR: makeAPayment(payer, recipient, payment, 1f / Currency.RUR_TO_EUR); break;
                    }
                    break;
                }
                case EUR: {
                    switch (recipient.getCurrency()) {
                        case RUR: makeAPayment(payer, recipient, payment, Currency.RUR_TO_EUR); break;
                        case USD: return Result.INSUFFICIENT_FUNDS;
                    }
                    break;
                }
                case USD: {
                    switch (recipient.getCurrency()) {
                        case RUR: makeAPayment(payer, recipient, payment, Currency.RUR_TO_USD); break;
                        case EUR: return Result.INSUFFICIENT_FUNDS;
                    }
                    break;
                }
            }
        }
        return Result.OK;
    }

    private void makeAPayment(Account payer, Account recipient, Payment payment, float convert){
        payer.setBalance(payer.getBalance() - payment.getAmount());
        recipient.setBalance(recipient.getBalance() + payment.getAmount() * convert);

    }
}
