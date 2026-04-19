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
import entities.Circuits;
import entities.RaceResults;
import entities.Races;
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
public class RacesJpaController implements Serializable {

    public RacesJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Races races) throws PreexistingEntityException, RollbackFailureException, Exception {
        if (races.getRaceResultsCollection() == null) {
            races.setRaceResultsCollection(new ArrayList<RaceResults>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Circuits circuitId = races.getCircuitId();
            if (circuitId != null) {
                circuitId = em.getReference(circuitId.getClass(), circuitId.getCircuitId());
                races.setCircuitId(circuitId);
            }
            Collection<RaceResults> attachedRaceResultsCollection = new ArrayList<RaceResults>();
            for (RaceResults raceResultsCollectionRaceResultsToAttach : races.getRaceResultsCollection()) {
                raceResultsCollectionRaceResultsToAttach = em.getReference(raceResultsCollectionRaceResultsToAttach.getClass(), raceResultsCollectionRaceResultsToAttach.getResultId());
                attachedRaceResultsCollection.add(raceResultsCollectionRaceResultsToAttach);
            }
            races.setRaceResultsCollection(attachedRaceResultsCollection);
            em.persist(races);
            if (circuitId != null) {
                circuitId.getRacesCollection().add(races);
                circuitId = em.merge(circuitId);
            }
            for (RaceResults raceResultsCollectionRaceResults : races.getRaceResultsCollection()) {
                Races oldRaceIdOfRaceResultsCollectionRaceResults = raceResultsCollectionRaceResults.getRaceId();
                raceResultsCollectionRaceResults.setRaceId(races);
                raceResultsCollectionRaceResults = em.merge(raceResultsCollectionRaceResults);
                if (oldRaceIdOfRaceResultsCollectionRaceResults != null) {
                    oldRaceIdOfRaceResultsCollectionRaceResults.getRaceResultsCollection().remove(raceResultsCollectionRaceResults);
                    oldRaceIdOfRaceResultsCollectionRaceResults = em.merge(oldRaceIdOfRaceResultsCollectionRaceResults);
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findRaces(races.getRaceId()) != null) {
                throw new PreexistingEntityException("Races " + races + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Races races) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Races persistentRaces = em.find(Races.class, races.getRaceId());
            Circuits circuitIdOld = persistentRaces.getCircuitId();
            Circuits circuitIdNew = races.getCircuitId();
            Collection<RaceResults> raceResultsCollectionOld = persistentRaces.getRaceResultsCollection();
            Collection<RaceResults> raceResultsCollectionNew = races.getRaceResultsCollection();
            if (circuitIdNew != null) {
                circuitIdNew = em.getReference(circuitIdNew.getClass(), circuitIdNew.getCircuitId());
                races.setCircuitId(circuitIdNew);
            }
            Collection<RaceResults> attachedRaceResultsCollectionNew = new ArrayList<RaceResults>();
            for (RaceResults raceResultsCollectionNewRaceResultsToAttach : raceResultsCollectionNew) {
                raceResultsCollectionNewRaceResultsToAttach = em.getReference(raceResultsCollectionNewRaceResultsToAttach.getClass(), raceResultsCollectionNewRaceResultsToAttach.getResultId());
                attachedRaceResultsCollectionNew.add(raceResultsCollectionNewRaceResultsToAttach);
            }
            raceResultsCollectionNew = attachedRaceResultsCollectionNew;
            races.setRaceResultsCollection(raceResultsCollectionNew);
            races = em.merge(races);
            if (circuitIdOld != null && !circuitIdOld.equals(circuitIdNew)) {
                circuitIdOld.getRacesCollection().remove(races);
                circuitIdOld = em.merge(circuitIdOld);
            }
            if (circuitIdNew != null && !circuitIdNew.equals(circuitIdOld)) {
                circuitIdNew.getRacesCollection().add(races);
                circuitIdNew = em.merge(circuitIdNew);
            }
            for (RaceResults raceResultsCollectionOldRaceResults : raceResultsCollectionOld) {
                if (!raceResultsCollectionNew.contains(raceResultsCollectionOldRaceResults)) {
                    raceResultsCollectionOldRaceResults.setRaceId(null);
                    raceResultsCollectionOldRaceResults = em.merge(raceResultsCollectionOldRaceResults);
                }
            }
            for (RaceResults raceResultsCollectionNewRaceResults : raceResultsCollectionNew) {
                if (!raceResultsCollectionOld.contains(raceResultsCollectionNewRaceResults)) {
                    Races oldRaceIdOfRaceResultsCollectionNewRaceResults = raceResultsCollectionNewRaceResults.getRaceId();
                    raceResultsCollectionNewRaceResults.setRaceId(races);
                    raceResultsCollectionNewRaceResults = em.merge(raceResultsCollectionNewRaceResults);
                    if (oldRaceIdOfRaceResultsCollectionNewRaceResults != null && !oldRaceIdOfRaceResultsCollectionNewRaceResults.equals(races)) {
                        oldRaceIdOfRaceResultsCollectionNewRaceResults.getRaceResultsCollection().remove(raceResultsCollectionNewRaceResults);
                        oldRaceIdOfRaceResultsCollectionNewRaceResults = em.merge(oldRaceIdOfRaceResultsCollectionNewRaceResults);
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
                String id = races.getRaceId();
                if (findRaces(id) == null) {
                    throw new NonexistentEntityException("The races with id " + id + " no longer exists.");
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
            Races races;
            try {
                races = em.getReference(Races.class, id);
                races.getRaceId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The races with id " + id + " no longer exists.", enfe);
            }
            Circuits circuitId = races.getCircuitId();
            if (circuitId != null) {
                circuitId.getRacesCollection().remove(races);
                circuitId = em.merge(circuitId);
            }
            Collection<RaceResults> raceResultsCollection = races.getRaceResultsCollection();
            for (RaceResults raceResultsCollectionRaceResults : raceResultsCollection) {
                raceResultsCollectionRaceResults.setRaceId(null);
                raceResultsCollectionRaceResults = em.merge(raceResultsCollectionRaceResults);
            }
            em.remove(races);
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

    public List<Races> findRacesEntities() {
        return findRacesEntities(true, -1, -1);
    }

    public List<Races> findRacesEntities(int maxResults, int firstResult) {
        return findRacesEntities(false, maxResults, firstResult);
    }

    private List<Races> findRacesEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Races.class));
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

    public Races findRaces(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Races.class, id);
        } finally {
            em.close();
        }
    }

    public int getRacesCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Races> rt = cq.from(Races.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
