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
import entities.Teams;
import entities.RaceResults;
import entities.Seasons;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.transaction.UserTransaction;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 *
 * @author kacey
 */
public class SeasonsJpaController implements Serializable {

    public SeasonsJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Seasons seasons) throws PreexistingEntityException, RollbackFailureException, Exception {
        if (seasons.getRaceResultsCollection() == null) {
            seasons.setRaceResultsCollection(new ArrayList<RaceResults>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Drivers winnerDriverId = seasons.getWinnerDriverId();
            if (winnerDriverId != null) {
                winnerDriverId = em.getReference(winnerDriverId.getClass(), winnerDriverId.getDriverId());
                seasons.setWinnerDriverId(winnerDriverId);
            }
            Teams winnerConstructorId = seasons.getWinnerConstructorId();
            if (winnerConstructorId != null) {
                winnerConstructorId = em.getReference(winnerConstructorId.getClass(), winnerConstructorId.getTeamId());
                seasons.setWinnerConstructorId(winnerConstructorId);
            }
            Collection<RaceResults> attachedRaceResultsCollection = new ArrayList<RaceResults>();
            for (RaceResults raceResultsCollectionRaceResultsToAttach : seasons.getRaceResultsCollection()) {
                raceResultsCollectionRaceResultsToAttach = em.getReference(raceResultsCollectionRaceResultsToAttach.getClass(), raceResultsCollectionRaceResultsToAttach.getResultId());
                attachedRaceResultsCollection.add(raceResultsCollectionRaceResultsToAttach);
            }
            seasons.setRaceResultsCollection(attachedRaceResultsCollection);
            em.persist(seasons);
            if (winnerDriverId != null) {
                winnerDriverId.getSeasonsCollection().add(seasons);
                winnerDriverId = em.merge(winnerDriverId);
            }
            if (winnerConstructorId != null) {
                winnerConstructorId.getSeasonsCollection().add(seasons);
                winnerConstructorId = em.merge(winnerConstructorId);
            }
            for (RaceResults raceResultsCollectionRaceResults : seasons.getRaceResultsCollection()) {
                Seasons oldSeasonYearOfRaceResultsCollectionRaceResults = raceResultsCollectionRaceResults.getSeasonYear();
                raceResultsCollectionRaceResults.setSeasonYear(seasons);
                raceResultsCollectionRaceResults = em.merge(raceResultsCollectionRaceResults);
                if (oldSeasonYearOfRaceResultsCollectionRaceResults != null) {
                    oldSeasonYearOfRaceResultsCollectionRaceResults.getRaceResultsCollection().remove(raceResultsCollectionRaceResults);
                    oldSeasonYearOfRaceResultsCollectionRaceResults = em.merge(oldSeasonYearOfRaceResultsCollectionRaceResults);
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findSeasons(seasons.getSeasonYear()) != null) {
                throw new PreexistingEntityException("Seasons " + seasons + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Seasons seasons) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Seasons persistentSeasons = em.find(Seasons.class, seasons.getSeasonYear());
            Drivers winnerDriverIdOld = persistentSeasons.getWinnerDriverId();
            Drivers winnerDriverIdNew = seasons.getWinnerDriverId();
            Teams winnerConstructorIdOld = persistentSeasons.getWinnerConstructorId();
            Teams winnerConstructorIdNew = seasons.getWinnerConstructorId();
            Collection<RaceResults> raceResultsCollectionOld = persistentSeasons.getRaceResultsCollection();
            Collection<RaceResults> raceResultsCollectionNew = seasons.getRaceResultsCollection();
            if (winnerDriverIdNew != null) {
                winnerDriverIdNew = em.getReference(winnerDriverIdNew.getClass(), winnerDriverIdNew.getDriverId());
                seasons.setWinnerDriverId(winnerDriverIdNew);
            }
            if (winnerConstructorIdNew != null) {
                winnerConstructorIdNew = em.getReference(winnerConstructorIdNew.getClass(), winnerConstructorIdNew.getTeamId());
                seasons.setWinnerConstructorId(winnerConstructorIdNew);
            }
            Collection<RaceResults> attachedRaceResultsCollectionNew = new ArrayList<RaceResults>();
            for (RaceResults raceResultsCollectionNewRaceResultsToAttach : raceResultsCollectionNew) {
                raceResultsCollectionNewRaceResultsToAttach = em.getReference(raceResultsCollectionNewRaceResultsToAttach.getClass(), raceResultsCollectionNewRaceResultsToAttach.getResultId());
                attachedRaceResultsCollectionNew.add(raceResultsCollectionNewRaceResultsToAttach);
            }
            raceResultsCollectionNew = attachedRaceResultsCollectionNew;
            seasons.setRaceResultsCollection(raceResultsCollectionNew);
            seasons = em.merge(seasons);
            if (winnerDriverIdOld != null && !winnerDriverIdOld.equals(winnerDriverIdNew)) {
                winnerDriverIdOld.getSeasonsCollection().remove(seasons);
                winnerDriverIdOld = em.merge(winnerDriverIdOld);
            }
            if (winnerDriverIdNew != null && !winnerDriverIdNew.equals(winnerDriverIdOld)) {
                winnerDriverIdNew.getSeasonsCollection().add(seasons);
                winnerDriverIdNew = em.merge(winnerDriverIdNew);
            }
            if (winnerConstructorIdOld != null && !winnerConstructorIdOld.equals(winnerConstructorIdNew)) {
                winnerConstructorIdOld.getSeasonsCollection().remove(seasons);
                winnerConstructorIdOld = em.merge(winnerConstructorIdOld);
            }
            if (winnerConstructorIdNew != null && !winnerConstructorIdNew.equals(winnerConstructorIdOld)) {
                winnerConstructorIdNew.getSeasonsCollection().add(seasons);
                winnerConstructorIdNew = em.merge(winnerConstructorIdNew);
            }
            for (RaceResults raceResultsCollectionOldRaceResults : raceResultsCollectionOld) {
                if (!raceResultsCollectionNew.contains(raceResultsCollectionOldRaceResults)) {
                    raceResultsCollectionOldRaceResults.setSeasonYear(null);
                    raceResultsCollectionOldRaceResults = em.merge(raceResultsCollectionOldRaceResults);
                }
            }
            for (RaceResults raceResultsCollectionNewRaceResults : raceResultsCollectionNew) {
                if (!raceResultsCollectionOld.contains(raceResultsCollectionNewRaceResults)) {
                    Seasons oldSeasonYearOfRaceResultsCollectionNewRaceResults = raceResultsCollectionNewRaceResults.getSeasonYear();
                    raceResultsCollectionNewRaceResults.setSeasonYear(seasons);
                    raceResultsCollectionNewRaceResults = em.merge(raceResultsCollectionNewRaceResults);
                    if (oldSeasonYearOfRaceResultsCollectionNewRaceResults != null && !oldSeasonYearOfRaceResultsCollectionNewRaceResults.equals(seasons)) {
                        oldSeasonYearOfRaceResultsCollectionNewRaceResults.getRaceResultsCollection().remove(raceResultsCollectionNewRaceResults);
                        oldSeasonYearOfRaceResultsCollectionNewRaceResults = em.merge(oldSeasonYearOfRaceResultsCollectionNewRaceResults);
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
                Integer id = seasons.getSeasonYear();
                if (findSeasons(id) == null) {
                    throw new NonexistentEntityException("The seasons with id " + id + " no longer exists.");
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
            Seasons seasons;
            try {
                seasons = em.getReference(Seasons.class, id);
                seasons.getSeasonYear();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The seasons with id " + id + " no longer exists.", enfe);
            }
            Drivers winnerDriverId = seasons.getWinnerDriverId();
            if (winnerDriverId != null) {
                winnerDriverId.getSeasonsCollection().remove(seasons);
                winnerDriverId = em.merge(winnerDriverId);
            }
            Teams winnerConstructorId = seasons.getWinnerConstructorId();
            if (winnerConstructorId != null) {
                winnerConstructorId.getSeasonsCollection().remove(seasons);
                winnerConstructorId = em.merge(winnerConstructorId);
            }
            Collection<RaceResults> raceResultsCollection = seasons.getRaceResultsCollection();
            for (RaceResults raceResultsCollectionRaceResults : raceResultsCollection) {
                raceResultsCollectionRaceResults.setSeasonYear(null);
                raceResultsCollectionRaceResults = em.merge(raceResultsCollectionRaceResults);
            }
            em.remove(seasons);
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

    public List<Seasons> findSeasonsEntities() {
        return findSeasonsEntities(true, -1, -1);
    }

    public List<Seasons> findSeasonsEntities(int maxResults, int firstResult) {
        return findSeasonsEntities(false, maxResults, firstResult);
    }

    private List<Seasons> findSeasonsEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Seasons.class));
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

    public Seasons findSeasons(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Seasons.class, id);
        } finally {
            em.close();
        }
    }

    public int getSeasonsCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Seasons> rt = cq.from(Seasons.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
