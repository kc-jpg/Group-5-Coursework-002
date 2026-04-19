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
import jakarta.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 *
 * @author kacey
 */
@Entity
@Table(name = "RACE_RESULTS")
@NamedQueries({
    @NamedQuery(name = "RaceResults.findAll", query = "SELECT r FROM RaceResults r"),
    @NamedQuery(name = "RaceResults.findByResultId", query = "SELECT r FROM RaceResults r WHERE r.resultId = :resultId"),
    @NamedQuery(name = "RaceResults.findByPosition", query = "SELECT r FROM RaceResults r WHERE r.position = :position"),
    @NamedQuery(name = "RaceResults.findByPointsEarned", query = "SELECT r FROM RaceResults r WHERE r.pointsEarned = :pointsEarned"),
    @NamedQuery(name = "RaceResults.findByStatus", query = "SELECT r FROM RaceResults r WHERE r.status = :status")})
public class RaceResults implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "RESULT_ID")
    private String resultId;
    @Column(name = "POSITION")
    private Integer position;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "POINTS_EARNED")
    private BigDecimal pointsEarned;
    @Column(name = "STATUS")
    private String status;
    @JoinColumn(name = "DRIVER_ID", referencedColumnName = "DRIVER_ID")
    @ManyToOne
    private Drivers driverId;
    @JoinColumn(name = "RACE_ID", referencedColumnName = "RACE_ID")
    @ManyToOne
    private Races raceId;
    @JoinColumn(name = "SEASON_YEAR", referencedColumnName = "SEASON_YEAR")
    @ManyToOne
    private Seasons seasonYear;

    public RaceResults() {
    }

    public RaceResults(String resultId) {
        this.resultId = resultId;
    }

    public String getResultId() {
        return resultId;
    }

    public void setResultId(String resultId) {
        this.resultId = resultId;
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    public BigDecimal getPointsEarned() {
        return pointsEarned;
    }

    public void setPointsEarned(BigDecimal pointsEarned) {
        this.pointsEarned = pointsEarned;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Drivers getDriverId() {
        return driverId;
    }

    public void setDriverId(Drivers driverId) {
        this.driverId = driverId;
    }

    public Races getRaceId() {
        return raceId;
    }

    public void setRaceId(Races raceId) {
        this.raceId = raceId;
    }

    public Seasons getSeasonYear() {
        return seasonYear;
    }

    public void setSeasonYear(Seasons seasonYear) {
        this.seasonYear = seasonYear;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (resultId != null ? resultId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof RaceResults)) {
            return false;
        }
        RaceResults other = (RaceResults) object;
        if ((this.resultId == null && other.resultId != null) || (this.resultId != null && !this.resultId.equals(other.resultId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.RaceResults[ resultId=" + resultId + " ]";
    }
    
}
