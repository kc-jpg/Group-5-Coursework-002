/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package beans;

import beans.exceptions.NonexistentEntityException;
import beans.exceptions.RollbackFailureException;
import java.io.Serializable;
import jakarta.persistence.Query;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import entities.Drivers;
import java.util.ArrayList;
import java.util.Collection;
import entities.Seasons;
import entities.Teams;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.transaction.UserTransaction;
import java.util.List;

/**
 *
 * @author kacey
 */
public class TeamsJpaController implements Serializable {

    public TeamsJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Teams teams) throws RollbackFailureException, Exception {
        if (teams.getDriversCollection() == null) {
            teams.setDriversCollection(new ArrayList<Drivers>());
        }
        if (teams.getSeasonsCollection() == null) {
            teams.setSeasonsCollection(new ArrayList<Seasons>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Collection<Drivers> attachedDriversCollection = new ArrayList<Drivers>();
            for (Drivers driversCollectionDriversToAttach : teams.getDriversCollection()) {
                driversCollectionDriversToAttach = em.getReference(driversCollectionDriversToAttach.getClass(), driversCollectionDriversToAttach.getDriverId());
                attachedDriversCollection.add(driversCollectionDriversToAttach);
            }
            teams.setDriversCollection(attachedDriversCollection);
            Collection<Seasons> attachedSeasonsCollection = new ArrayList<Seasons>();
            for (Seasons seasonsCollectionSeasonsToAttach : teams.getSeasonsCollection()) {
                seasonsCollectionSeasonsToAttach = em.getReference(seasonsCollectionSeasonsToAttach.getClass(), seasonsCollectionSeasonsToAttach.getSeasonYear());
                attachedSeasonsCollection.add(seasonsCollectionSeasonsToAttach);
            }
            teams.setSeasonsCollection(attachedSeasonsCollection);
            em.persist(teams);
            for (Drivers driversCollectionDrivers : teams.getDriversCollection()) {
                Teams oldTeamIdOfDriversCollectionDrivers = driversCollectionDrivers.getTeamId();
                driversCollectionDrivers.setTeamId(teams);
                driversCollectionDrivers = em.merge(driversCollectionDrivers);
                if (oldTeamIdOfDriversCollectionDrivers != null) {
                    oldTeamIdOfDriversCollectionDrivers.getDriversCollection().remove(driversCollectionDrivers);
                    oldTeamIdOfDriversCollectionDrivers = em.merge(oldTeamIdOfDriversCollectionDrivers);
                }
            }
            for (Seasons seasonsCollectionSeasons : teams.getSeasonsCollection()) {
                Teams oldWinnerConstructorIdOfSeasonsCollectionSeasons = seasonsCollectionSeasons.getWinnerConstructorId();
                seasonsCollectionSeasons.setWinnerConstructorId(teams);
                seasonsCollectionSeasons = em.merge(seasonsCollectionSeasons);
                if (oldWinnerConstructorIdOfSeasonsCollectionSeasons != null) {
                    oldWinnerConstructorIdOfSeasonsCollectionSeasons.getSeasonsCollection().remove(seasonsCollectionSeasons);
                    oldWinnerConstructorIdOfSeasonsCollectionSeasons = em.merge(oldWinnerConstructorIdOfSeasonsCollectionSeasons);
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Teams teams) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Teams persistentTeams = em.find(Teams.class, teams.getTeamId());
            Collection<Drivers> driversCollectionOld = persistentTeams.getDriversCollection();
            Collection<Drivers> driversCollectionNew = teams.getDriversCollection();
            Collection<Seasons> seasonsCollectionOld = persistentTeams.getSeasonsCollection();
            Collection<Seasons> seasonsCollectionNew = teams.getSeasonsCollection();
            Collection<Drivers> attachedDriversCollectionNew = new ArrayList<Drivers>();
            for (Drivers driversCollectionNewDriversToAttach : driversCollectionNew) {
                driversCollectionNewDriversToAttach = em.getReference(driversCollectionNewDriversToAttach.getClass(), driversCollectionNewDriversToAttach.getDriverId());
                attachedDriversCollectionNew.add(driversCollectionNewDriversToAttach);
            }
            driversCollectionNew = attachedDriversCollectionNew;
            teams.setDriversCollection(driversCollectionNew);
            Collection<Seasons> attachedSeasonsCollectionNew = new ArrayList<Seasons>();
            for (Seasons seasonsCollectionNewSeasonsToAttach : seasonsCollectionNew) {
                seasonsCollectionNewSeasonsToAttach = em.getReference(seasonsCollectionNewSeasonsToAttach.getClass(), seasonsCollectionNewSeasonsToAttach.getSeasonYear());
                attachedSeasonsCollectionNew.add(seasonsCollectionNewSeasonsToAttach);
            }
            seasonsCollectionNew = attachedSeasonsCollectionNew;
            teams.setSeasonsCollection(seasonsCollectionNew);
            teams = em.merge(teams);
            for (Drivers driversCollectionOldDrivers : driversCollectionOld) {
                if (!driversCollectionNew.contains(driversCollectionOldDrivers)) {
                    driversCollectionOldDrivers.setTeamId(null);
                    driversCollectionOldDrivers = em.merge(driversCollectionOldDrivers);
                }
            }
            for (Drivers driversCollectionNewDrivers : driversCollectionNew) {
                if (!driversCollectionOld.contains(driversCollectionNewDrivers)) {
                    Teams oldTeamIdOfDriversCollectionNewDrivers = driversCollectionNewDrivers.getTeamId();
                    driversCollectionNewDrivers.setTeamId(teams);
                    driversCollectionNewDrivers = em.merge(driversCollectionNewDrivers);
                    if (oldTeamIdOfDriversCollectionNewDrivers != null && !oldTeamIdOfDriversCollectionNewDrivers.equals(teams)) {
                        oldTeamIdOfDriversCollectionNewDrivers.getDriversCollection().remove(driversCollectionNewDrivers);
                        oldTeamIdOfDriversCollectionNewDrivers = em.merge(oldTeamIdOfDriversCollectionNewDrivers);
                    }
                }
            }
            for (Seasons seasonsCollectionOldSeasons : seasonsCollectionOld) {
                if (!seasonsCollectionNew.contains(seasonsCollectionOldSeasons)) {
                    seasonsCollectionOldSeasons.setWinnerConstructorId(null);
                    seasonsCollectionOldSeasons = em.merge(seasonsCollectionOldSeasons);
                }
            }
            for (Seasons seasonsCollectionNewSeasons : seasonsCollectionNew) {
                if (!seasonsCollectionOld.contains(seasonsCollectionNewSeasons)) {
                    Teams oldWinnerConstructorIdOfSeasonsCollectionNewSeasons = seasonsCollectionNewSeasons.getWinnerConstructorId();
                    seasonsCollectionNewSeasons.setWinnerConstructorId(teams);
                    seasonsCollectionNewSeasons = em.merge(seasonsCollectionNewSeasons);
                    if (oldWinnerConstructorIdOfSeasonsCollectionNewSeasons != null && !oldWinnerConstructorIdOfSeasonsCollectionNewSeasons.equals(teams)) {
                        oldWinnerConstructorIdOfSeasonsCollectionNewSeasons.getSeasonsCollection().remove(seasonsCollectionNewSeasons);
                        oldWinnerConstructorIdOfSeasonsCollectionNewSeasons = em.merge(oldWinnerConstructorIdOfSeasonsCollectionNewSeasons);
                    }
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = teams.getTeamId();
                if (findTeams(id) == null) {
                    throw new NonexistentEntityException("The teams with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Teams teams;
            try {
                teams = em.getReference(Teams.class, id);
                teams.getTeamId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The teams with id " + id + " no longer exists.", enfe);
            }
            Collection<Drivers> driversCollection = teams.getDriversCollection();
            for (Drivers driversCollectionDrivers : driversCollection) {
                driversCollectionDrivers.setTeamId(null);
                driversCollectionDrivers = em.merge(driversCollectionDrivers);
            }
            Collection<Seasons> seasonsCollection = teams.getSeasonsCollection();
            for (Seasons seasonsCollectionSeasons : seasonsCollection) {
                seasonsCollectionSeasons.setWinnerConstructorId(null);
                seasonsCollectionSeasons = em.merge(seasonsCollectionSeasons);
            }
            em.remove(teams);
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Teams> findTeamsEntities() {
        return findTeamsEntities(true, -1, -1);
    }

    public List<Teams> findTeamsEntities(int maxResults, int firstResult) {
        return findTeamsEntities(false, maxResults, firstResult);
    }

    private List<Teams> findTeamsEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Teams.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Teams findTeams(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Teams.class, id);
        } finally {
            em.close();
        }
    }

    public int getTeamsCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Teams> rt = cq.from(Teams.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
