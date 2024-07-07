package ru.otus.crm.model;

/*
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
*/
/*
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
*/

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

import static java.util.Objects.requireNonNull;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "phones")
public class Phone extends AbstractBaseEntity {
    @NotBlank
    @Size(max = 20)
    @Column(name = "number", nullable = false)
    @NotNull
    private String number;

    public Phone(String number) {
        this(null, number);
    }

    public Phone(Long id, String number) {
        requireNonNull(number, "Parameter number must not be null");
        this.id = id;
        this.number = number;
    }

    @Override
    public String toString() {
        return "Phone{id=%d, number=%s}".formatted(id, number);
    }
}
