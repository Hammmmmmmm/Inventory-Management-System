public class Staff extends User{
    public Staff(String userName, String passwordHash) {
        super(userName, passwordHash, User.Role.STAFF);
    }
}
