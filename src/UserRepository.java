import java.sql.SQLException;
import java.util.Optional;

import Exceptions.DataBaseConnectionException;
import Exceptions.InsufficientPermissionsException;
import Exceptions.UserNotLoggedInException;

public interface UserRepository {
    void createUsersTable() throws Exception;
    Optional<String> getHashedPassword(String userName) throws SQLException, DataBaseConnectionException;
    void insertUser(User user) throws DataBaseConnectionException, InsufficientPermissionsException, UserNotLoggedInException;
    Optional<User> findByUsername(String username) throws DataBaseConnectionException;
    boolean userExists(String username) throws DataBaseConnectionException;
}