package threebatmans.studentscoursesapplication.security.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class RedirectSecurityController {



        @GetMapping("/register")
        private String user_create() {
            return "redirect:/user/create";
        }

}
