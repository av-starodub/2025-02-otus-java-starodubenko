package ru.otus.advjdbc.reposistory;


import ru.otus.advjdbc.model.User;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class UsersDaoImpl implements UsersDao {
    private final DataSource dataSource;

    public UsersDaoImpl(DataSource ds) {
        dataSource = ds;
    }

    @Override
    public List<User> findAll() throws SQLException {
        List<User> out = new ArrayList<>();
        try (var connection = dataSource.getConnection();
             var pst = connection.prepareStatement("select * from users;");
             var rs = pst.executeQuery()) {
            while (rs.next()) {
                User user = new User(rs.getLong("id"), rs.getString("login"), rs.getString("password"), rs.getString("nickname"));
                out.add(user);
            }
        }
        return Collections.unmodifiableList(out);
    }
}
