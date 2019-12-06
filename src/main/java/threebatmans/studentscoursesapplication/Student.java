package threebatmans.studentscoursesapplication;


import javax.persistence.*;
import java.util.Set;

@Entity
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String firstName;
    private String lastName;
    private String DoB;
    private String pic;

    @ManyToMany(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    public
    Set<Course> courses;

    public Student() {
    }

    public Student(String firstName, String lastName, String doB, String pic, Set<Course> courses) {
        this.firstName = firstName;
        this.lastName = lastName;
        DoB = doB;
        this.pic = pic;
        this.courses = courses;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getDoB() {
        return DoB;
    }

    public void setDoB(String doB) {
        DoB = doB;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public Set<Course> getCourses() {
        return courses;
    }

    public void setCourses(Set<Course> courses) {
        this.courses = courses;
    }
}
