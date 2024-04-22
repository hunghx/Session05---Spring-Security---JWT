package api.example.com.springsecurityjwt.repository;

import api.example.com.springsecurityjwt.model.entity.Role;
import api.example.com.springsecurityjwt.model.entity.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IRoleRepository extends JpaRepository<Role,Long> {
    Optional<Role> findByRoleName(RoleName roleName);
}
