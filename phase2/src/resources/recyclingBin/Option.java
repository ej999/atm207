//package recyclingBin;
//
//import java.lang.reflect.Method;
//import java.util.*;
//
//class Option {
//    private String displayName;
//    // Key is the display name; value is [Class inputType, List inputOptions]
//    private Map<String, List> inputList;
//    private Method execution;
//    private Option nextOption;
//
//    public String getDisplayName() {
//        return displayName;
//    }
//
//    public void setDisplayName(String displayName) {
//        this.displayName = displayName;
//    }
//
//    public Map<String, List> getInputList() {
//        return inputList;
//    }
//
//    public void setInputList(Map<String, List> inputFields) {
//        this.inputList = inputFields;
//    }
//
//    public Method getExecution() {
//        return execution;
//    }
//
//    public Option getNextOption() {
//        return nextOption;
//    }
//
//    public void setNextOption(Option nextOption) {
//        this.nextOption = nextOption;
//    }
//
//    public void setExecution(Method execution) {
//        this.execution = execution;
//    }
//
//    @Override
//    public String toString() {
//        return "Option{" +
//                "displayName='" + displayName + '\'' +
//                ", inputList=" + inputList +
//                ", execution=" + execution +
//                '}';
//    }
//
//    public static void main(String[] args) {
//        List<Option> optionList = new ArrayList<>();
//
//        Option createUser = new Option();
//        createUser.setDisplayName("Create an user");
//        HashMap<String, List> input_fields = new HashMap<>();
//        input_fields.put("user type", Arrays.asList(String.class, userManager.USER_TYPE_NAMES));
//        input_fields.put("username", Collections.singletonList(String.class));
//        input_fields.put("password", Arrays.asList(String.class, Collections.emptyList()));
//        createUser.setInputList(input_fields);
//        try {
//            Method method = userManager.class.getMethod("createAccount", String.class, String.class, String.class);
//            createUser.setExecution(method);
//        } catch (NoSuchMethodException e) {
//            System.out.println("ouch!");
//        }
//    }
//
//    class Input {
//        private String displayName;
//        private Class type;
//
//
//        public String getDisplayName() {
//            return displayName;
//        }
//
//        public void setDisplayName(String displayName) {
//            this.displayName = displayName;
//        }
//    }
//
//}
