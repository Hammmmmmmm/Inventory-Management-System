import Exceptions.DataBaseConnectionException;
import Exceptions.InsufficientPermissionsException;
import Exceptions.UserNotLoggedInException;

interface UserService {
    void createUser(User user) throws InsufficientPermissionsException, UserNotLoggedInException, DataBaseConnectionException;
    boolean authenticateUser(String username, String password) throws DataBaseConnectionException;
}

public class DataBaseUserService implements UserService {

    @Override
    public void createUser(User user)
            throws InsufficientPermissionsException, UserNotLoggedInException, DataBaseConnectionException {
        
    }

    @Override
    public boolean authenticateUser(String username, String password) throws DataBaseConnectionException {
        Main main = (Main) Main.getDatabaseHandler();
        main.showScreen(username);
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'authenticateUser'");
    }
    
}