/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package beans;

import beans.exceptions.NonexistentEntityException;
import beans.exceptions.RollbackFailureException;
import entities.Bookmarks;
import java.io.Serializable;
import jakarta.persistence.Query;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import entities.Users;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.transaction.UserTransaction;
import java.util.List;

/**
 *
 * @author kacey
 */
public class BookmarksJpaController implements Serializable {

    public BookmarksJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Bookmarks bookmarks) throws RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Users userId = bookmarks.getUserId();
            if (userId != null) {
                userId = em.getReference(userId.getClass(), userId.getUserId());
                bookmarks.setUserId(userId);
            }
            em.persist(bookmarks);
            if (userId != null) {
                userId.getBookmarksCollection().add(bookmarks);
                userId = em.merge(userId);
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

    public void edit(Bookmarks bookmarks) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Bookmarks persistentBookmarks = em.find(Bookmarks.class, bookmarks.getBookmarkId());
            Users userIdOld = persistentBookmarks.getUserId();
            Users userIdNew = bookmarks.getUserId();
            if (userIdNew != null) {
                userIdNew = em.getReference(userIdNew.getClass(), userIdNew.getUserId());
                bookmarks.setUserId(userIdNew);
            }
            bookmarks = em.merge(bookmarks);
            if (userIdOld != null && !userIdOld.equals(userIdNew)) {
                userIdOld.getBookmarksCollection().remove(bookmarks);
                userIdOld = em.merge(userIdOld);
            }
            if (userIdNew != null && !userIdNew.equals(userIdOld)) {
                userIdNew.getBookmarksCollection().add(bookmarks);
                userIdNew = em.merge(userIdNew);
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
                Integer id = bookmarks.getBookmarkId();
                if (findBookmarks(id) == null) {
                    throw new NonexistentEntityException("The bookmarks with id " + id + " no longer exists.");
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
            Bookmarks bookmarks;
            try {
                bookmarks = em.getReference(Bookmarks.class, id);
                bookmarks.getBookmarkId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The bookmarks with id " + id + " no longer exists.", enfe);
            }
            Users userId = bookmarks.getUserId();
            if (userId != null) {
                userId.getBookmarksCollection().remove(bookmarks);
                userId = em.merge(userId);
            }
            em.remove(bookmarks);
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

    public List<Bookmarks> findBookmarksEntities() {
        return findBookmarksEntities(true, -1, -1);
    }

    public List<Bookmarks> findBookmarksEntities(int maxResults, int firstResult) {
        return findBookmarksEntities(false, maxResults, firstResult);
    }

    private List<Bookmarks> findBookmarksEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Bookmarks.class));
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

    public Bookmarks findBookmarks(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Bookmarks.class, id);
        } finally {
            em.close();
        }
    }

    public int getBookmarksCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Bookmarks> rt = cq.from(Bookmarks.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
