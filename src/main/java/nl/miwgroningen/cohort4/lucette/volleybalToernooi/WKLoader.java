package nl.miwgroningen.cohort4.lucette.volleybalToernooi;

import com.github.javafaker.Faker;
import nl.miwgroningen.cohort4.lucette.volleybalToernooi.model.Player;
import nl.miwgroningen.cohort4.lucette.volleybalToernooi.model.Role;
import nl.miwgroningen.cohort4.lucette.volleybalToernooi.model.Team;
import nl.miwgroningen.cohort4.lucette.volleybalToernooi.repository.PlayerRepository;
import nl.miwgroningen.cohort4.lucette.volleybalToernooi.repository.RoleRepository;
import nl.miwgroningen.cohort4.lucette.volleybalToernooi.repository.TeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Vincent Velthuizen <v.r.velthuizen@pl.hanze.nl>
 */

@Component
public class WKLoader implements CommandLineRunner {

    // https://nl.wikipedia.org/wiki/Wereldkampioenschap_volleybal_vrouwen_2018
    private static final String[] NATIONALITIES = {
            "Kameroen", "Kenia",
            "Japan", "China", "Kazachstan", "Zuid-Korea", "Thailand",
            "Rusland", "Servië", "Turkije", "Italië", "Azerbeidzjan", "Duitsland", "Nederland", "Bulgarije",
            "Verenigde Staten", "Dominicaanse Republiek", "Puerto Rico", "Canada", "Cuba", "Mexico",
                "Trinidad en Tobego",
            "Brazilië", "Argentinië"
    };
    private static final String[] ROLES = {
            "Setter", "Libero", "Middle blocker/hitter", "Outside hitter", "Opposite hitter", "Coach", "PT"
    };

    private final PlayerRepository playerRepository;
    private final RoleRepository roleRepository;
    private final TeamRepository teamRepository;

    private final Faker faker;

    @Autowired
    public WKLoader(PlayerRepository playerRepository, RoleRepository roleRepository, TeamRepository teamRepository) {
        this.playerRepository = playerRepository;
        this.roleRepository = roleRepository;
        this.teamRepository = teamRepository;

        this.faker = new Faker();
    }

    @Override
    public void run(String... args) throws Exception {
        if (teamRepository.findAll().size() > 0) {
            return;
        }

        List<Role> roles = new ArrayList<>();

        for (String role : ROLES) {
            roles.add(new Role(role));
        }
        roleRepository.saveAll(roles);
        roleRepository.flush();

        for (String nationality : NATIONALITIES){
            Team team = new Team();
            team.setTeamName(nationality);
            if (nationality.equals("Nederland")) {
                // TODO add all flags to the folder so this can be done for all nationalities in the sample set
                team.setNationalFlag("images/team-images/Nederland.svg");
            }
            // team.setNationality(nationality);
            teamRepository.save(team);
            for (Role role : roles) {
                generatePlayerWithRoleAndTeam(role, team);
                generatePlayerWithRoleAndTeam(role, team);
                generatePlayerWithRoleAndTeam(role, team);
            }
        }
    }

    private Player generatePlayerWithRoleAndTeam(Role role, Team team) {
        Player player = new Player();

        player.setTeam(team);
        player.setBirthdate(LocalDate.ofInstant(faker.date().birthday(18, 32).toInstant(), ZoneId.systemDefault()));
        player.setRole(role);
        player.setCurrentClub(faker.team().name());
        player.setAssociationRegistrationNumber(faker.idNumber().toString());
        player.setHeight(faker.random().nextInt(170, 220));
        player.setFirstName(faker.name().firstName());
        player.setLastName(faker.name().lastName());

        playerRepository.save(player);
        return player;
    }
}
