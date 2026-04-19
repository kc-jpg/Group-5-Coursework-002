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
import java.io.Serializable;
import java.util.Collection;

/**
 *
 * @author kacey
 */
@Entity
@Table(name = "SEASONS")
@NamedQueries({
    @NamedQuery(name = "Seasons.findAll", query = "SELECT s FROM Seasons s"),
    @NamedQuery(name = "Seasons.findBySeasonYear", query = "SELECT s FROM Seasons s WHERE s.seasonYear = :seasonYear")})
public class Seasons implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "SEASON_YEAR")
    private Integer seasonYear;
    @OneToMany(mappedBy = "seasonYear")
    private Collection<RaceResults> raceResultsCollection;
    @JoinColumn(name = "WINNER_DRIVER_ID", referencedColumnName = "DRIVER_ID")
    @ManyToOne
    private Drivers winnerDriverId;
    @JoinColumn(name = "WINNER_CONSTRUCTOR_ID", referencedColumnName = "TEAM_ID")
    @ManyToOne
    private Teams winnerConstructorId;

    public Seasons() {
    }

    public Seasons(Integer seasonYear) {
        this.seasonYear = seasonYear;
    }

    public Integer getSeasonYear() {
        return seasonYear;
    }

    public void setSeasonYear(Integer seasonYear) {
        this.seasonYear = seasonYear;
    }

    public Collection<RaceResults> getRaceResultsCollection() {
        return raceResultsCollection;
    }

    public void setRaceResultsCollection(Collection<RaceResults> raceResultsCollection) {
        this.raceResultsCollection = raceResultsCollection;
    }

    public Drivers getWinnerDriverId() {
        return winnerDriverId;
    }

    public void setWinnerDriverId(Drivers winnerDriverId) {
        this.winnerDriverId = winnerDriverId;
    }

    public Teams getWinnerConstructorId() {
        return winnerConstructorId;
    }

    public void setWinnerConstructorId(Teams winnerConstructorId) {
        this.winnerConstructorId = winnerConstructorId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (seasonYear != null ? seasonYear.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Seasons)) {
            return false;
        }
        Seasons other = (Seasons) object;
        if ((this.seasonYear == null && other.seasonYear != null) || (this.seasonYear != null && !this.seasonYear.equals(other.seasonYear))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.Seasons[ seasonYear=" + seasonYear + " ]";
    }
    
}
