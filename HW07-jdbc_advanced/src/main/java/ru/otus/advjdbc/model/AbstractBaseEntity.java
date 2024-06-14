package ru.otus.advjdbc.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import ru.otus.advjdbc.RepositoryField;
import ru.otus.advjdbc.RepositoryIdField;

@Setter
@Getter
@AllArgsConstructor
public abstract class AbstractBaseEntity {
    @RepositoryIdField
    @RepositoryField
    protected Long id;
}
