package ru.otus.app.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

@Getter
@Setter
@MappedSuperclass
public abstract class AbstractBaseEntity {

    @Id
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;
}
