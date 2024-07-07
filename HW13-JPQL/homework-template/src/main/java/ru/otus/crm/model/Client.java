package ru.otus.crm.model;


import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.otus.crm.model.base.AbstractBaseEntity;

import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.isNull;
import static java.util.Objects.requireNonNull;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "client")
public final class Client extends AbstractBaseEntity implements Cloneable {

    @NotBlank
    @Size(max = 50)
    @Column(name = "name")
    @NotNull
    private String name;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "address_id", unique = true)
    private Address address;

    @OneToMany(mappedBy = "client", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @NotNull
    private List<Phone> phones;

    public Client(String name) {
        this(null, name);
    }

    public Client(Long id, String name) {
        this(id, name, null, new ArrayList<>());
    }

    public Client(Long id, String name, Address address, List<Phone> phones) {
        requireNonNull(name, "Parameter name must not be null");
        requireNonNull(name, "Parameter phones must not be null");
        this.id = id;
        this.name = name;
        this.address = address;
        this.phones = phones;
        phones.forEach(phone -> phone.setClient(this));
    }

    @Override
    public Client clone() {
        var clientClone = new Client(id, name);
        clientClone.setAddress(cloneAddress());
        clientClone.setPhones(clonePhones(clientClone));
        return clientClone;
    }

    private Address cloneAddress() {
        if (address != null) {
            return new Address(address.getId(), address.getStreet());
        }
        return null;
    }

    private List<Phone> clonePhones(Client clientClone) {
        if (isNull(phones)) {
            return new ArrayList<>();
        }
        return phones.stream()
                .map(phone -> {
                    var clonePhone = new Phone(phone.getId(), phone.getNumber());
                    clonePhone.setClient(clientClone);
                    return clonePhone;
                }).toList();
    }

    @Override
    public String toString() {
        return "Client{id=%d, name='%s', %s}".formatted(id, name, address);
    }
}

