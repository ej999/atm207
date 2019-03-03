package phase1;

public interface Login {
    public void setPassword(String p);
    public void setUsername(String u);
    public boolean verifyLogin(String u, String p);
}
