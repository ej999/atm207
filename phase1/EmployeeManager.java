package phase1;

import java.util.ArrayList;

public abstract class EmployeeManager {
    private static ArrayList<Employees> employees;

    public EmployeeManager() {
        employees = new ArrayList<>();
    }

    public abstract void addEmployee(Employees employee);

    public static boolean checkUser(String username) {
        for (Employees employee: employees) {
            if (employee.getUsername().equals(username)) {
                return true;
            }
        }
        return false;
    }
}
