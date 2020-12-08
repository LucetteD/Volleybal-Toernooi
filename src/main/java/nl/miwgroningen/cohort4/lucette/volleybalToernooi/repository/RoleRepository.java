package nl.miwgroningen.cohort4.lucette.volleybalToernooi.repository;

import nl.miwgroningen.cohort4.lucette.volleybalToernooi.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * @author Lucette Das <l.k.das@st.hanze.nl>
 */

public interface RoleRepository extends JpaRepository<Role, Integer> {
    Optional<Role> findByRoleName(String roleName);
    boolean existsByRoleName(String roleName);
}
