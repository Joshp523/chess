package dataaccess.memory;
import dataaccess.DataAccessException;
import dataaccess.UserDAO;
import model.UserData;
import java.util.HashMap;

public class MemUser implements UserDAO {

    final private HashMap<String, UserData> userList
            = new HashMap<>();

    public void createUser(UserData u)throws DataAccessException {
        if(userList.get(u.username())!=null){
            throw new DataAccessException("Error: already taken");
        }
        userList.put(u.username(), u);
    }

    public void clearUsers() {
        userList.clear();
    }

    @Override
    public UserData findByUnPwd(String username, String password) throws DataAccessException{
        UserData checkMe = userList.get(username);
        if (checkMe != null && checkMe.password().equals(password)) {
            return checkMe;
        }else throw new DataAccessException("Error: unauthorized");
    }

    @Override
    public UserData findByUsername(String username) {
        return userList.get(username);
    }

    public HashMap<String, UserData> getUserList() {
        return userList;
    }
}
