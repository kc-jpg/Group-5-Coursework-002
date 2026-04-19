/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package beans;

import beans.exceptions.NonexistentEntityException;
import beans.exceptions.PreexistingEntityException;
import beans.exceptions.RollbackFailureException;
import entities.Drivers;
import java.io.Serializable;
import jakarta.persistence.Query;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import entities.Teams;
import entities.RaceResults;
import java.util.ArrayList;
import java.util.Collection;
import entities.Seasons;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.transaction.UserTransaction;
import java.util.List;

/**
 *
 * @author kacey
 */
public class DriversJpaController implements Serializable {

    public DriversJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Drivers drivers) throws PreexistingEntityException, RollbackFailureException, Exception {
        if (drivers.getRaceResultsCollection() == null) {
            drivers.setRaceResultsCollection(new ArrayList<RaceResults>());
        }
        if (drivers.getSeasonsCollection() == null) {
            drivers.setSeasonsCollection(new ArrayList<Seasons>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Teams teamId = drivers.getTeamId();
            if (teamId != null) {
                teamId = em.getReference(teamId.getClass(), teamId.getTeamId());
                drivers.setTeamId(teamId);
            }
            Collection<RaceResults> attachedRaceResultsCollection = new ArrayList<RaceResults>();
            for (RaceResults raceResultsCollectionRaceResultsToAttach : drivers.getRaceResultsCollection()) {
                raceResultsCollectionRaceResultsToAttach = em.getReference(raceResultsCollectionRaceResultsToAttach.getClass(), raceResultsCollectionRaceResultsToAttach.getResultId());
                attachedRaceResultsCollection.add(raceResultsCollectionRaceResultsToAttach);
            }
            drivers.setRaceResultsCollection(attachedRaceResultsCollection);
            Collection<Seasons> attachedSeasonsCollection = new ArrayList<Seasons>();
            for (Seasons seasonsCollectionSeasonsToAttach : drivers.getSeasonsCollection()) {
                seasonsCollectionSeasonsToAttach = em.getReference(seasonsCollectionSeasonsToAttach.getClass(), seasonsCollectionSeasonsToAttach.getSeasonYear());
                attachedSeasonsCollection.add(seasonsCollectionSeasonsToAttach);
            }
            drivers.setSeasonsCollection(attachedSeasonsCollection);
            em.persist(drivers);
            if (teamId != null) {
                teamId.getDriversCollection().add(drivers);
                teamId = em.merge(teamId);
            }
            for (RaceResults raceResultsCollectionRaceResults : drivers.getRaceResultsCollection()) {
                Drivers oldDriverIdOfRaceResultsCollectionRaceResults = raceResultsCollectionRaceResults.getDriverId();
                raceResultsCollectionRaceResults.setDriverId(drivers);
                raceResultsCollectionRaceResults = em.merge(raceResultsCollectionRaceResults);
                if (oldDriverIdOfRaceResultsCollectionRaceResults != null) {
                    oldDriverIdOfRaceResultsCollectionRaceResults.getRaceResultsCollection().remove(raceResultsCollectionRaceResults);
                    oldDriverIdOfRaceResultsCollectionRaceResults = em.merge(oldDriverIdOfRaceResultsCollectionRaceResults);
                }
            }
            for (Seasons seasonsCollectionSeasons : drivers.getSeasonsCollection()) {
                Drivers oldWinnerDriverIdOfSeasonsCollectionSeasons = seasonsCollectionSeasons.getWinnerDriverId();
                seasonsCollectionSeasons.setWinnerDriverId(drivers);
                seasonsCollectionSeasons = em.merge(seasonsCollectionSeasons);
                if (oldWinnerDriverIdOfSeasonsCollectionSeasons != null) {
                    oldWinnerDriverIdOfSeasonsCollectionSeasons.getSeasonsCollection().remove(seasonsCollectionSeasons);
                    oldWinnerDriverIdOfSeasonsCollectionSeasons = em.merge(oldWinnerDriverIdOfSeasonsCollectionSeasons);
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findDrivers(drivers.getDriverId()) != null) {
                throw new PreexistingEntityException("Drivers " + drivers + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Drivers drivers) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Drivers persistentDrivers = em.find(Drivers.class, drivers.getDriverId());
            Teams teamIdOld = persistentDrivers.getTeamId();
            Teams teamIdNew = drivers.getTeamId();
            Collection<RaceResults> raceResultsCollectionOld = persistentDrivers.getRaceResultsCollection();
            Collection<RaceResults> raceResultsCollectionNew = drivers.getRaceResultsCollection();
            Collection<Seasons> seasonsCollectionOld = persistentDrivers.getSeasonsCollection();
            Collection<Seasons> seasonsCollectionNew = drivers.getSeasonsCollection();
            if (teamIdNew != null) {
                teamIdNew = em.getReference(teamIdNew.getClass(), teamIdNew.getTeamId());
                drivers.setTeamId(teamIdNew);
            }
            Collection<RaceResults> attachedRaceResultsCollectionNew = new ArrayList<RaceResults>();
            for (RaceResults raceResultsCollectionNewRaceResultsToAttach : raceResultsCollectionNew) {
                raceResultsCollectionNewRaceResultsToAttach = em.getReference(raceResultsCollectionNewRaceResultsToAttach.getClass(), raceResultsCollectionNewRaceResultsToAttach.getResultId());
                attachedRaceResultsCollectionNew.add(raceResultsCollectionNewRaceResultsToAttach);
            }
            raceResultsCollectionNew = attachedRaceResultsCollectionNew;
            drivers.setRaceResultsCollection(raceResultsCollectionNew);
            Collection<Seasons> attachedSeasonsCollectionNew = new ArrayList<Seasons>();
            for (Seasons seasonsCollectionNewSeasonsToAttach : seasonsCollectionNew) {
                seasonsCollectionNewSeasonsToAttach = em.getReference(seasonsCollectionNewSeasonsToAttach.getClass(), seasonsCollectionNewSeasonsToAttach.getSeasonYear());
                attachedSeasonsCollectionNew.add(seasonsCollectionNewSeasonsToAttach);
            }
            seasonsCollectionNew = attachedSeasonsCollectionNew;
            drivers.setSeasonsCollection(seasonsCollectionNew);
            drivers = em.merge(drivers);
            if (teamIdOld != null && !teamIdOld.equals(teamIdNew)) {
                teamIdOld.getDriversCollection().remove(drivers);
                teamIdOld = em.merge(teamIdOld);
            }
            if (teamIdNew != null && !teamIdNew.equals(teamIdOld)) {
                teamIdNew.getDriversCollection().add(drivers);
                teamIdNew = em.merge(teamIdNew);
            }
            for (RaceResults raceResultsCollectionOldRaceResults : raceResultsCollectionOld) {
                if (!raceResultsCollectionNew.contains(raceResultsCollectionOldRaceResults)) {
                    raceResultsCollectionOldRaceResults.setDriverId(null);
                    raceResultsCollectionOldRaceResults = em.merge(raceResultsCollectionOldRaceResults);
                }
            }
            for (RaceResults raceResultsCollectionNewRaceResults : raceResultsCollectionNew) {
                if (!raceResultsCollectionOld.contains(raceResultsCollectionNewRaceResults)) {
                    Drivers oldDriverIdOfRaceResultsCollectionNewRaceResults = raceResultsCollectionNewRaceResults.getDriverId();
                    raceResultsCollectionNewRaceResults.setDriverId(drivers);
                    raceResultsCollectionNewRaceResults = em.merge(raceResultsCollectionNewRaceResults);
                    if (oldDriverIdOfRaceResultsCollectionNewRaceResults != null && !oldDriverIdOfRaceResultsCollectionNewRaceResults.equals(drivers)) {
                        oldDriverIdOfRaceResultsCollectionNewRaceResults.getRaceResultsCollection().remove(raceResultsCollectionNewRaceResults);
                        oldDriverIdOfRaceResultsCollectionNewRaceResults = em.merge(oldDriverIdOfRaceResultsCollectionNewRaceResults);
                    }
                }
            }
            for (Seasons seasonsCollectionOldSeasons : seasonsCollectionOld) {
                if (!seasonsCollectionNew.contains(seasonsCollectionOldSeasons)) {
                    seasonsCollectionOldSeasons.setWinnerDriverId(null);
                    seasonsCollectionOldSeasons = em.merge(seasonsCollectionOldSeasons);
                }
            }
            for (Seasons seasonsCollectionNewSeasons : seasonsCollectionNew) {
                if (!seasonsCollectionOld.contains(seasonsCollectionNewSeasons)) {
                    Drivers oldWinnerDriverIdOfSeasonsCollectionNewSeasons = seasonsCollectionNewSeasons.getWinnerDriverId();
                    seasonsCollectionNewSeasons.setWinnerDriverId(drivers);
                    seasonsCollectionNewSeasons = em.merge(seasonsCollectionNewSeasons);
                    if (oldWinnerDriverIdOfSeasonsCollectionNewSeasons != null && !oldWinnerDriverIdOfSeasonsCollectionNewSeasons.equals(drivers)) {
                        oldWinnerDriverIdOfSeasonsCollectionNewSeasons.getSeasonsCollection().remove(seasonsCollectionNewSeasons);
                        oldWinnerDriverIdOfSeasonsCollectionNewSeasons = em.merge(oldWinnerDriverIdOfSeasonsCollectionNewSeasons);
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
                String id = drivers.getDriverId();
                if (findDrivers(id) == null) {
                    throw new NonexistentEntityException("The drivers with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(String id) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Drivers drivers;
            try {
                drivers = em.getReference(Drivers.class, id);
                drivers.getDriverId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The drivers with id " + id + " no longer exists.", enfe);
            }
            Teams teamId = drivers.getTeamId();
            if (teamId != null) {
                teamId.getDriversCollection().remove(drivers);
                teamId = em.merge(teamId);
            }
            Collection<RaceResults> raceResultsCollection = drivers.getRaceResultsCollection();
            for (RaceResults raceResultsCollectionRaceResults : raceResultsCollection) {
                raceResultsCollectionRaceResults.setDriverId(null);
                raceResultsCollectionRaceResults = em.merge(raceResultsCollectionRaceResults);
            }
            Collection<Seasons> seasonsCollection = drivers.getSeasonsCollection();
            for (Seasons seasonsCollectionSeasons : seasonsCollection) {
                seasonsCollectionSeasons.setWinnerDriverId(null);
                seasonsCollectionSeasons = em.merge(seasonsCollectionSeasons);
            }
            em.remove(drivers);
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

    public List<Drivers> findDriversEntities() {
        return findDriversEntities(true, -1, -1);
    }

    public List<Drivers> findDriversEntities(int maxResults, int firstResult) {
        return findDriversEntities(false, maxResults, firstResult);
    }

    private List<Drivers> findDriversEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Drivers.class));
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

    public Drivers findDrivers(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Drivers.class, id);
        } finally {
            em.close();
        }
    }

    public int getDriversCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Drivers> rt = cq.from(Drivers.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
