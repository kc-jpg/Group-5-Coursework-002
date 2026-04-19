/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entities;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
@Table(name = "TEAMS")
@NamedQueries({
    @NamedQuery(name = "Teams.findAll", query = "SELECT t FROM Teams t"),
    @NamedQuery(name = "Teams.findByTeamId", query = "SELECT t FROM Teams t WHERE t.teamId = :teamId"),
    @NamedQuery(name = "Teams.findByTeamName", query = "SELECT t FROM Teams t WHERE t.teamName = :teamName"),
    @NamedQuery(name = "Teams.findByBaseCountry", query = "SELECT t FROM Teams t WHERE t.baseCountry = :baseCountry"),
    @NamedQuery(name = "Teams.findByPrincipal", query = "SELECT t FROM Teams t WHERE t.principal = :principal"),
    @NamedQuery(name = "Teams.findByEngine", query = "SELECT t FROM Teams t WHERE t.engine = :engine")})
public class Teams implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "TEAM_ID")
    private Integer teamId;
    @Basic(optional = false)
    @Column(name = "TEAM_NAME")
    private String teamName;
    @Column(name = "BASE_COUNTRY")
    private String baseCountry;
    @Column(name = "PRINCIPAL")
    private String principal;
    @Column(name = "ENGINE")
    private String engine;
    @OneToMany(mappedBy = "teamId")
    private Collection<Drivers> driversCollection;
    @OneToMany(mappedBy = "winnerConstructorId")
    private Collection<Seasons> seasonsCollection;

    public Teams() {
    }

    public Teams(Integer teamId) {
        this.teamId = teamId;
    }

    public Teams(Integer teamId, String teamName) {
        this.teamId = teamId;
        this.teamName = teamName;
    }

    public Integer getTeamId() {
        return teamId;
    }

    public void setTeamId(Integer teamId) {
        this.teamId = teamId;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public String getBaseCountry() {
        return baseCountry;
    }

    public void setBaseCountry(String baseCountry) {
        this.baseCountry = baseCountry;
    }

    public String getPrincipal() {
        return principal;
    }

    public void setPrincipal(String principal) {
        this.principal = principal;
    }

    public String getEngine() {
        return engine;
    }

    public void setEngine(String engine) {
        this.engine = engine;
    }

    public Collection<Drivers> getDriversCollection() {
        return driversCollection;
    }

    public void setDriversCollection(Collection<Drivers> driversCollection) {
        this.driversCollection = driversCollection;
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
        hash += (teamId != null ? teamId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Teams)) {
            return false;
        }
        Teams other = (Teams) object;
        if ((this.teamId == null && other.teamId != null) || (this.teamId != null && !this.teamId.equals(other.teamId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.Teams[ teamId=" + teamId + " ]";
    }
    
}
