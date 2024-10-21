package controllers;

import models.DataBase;
import models.User;

import java.sql.SQLException;
import java.util.List;

/**
 * Klasa {@code AdminController} pruža funkcionalnosti za upravljanje korisnicima u administratorskom dijelu aplikacije.
 * <p>
 * Omogućuje administratorima pregled korisnika i dohvaćanje podataka o korisnicima iz baze podataka.
 *
 *  @author Andrej Markanjević
 */
public class AdminController {

    private DataBase db = new DataBase();


    /**
     * Konstruktor klase {@code AdminController}.
     * <p>
     * Povezuje se s bazom podataka prilikom inicijalizacije.
     * U slučaju pogreške prilikom povezivanja s bazom podataka, ispisuje stack trace.
     */

    public AdminController() {
        try {
            db.connect();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Dohvaća sve korisnike iz baze podataka.
     * <p>
     * Učitava korisnike iz baze podataka, kao i njihove povezane artikle.
     * U slučaju pogreške tijekom rada s bazom podataka, ispisuje stack trace.
     *
     * @return Lista svih korisnika iz baze podataka, zajedno s njihovim artiklima.
     */
    public List<User> getUsers() {

        try {
            List<User> users = db.loadAllUsersFromDB();
            return users;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}