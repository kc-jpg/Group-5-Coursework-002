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
@Table(name = "DRIVERS")
@NamedQueries({
    @NamedQuery(name = "Drivers.findAll", query = "SELECT d FROM Drivers d"),
    @NamedQuery(name = "Drivers.findByDriverId", query = "SELECT d FROM Drivers d WHERE d.driverId = :driverId"),
    @NamedQuery(name = "Drivers.findByName", query = "SELECT d FROM Drivers d WHERE d.name = :name"),
    @NamedQuery(name = "Drivers.findByNationality", query = "SELECT d FROM Drivers d WHERE d.nationality = :nationality"),
    @NamedQuery(name = "Drivers.findByDob", query = "SELECT d FROM Drivers d WHERE d.dob = :dob")})
public class Drivers implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "DRIVER_ID")
    private String driverId;
    @Basic(optional = false)
    @Column(name = "NAME")
    private String name;
    @Column(name = "NATIONALITY")
    private String nationality;
    @Column(name = "DOB")
    @Temporal(TemporalType.DATE)
    private Date dob;
    @OneToMany(mappedBy = "driverId")
    private Collection<RaceResults> raceResultsCollection;
    @JoinColumn(name = "TEAM_ID", referencedColumnName = "TEAM_ID")
    @ManyToOne
    private Teams teamId;
    @OneToMany(mappedBy = "winnerDriverId")
    private Collection<Seasons> seasonsCollection;

    public Drivers() {
    }

    public Drivers(String driverId) {
        this.driverId = driverId;
    }

    public Drivers(String driverId, String name) {
        this.driverId = driverId;
        this.name = name;
    }

    public String getDriverId() {
        return driverId;
    }

    public void setDriverId(String driverId) {
        this.driverId = driverId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public Date getDob() {
        return dob;
    }

    public void setDob(Date dob) {
        this.dob = dob;
    }

    public Collection<RaceResults> getRaceResultsCollection() {
        return raceResultsCollection;
    }

    public void setRaceResultsCollection(Collection<RaceResults> raceResultsCollection) {
        this.raceResultsCollection = raceResultsCollection;
    }

    public Teams getTeamId() {
        return teamId;
    }

    public void setTeamId(Teams teamId) {
        this.teamId = teamId;
    }

    public Collection<Seasons> getSeasonsCollection() {
        return seasonsCollection;
    }

    public void setSeasonsCollection(Collection<Seasons> seasonsCollection) {
        this.seasonsCollection = seasonsCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (driverId != null ? driverId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Drivers)) {
            return false;
        }
        Drivers other = (Drivers) object;
        if ((this.driverId == null && other.driverId != null) || (this.driverId != null && !this.driverId.equals(other.driverId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.Drivers[ driverId=" + driverId + " ]";
    }
    
}
