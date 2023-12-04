package ru.otus.crm.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.otus.crm.model.base.AbstractBaseEntity;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Phone extends AbstractBaseEntity {
    @NotBlank
    @Size(max = 20)
    @Column(name = "number", nullable = false)
    private String number;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "client_id", updatable = false)
    @NotNull
    private Client client;

    public Phone(String number) {
        this(null, number);
    }

    public Phone(Long id, String number) {
        Objects.requireNonNull(number, "number must not be null");
        this.id = id;
        this.number = number;
    }

    @Override
    public String toString() {
        return "Phone{" +
                "id=" + id +
                ", number='" + number + '\'' +
                '}';
    }
}
