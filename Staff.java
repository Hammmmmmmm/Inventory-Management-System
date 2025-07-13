public class Staff extends User{
    public Staff(int id, String userName, String passwordHash) {
        super(id, userName, passwordHash, User.Role.STAFF);
    }
}
