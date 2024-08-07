package otus.crm.dao;

import otus.crm.model.User;

import java.util.Optional;

public interface UserDao {
    Optional<User> findByLogin(String login);
}
