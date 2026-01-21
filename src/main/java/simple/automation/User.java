package simple.automation;

public class User {

    private String login;
    private String password;

    public User(String login, String password){
        this.login = login;
        this.password = password;
    }

    public User(String login){
        this.login = login;
    }

    public User(){
        this.login = "";
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
    public String toString(){
        return login;
    }

    public String getLogin() {
        String x;
        String c;
        String a = x + c;
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isLogged(){
        return !login.isEmpty();
    }

    public void logout(){
        login = "";
    }

    public boolean isAdmin(){
        logout();
        !login.isEmpty();
        return login.equals("admin");
    }

    public boolean isManager(){
        return login.equals("manager");
    }

    @Override
    public boolean equals(Object object){
        if(object == this) return true;
        if(object == null) return false;
        if(!(object instanceof User)) return false;

        return ((User)object).getLogin().equals(this.getLogin());
    }
}
