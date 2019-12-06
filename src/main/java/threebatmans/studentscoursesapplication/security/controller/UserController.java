package threebatmans.studentscoursesapplication.security.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import threebatmans.studentscoursesapplication.security.binder.UserBinder;
import threebatmans.studentscoursesapplication.security.model.Authority;
import threebatmans.studentscoursesapplication.security.model.User;
import threebatmans.studentscoursesapplication.security.repository.AuthorityRepository;
import threebatmans.studentscoursesapplication.security.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthorityRepository authorityRepository;

    @ModelAttribute
    private void init(Model model) {
        model.addAttribute("authorityList", authorityRepository.findAllByOrderByAuthority());
    }

    @GetMapping("/create")
    private String create(Model model) {

        model.addAttribute("user", new User());
        model.addAttribute("action","/user/create");
        model.addAttribute("method", "POST");
        return "Registration";
    }

    @PostMapping("/create")
    private String createProcess(Model model, @ModelAttribute UserBinder userBinder) {

        model.addAttribute("action","/user/create");
        model.addAttribute("method", "POST");

        if (userBinder.getId() != 0) {
            model.addAttribute("error", "The id must be 0 for new user");
            userBinder.setId(0);
            model.addAttribute("user", userBinder);

            return "Registration";
        }

        // if BOTH password and passwordVerify is not empty and it is not match!
        if (!(userBinder.getPassword().trim().equals("") && userBinder.getPasswordVerify().trim().equals("") &&
                userBinder.getPassword().trim().equals(userBinder.getPasswordVerify().trim())))  {
            model.addAttribute("error", "Password and Password Verify do not match.");
            model.addAttribute("user", userBinder);
            return "user_update";
        }

        userRepository.save(userBinder.toUser());

        return "redirect:/user/retrieve?success=Created&id="+userBinder.getId();
    }

    @GetMapping("/retrieve")
    private String retrieve(Model model, @RequestParam long id) {

        Optional<User> optionalUser = userRepository.findById(id);

        if (!optionalUser.isPresent()) {
            model.addAttribute("error", String.format("No such user with id=%d.", id));
        }
        model.addAttribute("info", String.format("Found user with id=%d.", id));
        model.addAttribute("user", optionalUser.orElse(new User()));

        return "user_retrieve";
    }
    @GetMapping("/update")
    private String update(Model model, @RequestParam long id) {

        Optional<User> optionalUser = userRepository.findById(id);

        if (!optionalUser.isPresent()) {
            model.addAttribute("error", String.format("No such user with id=%d.", id));
        }

        model.addAttribute("user", UserBinder.fromUser(optionalUser.orElse(new User())));

        model.addAttribute("action","/user/update");
        model.addAttribute("method", "POST");

        return "user_update";
    }

    @PostMapping("/update")
    private String updateProcess(Model model, @ModelAttribute UserBinder userBinder) {
        Optional<User> optionalUser = userRepository.findById(userBinder.getId());

        if (!optionalUser.isPresent()) {
            model.addAttribute("error", String.format("No such user with id=%d.", userBinder.getId()));
            return "user_update";
        }
        // if BOTH password and passwordVerify is not empty and it is not match!
        if (!(userBinder.getPassword().trim().equals("") && userBinder.getPasswordVerify().trim().equals("") &&
                userBinder.getPassword().trim().equals(userBinder.getPasswordVerify().trim())))  {
            model.addAttribute("error", String.format("Password and Password Verify do not match.", userBinder.getId()));
            model.addAttribute("user", userBinder);
            return "user_update";
        }

        userRepository.save(userBinder.merge(optionalUser.get()));

        return "redirect:/user/retrieve?success=Saved&id="+userBinder.getId();
    }

    @RequestMapping("/delete")
    private String deleteProcess(Model model, @RequestParam long id) {

        Optional<User> optionalUser = userRepository.findById(id);

        if (!optionalUser.isPresent()) {
            model.addAttribute("error", String.format("No such user with id=%d.", id));
            return "redirect:/user/update?error=No+such+user";
        }

        User user = optionalUser.get();
        if (user.getAuthorities() != null) {
            List<Authority> authorityList = user.getAuthorities();
            for (Authority authority : authorityList) {
                authority.getUserList().remove(user);
                authorityList.remove(authority);
                authorityRepository.save(authority);
            }
            user.setAuthorities(null);
            userRepository.save(user);
        }

        userRepository.deleteById(id);
        model.addAttribute("success", String.format("User with id=%d was deleted.", id));

        return "user_delete";
    }


}
