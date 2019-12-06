package threebatmans.studentscoursesapplication.security.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import threebatmans.studentscoursesapplication.security.model.User;
import threebatmans.studentscoursesapplication.security.repository.UserRepository;

import java.util.Optional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {

        Optional<User> optionalUser = userRepository.findByUsername(userName);

        if (!optionalUser.isPresent())
            throw new UsernameNotFoundException("User '"+userName+"' not found.");

        return optionalUser.get();
    }

}
