package nl.miwgroningen.cohort4.lucette.volleybalToernooi;

import nl.miwgroningen.cohort4.lucette.volleybalToernooi.repository.PlayerRepository;
import nl.miwgroningen.cohort4.lucette.volleybalToernooi.repository.RoleRepository;
import nl.miwgroningen.cohort4.lucette.volleybalToernooi.repository.TeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * @author Vincent Velthuizen <v.r.velthuizen@pl.hanze.nl>
 */

@Component
public class WKLoader implements CommandLineRunner {

    private static final String[] NATIONALITIES = {};
    private static final String[] ROLES = {};

    private final PlayerRepository playerRepository;
    private final RoleRepository roleRepository;
    private final TeamRepository teamRepository;

    @Autowired
    public WKLoader(PlayerRepository playerRepository, RoleRepository roleRepository, TeamRepository teamRepository) {
        this.playerRepository = playerRepository;
        this.roleRepository = roleRepository;
        this.teamRepository = teamRepository;
    }

    @Override
    public void run(String... args) throws Exception {

    }
}
