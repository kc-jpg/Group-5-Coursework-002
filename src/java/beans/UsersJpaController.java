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
import entities.Bookmarks;
import entities.Users;
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
public class UsersJpaController implements Serializable {

    public UsersJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Users users) throws RollbackFailureException, Exception {
        if (users.getBookmarksCollection() == null) {
            users.setBookmarksCollection(new ArrayList<Bookmarks>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Collection<Bookmarks> attachedBookmarksCollection = new ArrayList<Bookmarks>();
            for (Bookmarks bookmarksCollectionBookmarksToAttach : users.getBookmarksCollection()) {
                bookmarksCollectionBookmarksToAttach = em.getReference(bookmarksCollectionBookmarksToAttach.getClass(), bookmarksCollectionBookmarksToAttach.getBookmarkId());
                attachedBookmarksCollection.add(bookmarksCollectionBookmarksToAttach);
            }
            users.setBookmarksCollection(attachedBookmarksCollection);
            em.persist(users);
            for (Bookmarks bookmarksCollectionBookmarks : users.getBookmarksCollection()) {
                Users oldUserIdOfBookmarksCollectionBookmarks = bookmarksCollectionBookmarks.getUserId();
                bookmarksCollectionBookmarks.setUserId(users);
                bookmarksCollectionBookmarks = em.merge(bookmarksCollectionBookmarks);
                if (oldUserIdOfBookmarksCollectionBookmarks != null) {
                    oldUserIdOfBookmarksCollectionBookmarks.getBookmarksCollection().remove(bookmarksCollectionBookmarks);
                    oldUserIdOfBookmarksCollectionBookmarks = em.merge(oldUserIdOfBookmarksCollectionBookmarks);
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

    public void edit(Users users) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Users persistentUsers = em.find(Users.class, users.getUserId());
            Collection<Bookmarks> bookmarksCollectionOld = persistentUsers.getBookmarksCollection();
            Collection<Bookmarks> bookmarksCollectionNew = users.getBookmarksCollection();
            Collection<Bookmarks> attachedBookmarksCollectionNew = new ArrayList<Bookmarks>();
            for (Bookmarks bookmarksCollectionNewBookmarksToAttach : bookmarksCollectionNew) {
                bookmarksCollectionNewBookmarksToAttach = em.getReference(bookmarksCollectionNewBookmarksToAttach.getClass(), bookmarksCollectionNewBookmarksToAttach.getBookmarkId());
                attachedBookmarksCollectionNew.add(bookmarksCollectionNewBookmarksToAttach);
            }
            bookmarksCollectionNew = attachedBookmarksCollectionNew;
            users.setBookmarksCollection(bookmarksCollectionNew);
            users = em.merge(users);
            for (Bookmarks bookmarksCollectionOldBookmarks : bookmarksCollectionOld) {
                if (!bookmarksCollectionNew.contains(bookmarksCollectionOldBookmarks)) {
                    bookmarksCollectionOldBookmarks.setUserId(null);
                    bookmarksCollectionOldBookmarks = em.merge(bookmarksCollectionOldBookmarks);
                }
            }
            for (Bookmarks bookmarksCollectionNewBookmarks : bookmarksCollectionNew) {
                if (!bookmarksCollectionOld.contains(bookmarksCollectionNewBookmarks)) {
                    Users oldUserIdOfBookmarksCollectionNewBookmarks = bookmarksCollectionNewBookmarks.getUserId();
                    bookmarksCollectionNewBookmarks.setUserId(users);
                    bookmarksCollectionNewBookmarks = em.merge(bookmarksCollectionNewBookmarks);
                    if (oldUserIdOfBookmarksCollectionNewBookmarks != null && !oldUserIdOfBookmarksCollectionNewBookmarks.equals(users)) {
                        oldUserIdOfBookmarksCollectionNewBookmarks.getBookmarksCollection().remove(bookmarksCollectionNewBookmarks);
                        oldUserIdOfBookmarksCollectionNewBookmarks = em.merge(oldUserIdOfBookmarksCollectionNewBookmarks);
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
                Integer id = users.getUserId();
                if (findUsers(id) == null) {
                    throw new NonexistentEntityException("The users with id " + id + " no longer exists.");
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
            Users users;
            try {
                users = em.getReference(Users.class, id);
                users.getUserId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The users with id " + id + " no longer exists.", enfe);
            }
            Collection<Bookmarks> bookmarksCollection = users.getBookmarksCollection();
            for (Bookmarks bookmarksCollectionBookmarks : bookmarksCollection) {
                bookmarksCollectionBookmarks.setUserId(null);
                bookmarksCollectionBookmarks = em.merge(bookmarksCollectionBookmarks);
            }
            em.remove(users);
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

    public List<Users> findUsersEntities() {
        return findUsersEntities(true, -1, -1);
    }

    public List<Users> findUsersEntities(int maxResults, int firstResult) {
        return findUsersEntities(false, maxResults, firstResult);
    }

    private List<Users> findUsersEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Users.class));
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

    public Users findUsers(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Users.class, id);
        } finally {
            em.close();
        }
    }

    public int getUsersCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Users> rt = cq.from(Users.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
