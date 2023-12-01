package ru.otus.crm.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.otus.crm.model.base.AbstractBaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Objects;

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
        Objects.requireNonNull(street, "street must not be null");
        this.id = id;
        this.street = street;
    }

    @Override
    public String toString() {
        return "Address{" +
                "id=" + id +
                ", street='" + street + '\'' +
                '}';
    }
}
