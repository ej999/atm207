package ATM;

import org.junit.Before;
import org.junit.Test;

public class OptionsTest {

    private Options options;

    @Before
    public void setUp() {
        options = new Options(new User_Employee_BankManager("jen", "1234"));

    }

    @Test
    public void testCreateUserPrompt() {
    }
}