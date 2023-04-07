package lt.code.academy.data;

public class User {
    private int id;
    private String userName;
    private String name;
    private String surName;
    private String password;
    private boolean is_admin;

    public User() {
    }

    public User(int id, String userName, String name, String surName, String password, boolean is_admin) {
        this.id = id;
        this.userName = userName;
        this.name = name;
        this.surName = surName;
        this.password = password;
        this.is_admin = is_admin;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSurName(String surName) {
        this.surName = surName;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setIs_admin(boolean is_admin) {
        this.is_admin = is_admin;
    }

    public int getId() {
        return id;
    }

    public String getUserName() {
        return userName;
    }

    public String getName() {
        return name;
    }

    public String getSurName() {
        return surName;
    }

    public String getPassword() {
        return password;
    }

    public boolean isIs_admin() {
        return is_admin;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", userName='" + userName + '\'' +
                ", name='" + name + '\'' +
                ", surName='" + surName + '\'' +
                ", password='" + password + '\'' +
                ", is_admin=" + is_admin +
                '}';
    }
}

