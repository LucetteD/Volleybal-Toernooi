package nl.miwgroningen.cohort4.lucette.volleybalToernooi.repository;

import nl.miwgroningen.cohort4.lucette.volleybalToernooi.model.VolleybalUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * @author Lucette Das <l.k.das@st.hanze.nl>
 */

public interface VolleybalUserRepository extends JpaRepository<VolleybalUser, Integer> {
    Optional<VolleybalUser> findByUsername(String username);
}
