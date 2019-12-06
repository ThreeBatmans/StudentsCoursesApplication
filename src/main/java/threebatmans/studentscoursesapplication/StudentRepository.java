package threebatmans.studentscoursesapplication;

import org.springframework.data.repository.CrudRepository;

import java.util.ArrayList;

public interface StudentRepository extends CrudRepository<Student,Long> {
    ArrayList<Student> findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(String firstname,String lastname);
}
