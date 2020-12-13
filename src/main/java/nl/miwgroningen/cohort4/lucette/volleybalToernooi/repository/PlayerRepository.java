package nl.miwgroningen.cohort4.lucette.volleybalToernooi.repository;

import nl.miwgroningen.cohort4.lucette.volleybalToernooi.model.Player;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author Lucette Das <l.k.das@st.hanze.nl>
 */

public interface PlayerRepository extends JpaRepository<Player, Integer> {
    boolean existsByAssociationRegistrationNumber(String associationRegistrationNumber);
    List<Player> findPlayersByLastName(String lastName);
}
