package ru.otus.crm.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.otus.crm.model.base.AbstractBaseEntity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.isNull;
import static java.util.Objects.requireNonNull;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "clients")
public class Client extends AbstractBaseEntity implements Cloneable {

    @NotBlank
    @Size(max = 50)
    @Column(name = "name", nullable = false)
    private String name;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "address_id", unique = true)
    private Address address;

    @OneToMany(mappedBy = "client", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Phone> phones;

    public Client(String name) {
        this(null, name);
    }

    public Client(Long id, String name) {
        this(id, name, null, new ArrayList<>());
    }

    public Client(Long id, String name, Address address, List<Phone> phones) {
        super(id);
        requireNonNull(name, "Parameter name must not be null");
        requireNonNull(name, "Parameter phones must not be null");
        this.name = name;
        this.address = address;
        this.phones = phones.stream()
                .map(phone -> new Phone(phone.getId(), phone.getNumber(), this))
                .toList();
    }

    @Override
    public Client clone() {
        var clientClone = new Client(super.getId(), name);
        clientClone.setAddress(cloneAddress());
        clientClone.setPhones(new ArrayList<>(clonePhones(clientClone)));
        return clientClone;
    }

    private Address cloneAddress() {
        if (address != null) {
            return new Address(address.getId(), address.getStreet());
        }
        return null;
    }

    private List<Phone> clonePhones(Client client) {
        if (isNull(phones)) {
            return new ArrayList<>();
        }
        return phones.stream()
                .map(phone -> new Phone(phone.getId(), phone.getNumber(), client))
                .toList();
    }

    @Override
    public String toString() {
        return "Client{id=%d, name='%s', %s, %s}".formatted(super.getId(), name, address, phones);
    }
}

