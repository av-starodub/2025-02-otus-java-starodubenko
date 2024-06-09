package ru.otus.advjdbc.model;

import lombok.Getter;
import lombok.Setter;
import ru.otus.advjdbc.RepositoryField;
import ru.otus.advjdbc.RepositoryIdField;

@Setter
@Getter
public abstract class AbstractBaseEntity {
    @RepositoryIdField
    @RepositoryField
    protected Long id;
}
