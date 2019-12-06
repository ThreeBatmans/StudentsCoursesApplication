package threebatmans.studentscoursesapplication;

import org.springframework.data.repository.CrudRepository;

import java.util.ArrayList;

public interface CourseRepository extends CrudRepository<Course, Long> {
    ArrayList<Course> findByNameContainingIgnoreCase(String name);
}
