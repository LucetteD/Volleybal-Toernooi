package nl.miwgroningen.cohort4.lucette.volleybalToernooi.model;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 * @author Vincent Velthuizen <v.r.velthuizen@pl.hanze.nl>
 */
@Entity
public class PouleGame extends Game {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "pouleId", referencedColumnName = "pouleId", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    Poule poule;

    public Poule getPoule() {
        return poule;
    }

    public void setPoule(Poule poule) {
        this.poule = poule;
    }
}
