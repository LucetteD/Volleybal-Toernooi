package nl.miwgroningen.cohort4.lucette.volleybalToernooi.model;

import javax.persistence.*;

/**
 * @author Lucette Das <l.k.das@st.hanze.nl>
 */

@Entity
public class Role implements Comparable<Role> {



    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer roleId;

    private String roleName;

    public Role(){}

    public Role(String role) {
        roleName = role;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    @Override
    public int compareTo(Role role) {
        return this.roleName.compareTo(role.roleName);
    }
}
