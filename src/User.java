import java.util.Objects;

public abstract class User {
    private final int id;
    private final String userName;
    private final String passwordHash;
    private final Role userRole;

    public enum Role {
        ADMIN,
        STAFF
    }

    protected User(int id, String userName, String passwordHash, Role userRole) {
        this.id = id;
        this.userName = Objects.requireNonNull(userName, "UserName cannot be null");
        this.passwordHash = Objects.requireNonNull(passwordHash, "Password cannot be null");
        this.userRole = Objects.requireNonNull(userRole);
    }

    public int getID() { return id; }
    public String getUsername() { return userName; }
    public String getPasswordHash() { return passwordHash; }
    protected Role getUserRole() { return userRole; }
    public String toString() {
        return getID() + ", " + getUsername()  + ", " + getPasswordHash() + ", " + getUserRole().name();
    }
}