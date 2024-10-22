package passoff.server;

import dataaccess.DataAccessException;
import dataaccess.memory.MemAuth;
import dataaccess.memory.MemGame;
import dataaccess.memory.MemUser;
import model.UserData;
import org.junit.jupiter.api.Test;
import server.Server;
import service.Service;

import static org.junit.jupiter.api.Assertions.*;

public class HomemadeTests {
    private final Server server = new Server();
    private final Service service = new Service(new MemUser(), new MemAuth(), new MemGame() );

    public HomemadeTests() {}

    @Test
    public void registerBen() throws DataAccessException {
        service.createUser(new UserData("ben", "pass", "cosmo@byu.edu"));
        assertEquals(1, service.getUserList().size());
    }

}
