public class CurrentLoginState {
    private LoginState state = new AwaitingCredentialsState();
    public void setState(LoginState state) {this.state = state;}
    public void attemptLogin(String username, String password) {
        state.handleLogin(this, username, password);
    }
}
