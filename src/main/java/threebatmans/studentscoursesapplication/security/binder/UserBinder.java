package threebatmans.studentscoursesapplication.security.binder;

import org.hibernate.annotations.NaturalId;
import threebatmans.studentscoursesapplication.security.model.Authority;
import threebatmans.studentscoursesapplication.security.model.User;
import threebatmans.studentscoursesapplication.security.service.PasswordEncoder;

import javax.persistence.CascadeType;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import java.util.ArrayList;
import java.util.List;

public class UserBinder {

    private long id;
    private String username;
    private String password;
    private String passwordVerify;
    private List<Authority> authorities = new ArrayList<>();
    private String firstname;
    private String lastname;

    public UserBinder() {
    }

    public User toUser() {
        User user = new User();

        user.setId(this.id);
        user.setUsername(this.username);
        user.setPassword(PasswordEncoder.getInstance().encode(this.password));
        user.setAuthorities(new ArrayList<Authority>());
        user.getAuthorities().addAll(this.authorities);
        user.setFirstname(this.firstname);
        user.setLastname(this.lastname);

        return user;
    }

    public static UserBinder fromUser(User user) {
        UserBinder userBinder = new UserBinder();

        userBinder.setId(user.getId());
        userBinder.setUsername(user.getUsername());
        userBinder.setPassword("");
        userBinder.setAuthorities(new ArrayList<Authority>());
        userBinder.getAuthorities().addAll(user.getAuthorities());
        userBinder.setFirstname(user.getFirstname());
        userBinder.setLastname(user.getLastname());

        return userBinder;
    }

    public User merge(User user) {

        user.setId(this.getId());
        user.setUsername(this.getUsername());
        if (!this.getPassword().trim().equals(""))
            user.setPassword(PasswordEncoder.getInstance().encode(this.getPassword().trim()));
        user.setAuthorities(new ArrayList<Authority>());
        user.getAuthorities().addAll(this.getAuthorities());
        user.setFirstname(this.getFirstname());
        user.setLastname(this.getLastname());

        return user;
    }


    // TODO: Getter & Setter
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPasswordVerify() {
        return passwordVerify;
    }

    public void setPasswordVerify(String passwordVerify) {
        this.passwordVerify = passwordVerify;
    }

    public List<Authority> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(List<Authority> authorities) {
        this.authorities = authorities;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }
}
