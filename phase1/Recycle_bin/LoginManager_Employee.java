//package phase1;
//
//import java.util.HashMap;
//import java.util.Map;
//
///**
// * A class to manage employees.
// */
//
//class LoginManager_Employee extends LoginManager {
//    private static HashMap<String, Login_Employee> employee_map;
//
//    LoginManager_Employee() {
//        employee_map = new HashMap<>();
//    }
//
//    static void addLogin(Login_Employee employee) {
//        employee_map.put(employee.getUsername(), employee);
//    }
//
//    static boolean checkUser(String username, String password) {
//        for (Map.Entry<String, Login_Employee> employeeEntry : employee_map.entrySet()) {
//            if (employeeEntry.getKey().equals(username) & employeeEntry.getValue().getPassword().equals(password)) {
//                return true;
//            }
//        }
//        return false;
//    }
//}
