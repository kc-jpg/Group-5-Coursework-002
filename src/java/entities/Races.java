/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entities;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import java.io.Serializable;
import java.util.Collection;
import java.util.Date;

/**
 *
 * @author kacey
 */
@Entity
@Table(name = "RACES")
@NamedQueries({
    @NamedQuery(name = "Races.findAll", query = "SELECT r FROM Races r"),
    @NamedQuery(name = "Races.findByRaceId", query = "SELECT r FROM Races r WHERE r.raceId = :raceId"),
    @NamedQuery(name = "Races.findByRaceName", query = "SELECT r FROM Races r WHERE r.raceName = :raceName"),
    @NamedQuery(name = "Races.findByRaceDate", query = "SELECT r FROM Races r WHERE r.raceDate = :raceDate")})
public class Races implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "RACE_ID")
    private String raceId;
    @Column(name = "RACE_NAME")
    private String raceName;
    @Column(name = "RACE_DATE")
    @Temporal(TemporalType.DATE)
    private Date raceDate;
    @JoinColumn(name = "CIRCUIT_ID", referencedColumnName = "CIRCUIT_ID")
    @ManyToOne
    private Circuits circuitId;
    @OneToMany(mappedBy = "raceId")
    private Collection<RaceResults> raceResultsCollection;

    public Races() {
    }

    public Races(String raceId) {
        this.raceId = raceId;
    }

    public String getRaceId() {
        return raceId;
    }

    public void setRaceId(String raceId) {
        this.raceId = raceId;
    }

    public String getRaceName() {
        return raceName;
    }

    public void setRaceName(String raceName) {
        this.raceName = raceName;
    }

    public Date getRaceDate() {
        return raceDate;
    }

    public void setRaceDate(Date raceDate) {
        this.raceDate = raceDate;
    }

    public Circuits getCircuitId() {
        return circuitId;
    }

    public void setCircuitId(Circuits circuitId) {
        this.circuitId = circuitId;
    }

    public Collection<RaceResults> getRaceResultsCollection() {
        return raceResultsCollection;
    }

    public void setRaceResultsCollection(Collection<RaceResults> raceResultsCollection) {
        this.raceResultsCollection = raceResultsCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (raceId != null ? raceId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Races)) {
            return false;
        }
        Races other = (Races) object;
        if ((this.raceId == null && other.raceId != null) || (this.raceId != null && !this.raceId.equals(other.raceId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.Races[ raceId=" + raceId + " ]";
    }
    
}
