//package phase1;
//
//import java.util.HashMap;
//import java.util.Map;
//
///**
// * A class to manage the list of customers' login accounts.
// */
//class LoginManager_Customer extends LoginManager {
////    /** A mapping of username to Login_Customer*/
////    private static HashMap<String, Login_Customer> user_map;
//
////    LoginManager_Customer() {
////        user_map = new HashMap<>();
////    }
//
////    static void addLogin(Login_Customer user) {
////        user_map.put(user.getUsername(), user);
////    }
//
////    boolean checkLoginExistence(String username) {
////        Login l = user_map.get(username);
////        return l != null;
////    }
////
////    public boolean verifyLogin(String u, String p) {
////        Login l = user_map.get(u);
////        // Username exists.
////        if (l != null) {
////            // Check whether password is correct.
////            return l.getPassword().equals(p);
////        // Username doesn't exist.
////        } else {
////            return false;
////        }
////    }
//}
