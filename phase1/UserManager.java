import java.util.ArrayList;

public class UserManager {
    // Manage Users
    private  static ArrayList<User> users;

    public UserManager() {
        users = new ArrayList<>();
    }

    public static void addAccount(User user){
        users.add(user);
    }

}
