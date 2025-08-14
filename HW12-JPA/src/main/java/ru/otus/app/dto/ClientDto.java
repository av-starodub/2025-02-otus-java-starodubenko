package ru.otus.app.dto;

import lombok.Data;
import ru.otus.app.model.Client;

@Data
public class ClientDto implements Dto<Client> {

    private Long id;

    private String name;

    @Override
    public Client toDomainObject() {
        return new Client(id, name);
    }
}
