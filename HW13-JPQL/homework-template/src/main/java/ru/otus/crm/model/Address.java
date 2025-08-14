package ru.otus.crm.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.otus.crm.model.base.AbstractBaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import static java.util.Objects.requireNonNull;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "addresses")
public class Address extends AbstractBaseEntity {
    @NotBlank
    @Size(max = 120)
    @Column(name = "street", nullable = false)
    private String street;

    public Address(String street) {
        this(null, street);
    }

    public Address(Long id, String street) {
        super(id);
        requireNonNull(street, "Parameter street must not be null");
        this.street = street;
    }

    @Override
    public String toString() {
        return "Address{id=%d, street=%s}".formatted(super.getId(), street);
    }
}
