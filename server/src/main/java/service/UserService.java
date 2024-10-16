package service;

import dataaccess.memory.MemAuth;
import model.AuthData;
import model.UserData;
import dataaccess.UserDAO;
import dataaccess.AuthDAO;

public class UserService {
    public AuthData register(UserData user){
        UserDAO.createUser(user);
    }

    public AuthData login(UserData user){
        AuthDAO.login(user.username());
        return AuthDAO.fetchToken(user.username);
    }

    public void logout(UserData user){
        MemAuth
    }
}
