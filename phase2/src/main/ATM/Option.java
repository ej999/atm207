package ATM;

import java.lang.reflect.Method;
import java.util.*;

public class Option {
    private String displayName;

    //String inputNameDisplay [Class inputType, List inputOptions]
    private Map<String, List> inputFields;

    private Thread execution;

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public Map<String, List> getInputFields() {
        return inputFields;
    }

    public void setInputFields(Map<String, List> inputFields) {
        this.inputFields = inputFields;
    }

    public Thread getExecution() {
        return execution;
    }

    public void setExecution(Thread execution) {
        this.execution = execution;
    }

    public static void main(String[] args) {

        Option createUser = new Option();
            createUser.setDisplayName("Create an user.");

            HashMap<String, List> input_fields = new HashMap<>();
            input_fields.put("user type", Arrays.asList(String.class, UserManager.USER_TYPE_NAMES));
            input_fields.put("username", Arrays.asList(String.class, Collections.emptyList()));
            input_fields.put("password", Arrays.asList(String.class, Collections.emptyList()));
            createUser.setInputFields(input_fields);

            createUser.setExecution(new Thread(UserManager::createAccount));




//        createUser.getExecution().run();




    }


}
