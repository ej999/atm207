package phase1;

public interface Login {
    void setPassword(String p);
    void setUsername(String u);
    boolean verifyLogin(String u, String p);
}
