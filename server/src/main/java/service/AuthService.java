package service;

public class AuthService implements service {
    public void clear(){
        AuthAccess.clearTokens();
    }
}
