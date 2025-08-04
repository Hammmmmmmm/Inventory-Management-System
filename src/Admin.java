public class Admin extends User{
    public Admin(String userName, String passwordHash) {
        super(userName, passwordHash, Role.ADMIN);
    }
}
