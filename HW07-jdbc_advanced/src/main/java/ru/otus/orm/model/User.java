package ru.otus.orm.model;

import lombok.Getter;
import lombok.Setter;
import ru.otus.orm.entity.annotations.RepositoryField;
import ru.otus.orm.entity.annotations.RepositoryTable;
import ru.otus.orm.entity.AbstractBaseEntity;

@Getter
@Setter
@RepositoryTable(title = "users")
public final class User extends AbstractBaseEntity {

    @RepositoryField
    private String login;

    @RepositoryField
    private String password;

    @RepositoryField
    private String nickname;

    public User(String login, String password, String nickname) {
        this(null, login, password, nickname);
    }

    public User(Long id, String login, String password, String nickname) {
        super(id);
        this.login = login;
        this.password = password;
        this.nickname = nickname;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", login='" + login + '\'' +
                ", password='" + password + '\'' +
                ", nickname='" + nickname + '\'' +
                '}';
    }
}
