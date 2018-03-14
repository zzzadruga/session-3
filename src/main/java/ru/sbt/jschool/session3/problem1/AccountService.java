package ru.sbt.jschool.session3.problem1;

import java.util.List;

/**
 */
public interface AccountService {
    Result create(long clientID, long accountID, float initialBalance, Currency currency);

    List<Account> findForClient(long clientID);

    Account find(long accountID);

    Result doPayment(Payment payment);
}
