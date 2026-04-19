/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package beans;

import beans.exceptions.NonexistentEntityException;
import beans.exceptions.PreexistingEntityException;
import beans.exceptions.RollbackFailureException;
import entities.Circuits;
import java.io.Serializable;
import jakarta.persistence.Query;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
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
public class CircuitsJpaController implements Serializable {

    public CircuitsJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Circuits circuits) throws PreexistingEntityException, RollbackFailureException, Exception {
        if (circuits.getRacesCollection() == null) {
            circuits.setRacesCollection(new ArrayList<Races>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Collection<Races> attachedRacesCollection = new ArrayList<Races>();
            for (Races racesCollectionRacesToAttach : circuits.getRacesCollection()) {
                racesCollectionRacesToAttach = em.getReference(racesCollectionRacesToAttach.getClass(), racesCollectionRacesToAttach.getRaceId());
                attachedRacesCollection.add(racesCollectionRacesToAttach);
            }
            circuits.setRacesCollection(attachedRacesCollection);
            em.persist(circuits);
            for (Races racesCollectionRaces : circuits.getRacesCollection()) {
                Circuits oldCircuitIdOfRacesCollectionRaces = racesCollectionRaces.getCircuitId();
                racesCollectionRaces.setCircuitId(circuits);
                racesCollectionRaces = em.merge(racesCollectionRaces);
                if (oldCircuitIdOfRacesCollectionRaces != null) {
                    oldCircuitIdOfRacesCollectionRaces.getRacesCollection().remove(racesCollectionRaces);
                    oldCircuitIdOfRacesCollectionRaces = em.merge(oldCircuitIdOfRacesCollectionRaces);
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findCircuits(circuits.getCircuitId()) != null) {
                throw new PreexistingEntityException("Circuits " + circuits + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Circuits circuits) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Circuits persistentCircuits = em.find(Circuits.class, circuits.getCircuitId());
            Collection<Races> racesCollectionOld = persistentCircuits.getRacesCollection();
            Collection<Races> racesCollectionNew = circuits.getRacesCollection();
            Collection<Races> attachedRacesCollectionNew = new ArrayList<Races>();
            for (Races racesCollectionNewRacesToAttach : racesCollectionNew) {
                racesCollectionNewRacesToAttach = em.getReference(racesCollectionNewRacesToAttach.getClass(), racesCollectionNewRacesToAttach.getRaceId());
                attachedRacesCollectionNew.add(racesCollectionNewRacesToAttach);
            }
            racesCollectionNew = attachedRacesCollectionNew;
            circuits.setRacesCollection(racesCollectionNew);
            circuits = em.merge(circuits);
            for (Races racesCollectionOldRaces : racesCollectionOld) {
                if (!racesCollectionNew.contains(racesCollectionOldRaces)) {
                    racesCollectionOldRaces.setCircuitId(null);
                    racesCollectionOldRaces = em.merge(racesCollectionOldRaces);
                }
            }
            for (Races racesCollectionNewRaces : racesCollectionNew) {
                if (!racesCollectionOld.contains(racesCollectionNewRaces)) {
                    Circuits oldCircuitIdOfRacesCollectionNewRaces = racesCollectionNewRaces.getCircuitId();
                    racesCollectionNewRaces.setCircuitId(circuits);
                    racesCollectionNewRaces = em.merge(racesCollectionNewRaces);
                    if (oldCircuitIdOfRacesCollectionNewRaces != null && !oldCircuitIdOfRacesCollectionNewRaces.equals(circuits)) {
                        oldCircuitIdOfRacesCollectionNewRaces.getRacesCollection().remove(racesCollectionNewRaces);
                        oldCircuitIdOfRacesCollectionNewRaces = em.merge(oldCircuitIdOfRacesCollectionNewRaces);
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
                String id = circuits.getCircuitId();
                if (findCircuits(id) == null) {
                    throw new NonexistentEntityException("The circuits with id " + id + " no longer exists.");
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
            Circuits circuits;
            try {
                circuits = em.getReference(Circuits.class, id);
                circuits.getCircuitId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The circuits with id " + id + " no longer exists.", enfe);
            }
            Collection<Races> racesCollection = circuits.getRacesCollection();
            for (Races racesCollectionRaces : racesCollection) {
                racesCollectionRaces.setCircuitId(null);
                racesCollectionRaces = em.merge(racesCollectionRaces);
            }
            em.remove(circuits);
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

    public List<Circuits> findCircuitsEntities() {
        return findCircuitsEntities(true, -1, -1);
    }

    public List<Circuits> findCircuitsEntities(int maxResults, int firstResult) {
        return findCircuitsEntities(false, maxResults, firstResult);
    }

    private List<Circuits> findCircuitsEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Circuits.class));
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

    public Circuits findCircuits(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Circuits.class, id);
        } finally {
            em.close();
        }
    }

    public int getCircuitsCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Circuits> rt = cq.from(Circuits.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
