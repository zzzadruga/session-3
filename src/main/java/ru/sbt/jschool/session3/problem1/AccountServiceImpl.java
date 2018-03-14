package ru.sbt.jschool.session3.problem1;

import java.util.Collections;
import java.util.List;

/**
 */
public class AccountServiceImpl implements AccountService {
    protected FraudMonitoring fraudMonitoring;

    public AccountServiceImpl(FraudMonitoring fraudMonitoring) {
        this.fraudMonitoring = fraudMonitoring;
    }

    @Override public Result create(long clientID, long accountID, float initialBalance, Currency currency) {
        return Result.OK;
    }

    @Override public List<Account> findForClient(long clientID) {
        return Collections.EMPTY_LIST;
    }

    @Override public Account find(long accountID) {
        return null;
    }

    @Override public Result doPayment(Payment payment) {
        return Result.OK;
    }
}
