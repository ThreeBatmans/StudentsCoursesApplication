package threebatmans.studentscoursesapplication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;

import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
  CourseRepository courseRepository;

    @Autowired
    StudentRepository studentRepository;




    @Override
    public void run(String... strings) throws Exception{

        Course course=new Course("Java101","Victor","Introduction to Java");
        Set<Course> cour = new HashSet<>();
        cour.add(course);
        Student student=new Student("Petros","Melake","1984","https://res.cloudinary.com/dyarncgbq/image/upload/v1575589919/cjmlg015rgoqfjfanamo.jpg",cour);




    }

}

