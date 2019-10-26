package publisaiz.functionalities.users;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.*;

public class UsersControllerTest {

    private final UserService userService = Mockito.mock(UserService.class);
    private final UsersController usersController = new UsersController(userService);

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void users() {
    }

    @Test
    public void update() {
    }

    @Test
    public void whoami() {
    }

    @Test
    public void logout() {
    }

    @Test
    public void getAuthors() {
    }

    @Test
    public void getMyProfile() {
    }

    @Test
    public void setMyProfile() {
    }

    @Test
    public void deleteMyProfile() {
    }
}