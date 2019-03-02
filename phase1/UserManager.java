package phase1;
import java.util.ArrayList;

/* A class to manage users*
 */

public class UserManager {
    private  static ArrayList<User> users;

    public UserManager() {
        users = new ArrayList<>();
    }

    public static void addUser(User user){
        users.add(user);
    }

    public static boolean checkUser(String username) {
        for (User user: users) {
            if (user.getUsername() == username) {
                return true;
            }
        }
        return false;
    }
}
