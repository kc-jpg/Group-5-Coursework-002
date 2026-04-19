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
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author kacey
 */
@Entity
@Table(name = "BOOKMARKS")
@NamedQueries({
    @NamedQuery(name = "Bookmarks.findAll", query = "SELECT b FROM Bookmarks b"),
    @NamedQuery(name = "Bookmarks.findByBookmarkId", query = "SELECT b FROM Bookmarks b WHERE b.bookmarkId = :bookmarkId"),
    @NamedQuery(name = "Bookmarks.findByItemId", query = "SELECT b FROM Bookmarks b WHERE b.itemId = :itemId"),
    @NamedQuery(name = "Bookmarks.findByItemType", query = "SELECT b FROM Bookmarks b WHERE b.itemType = :itemType"),
    @NamedQuery(name = "Bookmarks.findByAddedAt", query = "SELECT b FROM Bookmarks b WHERE b.addedAt = :addedAt")})
public class Bookmarks implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "BOOKMARK_ID")
    private Integer bookmarkId;
    @Column(name = "ITEM_ID")
    private Integer itemId;
    @Column(name = "ITEM_TYPE")
    private String itemType;
    @Column(name = "ADDED_AT")
    @Temporal(TemporalType.TIMESTAMP)
    private Date addedAt;
    @JoinColumn(name = "USER_ID", referencedColumnName = "USER_ID")
    @ManyToOne
    private Users userId;

    public Bookmarks() {
    }

    public Bookmarks(Integer bookmarkId) {
        this.bookmarkId = bookmarkId;
    }

    public Integer getBookmarkId() {
        return bookmarkId;
    }

    public void setBookmarkId(Integer bookmarkId) {
        this.bookmarkId = bookmarkId;
    }

    public Integer getItemId() {
        return itemId;
    }

    public void setItemId(Integer itemId) {
        this.itemId = itemId;
    }

    public String getItemType() {
        return itemType;
    }

    public void setItemType(String itemType) {
        this.itemType = itemType;
    }

    public Date getAddedAt() {
        return addedAt;
    }

    public void setAddedAt(Date addedAt) {
        this.addedAt = addedAt;
    }

    public Users getUserId() {
        return userId;
    }

    public void setUserId(Users userId) {
        this.userId = userId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (bookmarkId != null ? bookmarkId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Bookmarks)) {
            return false;
        }
        Bookmarks other = (Bookmarks) object;
        if ((this.bookmarkId == null && other.bookmarkId != null) || (this.bookmarkId != null && !this.bookmarkId.equals(other.bookmarkId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.Bookmarks[ bookmarkId=" + bookmarkId + " ]";
    }
    
}
