package controllers;

import models.DataBase;
import models.User;
import java.sql.SQLException;


/**
 * Klasa {@code UserProfileController} upravlja korisničkim profilima unutar aplikacije.
 * <p>
 * Omogućuje promjenu korisničkih informacija, dohvaćanje podataka o korisnicima i provjeru postojanja korisničkog imena.
 *
 * @author Andrej Markanjević
 */
public class UserProfileController {

    private DataBase db;

    /**
     * Konstruktor klase {@code UserProfileController}.
     * <p>
     * Inicijalizira objekt baze podataka i uspostavlja vezu s bazom.
     * U slučaju pogreške prilikom povezivanja, ispisuje stack trace.
     */
    public UserProfileController() {

        this.db = new DataBase();
        try {
            db.connect();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Mijenja korisničke informacije i ažurira ih u bazi podataka.
     * <p>
     * Provjerava postoji li korisničko ime. Ako postoji, vraća {@code false}.
     * Ako ne postoji, ažurira korisnika u bazi podataka i vraća {@code true}.
     *
     * @param newUser Korisnik s novim informacijama.
     * @return {@code true} ako su informacije uspješno promijenjene, inače {@code false}.
     */
    public boolean changeUserInfo(User newUser) {

        try {
            db.updateUser(newUser);
        } catch (SQLException e) {
            return false;
        }
        return true;
    }
}