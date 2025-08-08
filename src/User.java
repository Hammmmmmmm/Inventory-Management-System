import java.util.Objects;



public class User {
    private final String username;
    private final String passwordHash;
    private final Role userRole;

    protected User(String username, String passwordHash, Role userRole) {
        this.username = Objects.requireNonNull(username, "UserName cannot be null");
        this.passwordHash = Objects.requireNonNull(passwordHash, "Password cannot be null");
        this.userRole = Objects.requireNonNull(userRole);
    }

    public String getUsername() { return username; }
    public String getPasswordHash() { return passwordHash; }
    protected Role getRole() { return userRole; }
    public String toString() {
        return getUsername()  + ", " + getPasswordHash() + ", " + getRole().name();
    }
}