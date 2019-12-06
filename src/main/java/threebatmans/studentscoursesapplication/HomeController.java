package threebatmans.studentscoursesapplication;


import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import threebatmans.studentscoursesapplication.security.model.User;
import threebatmans.studentscoursesapplication.security.repository.UserRepository;

import javax.validation.Valid;
import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Controller
public class HomeController {

    @Autowired
    CourseRepository courseRepository;

    @Autowired
    StudentRepository studentRepository;

    @Autowired
    CloudinaryConfig cloudc;

    @Autowired
    UserRepository userRepository;

    @GetMapping("/")
    public String Homepage(Model model){
        model.addAttribute("courses",courseRepository.findAll());
        return "index";
    }

    @GetMapping("/register")
    public String registration(Model model){
        model.addAttribute("user",new User());
        return "Registration";
    }
    @GetMapping("/addstudent")
    public String addStudent(Model model){
        model.addAttribute("student",new Student());
        model.addAttribute("courses",courseRepository.findAll());
        return "studentform";
    }


    @PostMapping("/processstudent")
    public String processStudent(@Valid()@ModelAttribute Student student, @RequestParam("courseId") long id, @RequestParam("file") MultipartFile file, BindingResult result){

        //check for error

        if(file.isEmpty() && (student.getPic()==null || student.getPic().equals(""))){
            return "redirect:/addstudent";
        }
        if(result.hasErrors()){
            return "/addstudent";
        }

        //add course to student


      Course course = courseRepository.findById(id).get();
        Set<Course> cour;
        if(student.courses != null){
            cour= new HashSet<>(student.courses);
        }
        else{
           cour = new HashSet<>();
        }
        cour.add(course);
        student.setCourses(cour);

        //add student to course

        Set<Student> stud;
        if(course.students != null){
            stud= new HashSet<>(course.students);
        }
        else{
           stud = new HashSet<>();
        }

        // add pic to student class

        if(!file.isEmpty()){
            try {
                Map uploadResult = cloudc.upload(file.getBytes(), ObjectUtils.asMap("resourcetype", "auto"));
                student.setPic(uploadResult.get("url").toString());
                studentRepository.save(student);
            } catch (IOException e) {
                e.printStackTrace();
                return "redirect:/addstudent";
            }
        }
        else{
            studentRepository.save(student);
        }

        //save

        stud.add(student);
        course.setStudents(stud);
        courseRepository.save(course);
        return "redirect:/";
    }

    @GetMapping("/addcourse")
    public String addCoure(Model model){
        model.addAttribute("course", new Course());
        model.addAttribute("students", studentRepository.findAll());
        return "courseform";
    }

    @PostMapping("/processcourse")
    public String processDepartment(@ModelAttribute Course course){

        courseRepository.save(course);
        return "redirect:/";
    }



    //Delete, Update and detail student


    @RequestMapping("/detail/{id}")
    public String ViewDetail(@PathVariable("id") long id, Model model){
        model.addAttribute("student",studentRepository.findById(id).get());
        return "detailStudent";
    }
    @RequestMapping("/update/{id}")
    public String Update(@PathVariable("id") long id, Model model){
        model.addAttribute("student",studentRepository.findById(id).get());
        model.addAttribute("courses", courseRepository.findAll());
        return "studnetform";
    }
    @RequestMapping("/delete/{id}")
    public String Delete(@PathVariable("id") long id){
        studentRepository.deleteById(id);
        return "redirect:/";
    }



    //Delete, Update and detail course

    @RequestMapping("/detail/course/{id}")
    public String ViewDetailDep(@PathVariable("id") long id, Model model){
        model.addAttribute("course",courseRepository.findById(id).get());
        return "detailsCourse";
    }
    @RequestMapping("/update/course/{id}")
    public String UpdateDep(@PathVariable("id") long id, Model model){
        model.addAttribute("course",courseRepository.findById(id).get());
        model.addAttribute("students", studentRepository.findAll());
        return "courseform";
    }
    @RequestMapping("/delete/department/{id}")
    public String DeleteDep(@PathVariable("id") long id){
        courseRepository.deleteById(id);
        return "redirect:/";
    }

    // Search method for student or teacher

    @PostMapping("/searchcourse")
    public String SearchPageCourse(Model model, @RequestParam("search") String search) {
        model.addAttribute("courses",courseRepository.findByNameContainingIgnoreCase(search));
        return "searchindex1";
    }
    @PostMapping("/searchstudent")
    public String SearchPageStudent(Model model, @RequestParam("search") String search) {
        model.addAttribute("students",studentRepository.findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(search,search));
        return "searchindex2";
    }

}
