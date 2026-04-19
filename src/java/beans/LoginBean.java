/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package beans;

import entities.Users;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.TypedQuery;
import jakarta.faces.context.FacesContext;
import java.io.Serializable;


/**
 *
 * @author kacey
 */

@Named
@SessionScoped
public class LoginBean implements Serializable {

    private String username;
    private String password;
    private String errorMessage;
    private Users loggedInUser;

    private EntityManagerFactory emf =
        Persistence.createEntityManagerFactory("Formula_1PU");

    public String login() {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<Users> query = em.createQuery(
                "SELECT u FROM Users u WHERE u.username = :username " +
                "AND u.passwordHash = :password", Users.class);
            query.setParameter("username", username);
            query.setParameter("password", password);
            loggedInUser = query.getSingleResult();
            errorMessage = null;
            return "/index?faces-redirect=true";
        } catch (Exception e) {
            errorMessage = "Invalid username or password.";
            loggedInUser = null;
        } finally {
            em.close();
        }
        return null;
    }

    public String logout() {
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        return "/login?faces-redirect=true";
    }

    public String getWelcomeMessage() {
        if (loggedInUser != null) {
            return "Welcome, " + loggedInUser.getUsername() + "!";
        }
        return "";
    }

    public Users getLoggedInUser() { return loggedInUser; }
    public String getUsername() { return username; }
    public void setUsername(String u) { this.username = u; }
    public String getPassword() { return password; }
    public void setPassword(String p) { this.password = p; }
    public String getErrorMessage() { return errorMessage; }
    public void setErrorMessage(String m) { this.errorMessage = m; }
}