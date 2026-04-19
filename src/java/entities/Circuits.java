/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entities;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collection;

/**
 *
 * @author kacey
 */
@Entity
@Table(name = "CIRCUITS")
@NamedQueries({
    @NamedQuery(name = "Circuits.findAll", query = "SELECT c FROM Circuits c"),
    @NamedQuery(name = "Circuits.findByCircuitId", query = "SELECT c FROM Circuits c WHERE c.circuitId = :circuitId"),
    @NamedQuery(name = "Circuits.findByName", query = "SELECT c FROM Circuits c WHERE c.name = :name"),
    @NamedQuery(name = "Circuits.findByLocation", query = "SELECT c FROM Circuits c WHERE c.location = :location"),
    @NamedQuery(name = "Circuits.findByLength", query = "SELECT c FROM Circuits c WHERE c.length = :length"),
    @NamedQuery(name = "Circuits.findByTurns", query = "SELECT c FROM Circuits c WHERE c.turns = :turns"),
    @NamedQuery(name = "Circuits.findByType", query = "SELECT c FROM Circuits c WHERE c.type = :type")})
public class Circuits implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "CIRCUIT_ID")
    private String circuitId;
    @Basic(optional = false)
    @Column(name = "NAME")
    private String name;
    @Column(name = "LOCATION")
    private String location;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "LENGTH")
    private BigDecimal length;
    @Column(name = "TURNS")
    private Integer turns;
    @Column(name = "TYPE")
    private String type;
    @OneToMany(mappedBy = "circuitId")
    private Collection<Races> racesCollection;

    public Circuits() {
    }

    public Circuits(String circuitId) {
        this.circuitId = circuitId;
    }

    public Circuits(String circuitId, String name) {
        this.circuitId = circuitId;
        this.name = name;
    }

    public String getCircuitId() {
        return circuitId;
    }

    public void setCircuitId(String circuitId) {
        this.circuitId = circuitId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public BigDecimal getLength() {
        return length;
    }

    public void setLength(BigDecimal length) {
        this.length = length;
    }

    public Integer getTurns() {
        return turns;
    }

    public void setTurns(Integer turns) {
        this.turns = turns;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Collection<Races> getRacesCollection() {
        return racesCollection;
    }

    public void setRacesCollection(Collection<Races> racesCollection) {
        this.racesCollection = racesCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (circuitId != null ? circuitId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Circuits)) {
            return false;
        }
        Circuits other = (Circuits) object;
        if ((this.circuitId == null && other.circuitId != null) || (this.circuitId != null && !this.circuitId.equals(other.circuitId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.Circuits[ circuitId=" + circuitId + " ]";
    }
    
}
