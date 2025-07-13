public class Admin extends User{
    public Admin(int id, String userName, String passwordHash) {
        super(id, userName, passwordHash, User.Role.ADMIN);
    }
}
