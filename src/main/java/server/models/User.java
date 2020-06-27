package server.models;

import org.mindrot.jbcrypt.BCrypt;
import server.pojo.UserPojo;

public class User {
    private static final String DEFAULT_NAME = "No Name";
    private static final String DEFAULT_PASS = "password";

    public int userid;
    public String username;
    private String passhash;

    public User() {
        this(DEFAULT_NAME, DEFAULT_PASS);
    }

    public User(UserPojo user) {
        this(user.username, user.password);
    }

    public User(String username, String password) {
        this(-1, username, password);
    }

    public User(int userid, String username, String password) {
        this.userid = userid;
        this.username = username;
        this.passhash = password;
    }

    public boolean checkPassword(String attempt) {
        boolean result = BCrypt.checkpw(attempt, this.passhash);
        return result;
    }
}