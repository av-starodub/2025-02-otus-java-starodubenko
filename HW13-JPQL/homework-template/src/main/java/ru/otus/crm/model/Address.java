package ru.otus.crm.model;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.otus.crm.model.base.AbstractBaseEntity;

import static java.util.Objects.requireNonNull;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "address")
public class Address extends AbstractBaseEntity {
    @NotBlank
    @Size(max = 120)
    @Column(name = "street", nullable = false)
    @NotNull
    private String street;

    public Address(String street) {
        this(null, street);
    }

    public Address(Long id, String street) {
        requireNonNull(street, "Parameter street must not be null");
        this.id = id;
        this.street = street;
    }

    @Override
    public String toString() {
        return "Address{id=%d, street=%s}".formatted(id, street);
    }
}
