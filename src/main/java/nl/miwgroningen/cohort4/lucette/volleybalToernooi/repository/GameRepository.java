package nl.miwgroningen.cohort4.lucette.volleybalToernooi.repository;

import nl.miwgroningen.cohort4.lucette.volleybalToernooi.model.Game;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Lucette Das <l.k.das@st.hanze.nl>
 */

public interface GameRepository extends JpaRepository<Game, Integer> {
}
