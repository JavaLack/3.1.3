package web.springBootSecurityProject.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import web.springBootSecurityProject.models.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepositories extends JpaRepository<User, Integer> {
    @Query("SELECT DISTINCT u FROM User u JOIN FETCH u.roles WHERE u.username = :username")
    Optional<User> findByUsername(String username);
    @Query("SELECT DISTINCT u FROM User u JOIN FETCH u.roles")
    List<User> getAllUsers();
}
