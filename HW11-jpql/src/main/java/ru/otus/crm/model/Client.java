package ru.otus.crm.model;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.otus.crm.model.base.AbstractBaseEntity;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "client")
public class Client extends AbstractBaseEntity implements Cloneable {
    @NotBlank
    @Size(max = 50)
    @Column(name = "name")
    @NotNull
    private String name;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "address_id", unique = true)
    @NotNull
    private Address address;

    @OneToMany(mappedBy = "client", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @NotNull
    private List<Phone> phones;

    public Client(String name) {
        this(null, name);
    }

    public Client(Long id, String name) {
        Objects.requireNonNull(name, "name must not be null");
        this.id = id;
        this.name = name;
    }

    public Client(Long id, String name, Address address, List<Phone> phones) {
        Objects.requireNonNull(address, "address must not be null");
        Objects.requireNonNull(phones, "phones must not be null");
        this.id = id;
        this.name = name;
        this.address = address;
        this.phones = phones;
        phones.forEach(phone -> Objects.requireNonNull(phone, "phone must not be null").setClient(this));
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
        if (phones == null) {
            return null;
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
        return "Client{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", address=" + address +
                ", phones=" + phones +
                '}';
    }
}
