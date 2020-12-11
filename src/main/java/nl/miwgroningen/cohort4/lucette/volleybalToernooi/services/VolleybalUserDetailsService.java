package nl.miwgroningen.cohort4.lucette.volleybalToernooi.services;

import nl.miwgroningen.cohort4.lucette.volleybalToernooi.repository.VolleybalUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * @author Lucette Das <l.k.das@st.hanze.nl>
 */

@Service
public class VolleybalUserDetailsService implements UserDetailsService {

    @Autowired
    VolleybalUserRepository volleybalUserRepository;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        return volleybalUserRepository.findByUsername(s).orElseThrow(
                () -> new UsernameNotFoundException("User " + s + " was not found.")
        );
    }

}
