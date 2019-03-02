package phase1;

import java.util.ArrayList;

public class EmployeeManager {
    private  static ArrayList<Employees> employees;

    public EmployeeManager() {
        employees = new ArrayList<>();
    }

    public static void addEmployee(Employees employee){
        employees.add(employee);
    }

    public static boolean checkUser(String username) {
        for (Employees employee: employees) {
            if (employee.getUsername().equals(username)) {
                return true;
            }
        }
        return false;
    }
}
