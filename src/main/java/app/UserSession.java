package app;

import com.google.gson.annotations.SerializedName;

public final class UserSession {
    private static UserSession instance;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @SerializedName("id")
    private Integer id;
    @SerializedName("username")
    private String username;
    @SerializedName("last_name")
    private String lastName;
    @SerializedName("registration_date")
    private String registrationDate;
    @SerializedName("token")
    private String token;
    @SerializedName("is_admin")
    private Boolean isAdmin;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Boolean getIs_admin() {
        return isAdmin;
    }

    public void setIs_admin(Boolean is_admin) {
        this.isAdmin = is_admin;
    }

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
        token = null;
        isAdmin = null;
        lastName = null;
        registrationDate = null;
        instance=null;
    }
}
