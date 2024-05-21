package ru.otus.advjdbc.reposistory;


import ru.otus.advjdbc.model.User;

import java.sql.SQLException;
import java.util.List;

public interface UsersDao {
    List<User> findAll() throws SQLException;
}
