package app;

public final class UserSession {
    private static UserSession instance;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    private Integer id;
    private String username;
    private String lastName;
    private String registrationDate;

    private UserSession() {
    }
    public static UserSession getInstance() {
        if(instance == null) {
            UserSession.instance=new UserSession();
        }
        return instance;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(String registrationDate) {
        this.registrationDate = registrationDate;
    }

    public void clearUserSession() {
        username = null;
        lastName = null;
        registrationDate = null;
        instance=null;
    }
}
