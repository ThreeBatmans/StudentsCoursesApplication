package threebatmans.studentscoursesapplication.security.initilizer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import threebatmans.studentscoursesapplication.security.AuthorityType;
import threebatmans.studentscoursesapplication.security.model.Authority;
import threebatmans.studentscoursesapplication.security.model.User;
import threebatmans.studentscoursesapplication.security.repository.AuthorityRepository;
import threebatmans.studentscoursesapplication.security.repository.UserRepository;

import java.util.ArrayList;
import java.util.Arrays;

@Component
public class DatabaseInitializer implements CommandLineRunner {

    @Autowired
    AuthorityRepository authorityRepository;

    @Autowired
    UserRepository userRepository;

    @Override
    public void run(String... args) throws Exception {

        initAuthorities();
        initUser();

    }

    /**
     * Ensure all AuthorityType are in database
     *
     * <p>Ensure all AuthorityType are in database by adding each AuthorityType if it is not existed</p>
     *
     * @see Authority
     * @see AuthorityType
     */
    private void initAuthorities() {
        for (AuthorityType authorityType : AuthorityType.values()) {
            if (!authorityRepository.findByAuthority(authorityType.name()).isPresent()) {
                authorityRepository.save(new Authority(authorityType.name()));
            }
        }
    }

    /**
     * Ensure all User are in database
     *
     * <p>Ensure all User are in database by adding each User if it is not existed<</p>
     *
     * @see User
     *
     */
    private void initUser() {
        ArrayList<String> usernameList = new ArrayList<>(
                Arrays.asList(
                        "admin",
                        "user",
                        "victor",
                        "krissada",
                        "petros",
                        "efran"
                )
        );


        for (String username : usernameList) {

            if (!userRepository.findByUsername(username).isPresent()) {

                User user = new User(username);
                user.setAuthorities(new ArrayList<Authority>());
                userRepository.save(user);

                authorityRepository
                        .findByAuthority(AuthorityType.USER.name())
                        .ifPresent(user.getAuthorities()::add);

                // if "admin" add Authority.ADMIN
                if (username.equals("admin"))
                    authorityRepository
                            .findByAuthority(AuthorityType.ADMIN.name())
                            .ifPresent(user.getAuthorities()::add);

                userRepository.save(user);

            }
        }

    }
}
