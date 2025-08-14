package ru.otus.orm.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import ru.otus.orm.entity.annotations.RepositoryField;
import ru.otus.orm.entity.annotations.RepositoryIdField;

@Setter
@Getter
@AllArgsConstructor
public abstract class AbstractBaseEntity {
    @RepositoryIdField
    @RepositoryField
    protected Long id;
}
