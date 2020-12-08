package nl.miwgroningen.cohort4.lucette.volleybalToernooi.model;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlIDREF;

/**
 * @author Lucette Das <l.k.das@st.hanze.nl>
 */

@Entity
public class GameParticipant {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Integer gameParticipantId;

    @ManyToOne
    Game game;

    @ManyToOne
    Player player;

}
