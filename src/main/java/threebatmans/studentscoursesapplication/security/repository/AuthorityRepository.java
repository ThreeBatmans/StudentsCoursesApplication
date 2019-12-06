package threebatmans.studentscoursesapplication.security.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import threebatmans.studentscoursesapplication.security.model.Authority;

import java.util.Optional;

@Repository
public interface AuthorityRepository extends CrudRepository<Authority, Long> {
    Optional<Authority> findByAuthority(String authority);
}
