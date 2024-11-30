import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import java.sql.*;
import org.junit.jupiter.api.*;
import org.mockito.*;

class LoginAppTest {

    @Mock
    Connection mockConnection;

    @Mock
    PreparedStatement mockPreparedStatement;

    @Mock
    ResultSet mockResultSet;

    LoginApp loginApp;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        loginApp = new LoginApp();
    }

    @Test
    void testValidLogin() throws SQLException {
        // Arrange
        String email = "user@example.com";
        String password = "correctpassword";
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getString("Name")).thenReturn("John Doe");

        String result = loginApp.authenticateUser(email, password);
        assertEquals("John Doe", result);
    }

    @Test
    void testInvalidLogin() throws SQLException {
        // Arrange
        String email = "user@example.com";
        String password = "wrongpassword";
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(false);

        String result = loginApp.authenticateUser(email, password);
        assertNull(result);
    }

    @Test
    void testEmptyEmail() {
        // Arrange
        String email = "";
        String password = "password";

        String result = loginApp.authenticateUser(email, password);
        assertNull(result);
    }

    @Test
    void testEmptyPassword() {
        // Arrange
        String email = "user@example.com";
        String password = "";

        String result = loginApp.authenticateUser(email, password);
        assertNull(result);
    }

    @Test
    void testDatabaseConnectionError() throws SQLException {
        // Arrange
        String email = "user@example.com";
        String password = "correctpassword";
        when(mockConnection.prepareStatement(anyString())).thenThrow(new SQLException("Database error"));

        String result = loginApp.authenticateUser(email, password);
        assertNull(result);
    }
}
