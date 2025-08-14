package ru.otus.crm.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.otus.crm.model.base.AbstractBaseEntity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import static java.util.Objects.requireNonNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "phones")
public class Phone extends AbstractBaseEntity {
    @NotBlank
    @Size(max = 20)
    @Column(name = "number", nullable = false)
    private String number;


    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id")
    private Client client;

    public Phone(String number) {
        this(null, number);
    }

    public Phone(Long id, String number) {
        super(id);
        requireNonNull(number, "Parameter number must not be null");
        this.number = number;
    }

    public Phone(Long id, String number, Client client) {
        super(id);
        requireNonNull(number, "Parameter number must not be null");
        requireNonNull(client, "Parameter client must not be null");
        this.number = number;
        this.client = client;
    }

    @Override
    public String toString() {
        return "Phone{id=%d, number=%s, client_id=%s}".formatted(super.getId(), number, client.getId());
    }
}
