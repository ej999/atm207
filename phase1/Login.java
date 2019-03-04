package phase1;

interface Login {
    String getUsername();
    void setUsername(String u);
    String getPassword();
    void setPassword(String p);
    boolean verifyLogin(String u, String p);
}
