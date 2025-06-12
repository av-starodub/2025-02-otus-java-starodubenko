package ru.otus.app.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Entity
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Table(name = "clients")
public class Client extends AbstractBaseEntity {

    @NotBlank
    @Column(name = "name", nullable = false)
    private String name;

    public Client(Long id, String name) {
        super(id);
        this.name = name;
    }

    @Override
    public String toString() {
        return "Client{id=%s, name=%s}".formatted(super.getId(), name);
    }
}
