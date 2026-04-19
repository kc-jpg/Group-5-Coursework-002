/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package beans;

import beans.exceptions.NonexistentEntityException;
import beans.exceptions.PreexistingEntityException;
import beans.exceptions.RollbackFailureException;
import java.io.Serializable;
import jakarta.persistence.Query;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import entities.Drivers;
import entities.RaceResults;
import entities.Races;
import entities.Seasons;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.transaction.UserTransaction;
import java.util.List;

/**
 *
 * @author kacey
 */
public class RaceResultsJpaController implements Serializable {

    public RaceResultsJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(RaceResults raceResults) throws PreexistingEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Drivers driverId = raceResults.getDriverId();
            if (driverId != null) {
                driverId = em.getReference(driverId.getClass(), driverId.getDriverId());
                raceResults.setDriverId(driverId);
            }
            Races raceId = raceResults.getRaceId();
            if (raceId != null) {
                raceId = em.getReference(raceId.getClass(), raceId.getRaceId());
                raceResults.setRaceId(raceId);
            }
            Seasons seasonYear = raceResults.getSeasonYear();
            if (seasonYear != null) {
                seasonYear = em.getReference(seasonYear.getClass(), seasonYear.getSeasonYear());
                raceResults.setSeasonYear(seasonYear);
            }
            em.persist(raceResults);
            if (driverId != null) {
                driverId.getRaceResultsCollection().add(raceResults);
                driverId = em.merge(driverId);
            }
            if (raceId != null) {
                raceId.getRaceResultsCollection().add(raceResults);
                raceId = em.merge(raceId);
            }
            if (seasonYear != null) {
                seasonYear.getRaceResultsCollection().add(raceResults);
                seasonYear = em.merge(seasonYear);
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findRaceResults(raceResults.getResultId()) != null) {
                throw new PreexistingEntityException("RaceResults " + raceResults + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(RaceResults raceResults) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            RaceResults persistentRaceResults = em.find(RaceResults.class, raceResults.getResultId());
            Drivers driverIdOld = persistentRaceResults.getDriverId();
            Drivers driverIdNew = raceResults.getDriverId();
            Races raceIdOld = persistentRaceResults.getRaceId();
            Races raceIdNew = raceResults.getRaceId();
            Seasons seasonYearOld = persistentRaceResults.getSeasonYear();
            Seasons seasonYearNew = raceResults.getSeasonYear();
            if (driverIdNew != null) {
                driverIdNew = em.getReference(driverIdNew.getClass(), driverIdNew.getDriverId());
                raceResults.setDriverId(driverIdNew);
            }
            if (raceIdNew != null) {
                raceIdNew = em.getReference(raceIdNew.getClass(), raceIdNew.getRaceId());
                raceResults.setRaceId(raceIdNew);
            }
            if (seasonYearNew != null) {
                seasonYearNew = em.getReference(seasonYearNew.getClass(), seasonYearNew.getSeasonYear());
                raceResults.setSeasonYear(seasonYearNew);
            }
            raceResults = em.merge(raceResults);
            if (driverIdOld != null && !driverIdOld.equals(driverIdNew)) {
                driverIdOld.getRaceResultsCollection().remove(raceResults);
                driverIdOld = em.merge(driverIdOld);
            }
            if (driverIdNew != null && !driverIdNew.equals(driverIdOld)) {
                driverIdNew.getRaceResultsCollection().add(raceResults);
                driverIdNew = em.merge(driverIdNew);
            }
            if (raceIdOld != null && !raceIdOld.equals(raceIdNew)) {
                raceIdOld.getRaceResultsCollection().remove(raceResults);
                raceIdOld = em.merge(raceIdOld);
            }
            if (raceIdNew != null && !raceIdNew.equals(raceIdOld)) {
                raceIdNew.getRaceResultsCollection().add(raceResults);
                raceIdNew = em.merge(raceIdNew);
            }
            if (seasonYearOld != null && !seasonYearOld.equals(seasonYearNew)) {
                seasonYearOld.getRaceResultsCollection().remove(raceResults);
                seasonYearOld = em.merge(seasonYearOld);
            }
            if (seasonYearNew != null && !seasonYearNew.equals(seasonYearOld)) {
                seasonYearNew.getRaceResultsCollection().add(raceResults);
                seasonYearNew = em.merge(seasonYearNew);
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
                String id = raceResults.getResultId();
                if (findRaceResults(id) == null) {
                    throw new NonexistentEntityException("The raceResults with id " + id + " no longer exists.");
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
            RaceResults raceResults;
            try {
                raceResults = em.getReference(RaceResults.class, id);
                raceResults.getResultId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The raceResults with id " + id + " no longer exists.", enfe);
            }
            Drivers driverId = raceResults.getDriverId();
            if (driverId != null) {
                driverId.getRaceResultsCollection().remove(raceResults);
                driverId = em.merge(driverId);
            }
            Races raceId = raceResults.getRaceId();
            if (raceId != null) {
                raceId.getRaceResultsCollection().remove(raceResults);
                raceId = em.merge(raceId);
            }
            Seasons seasonYear = raceResults.getSeasonYear();
            if (seasonYear != null) {
                seasonYear.getRaceResultsCollection().remove(raceResults);
                seasonYear = em.merge(seasonYear);
            }
            em.remove(raceResults);
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

    public List<RaceResults> findRaceResultsEntities() {
        return findRaceResultsEntities(true, -1, -1);
    }

    public List<RaceResults> findRaceResultsEntities(int maxResults, int firstResult) {
        return findRaceResultsEntities(false, maxResults, firstResult);
    }

    private List<RaceResults> findRaceResultsEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(RaceResults.class));
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

    public RaceResults findRaceResults(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(RaceResults.class, id);
        } finally {
            em.close();
        }
    }

    public int getRaceResultsCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<RaceResults> rt = cq.from(RaceResults.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
