package vn.edu.hcmute.dao;

import java.util.Optional;
import vn.edu.hcmute.entity.User;

public interface IUserDao {
    User insert(User user);
    User update(User user);
    void delete(int userId);
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
}
