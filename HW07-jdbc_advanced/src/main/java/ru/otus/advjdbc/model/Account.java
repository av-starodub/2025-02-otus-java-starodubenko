package ru.otus.advjdbc.model;

import lombok.Getter;
import lombok.Setter;
import ru.otus.advjdbc.entity.annotations.RepositoryField;
import ru.otus.advjdbc.entity.annotations.RepositoryTable;
import ru.otus.advjdbc.entity.AbstractBaseEntity;

@Getter
@Setter
@RepositoryTable(title = "accounts")
public class Account extends AbstractBaseEntity {

    @RepositoryField
    private Long amount;

    @RepositoryField(columnName = "tp") // TODO (name = "tp");
    private String accountType;

    @RepositoryField
    private String status;

    public Account(Long amount, String accountType, String status) {
        this(null, amount, accountType, status);
    }

    public Account(Long id, Long amount, String accountType, String status) {
        super(id);
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
