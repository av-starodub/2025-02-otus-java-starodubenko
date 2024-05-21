package ru.otus.advjdbc.model;

import ru.otus.advjdbc.RepositoryField;
import ru.otus.advjdbc.RepositoryIdField;
import ru.otus.advjdbc.RepositoryTable;

@RepositoryTable(title = "accounts")
public class Account {
    @RepositoryIdField
    @RepositoryField
    private Long id;

    @RepositoryField
    private Long amount;

    @RepositoryField // TODO (name = "tp");
    private String accountType;

    @RepositoryField
    private String status;

    public Account(Long amount, String accountType, String status) {
        this.amount = amount;
        this.accountType = accountType;
        this.status = status;
    }

    @Override
    public String toString() {
        return "Account{" +
                "id=" + id +
                ", amount=" + amount +
                ", accountType='" + accountType + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
