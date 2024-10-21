package controllers;

import models.DataBase;
import models.User;
import java.sql.SQLException;

/**
* Klasa {@code LoginController} pruža metode za autentifikaciju korisnika i kreiranje novih korisničkih profila.
* <p>
* Ova klasa omogućuje preuzimanje korisničkih podataka iz baze podataka na temelju korisničkog imena i lozinke,
* te kreiranje novih korisnika u sustavu.
*
* @author Andrej Markanjević
*/
public class LoginController {

    private DataBase db = new DataBase();

    /**
     * Konstruktor klase {@code LoginController}.
     * <p>
     * Inicijalizira kontroler za autentifikaciju korisnika i pokušava se povezati s bazom podataka.
     * U slučaju pogreške prilikom povezivanja s bazom podataka, ispisuje stack trace.
     */
    public LoginController() {

        try {
            db.connect();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Dohvaća korisnika na temelju korisničkog imena.
     * <p>
     * Provjerava postoji li korisnik s navedenim korisničkim imenom u bazi podataka.
     * Ako korisnik ne postoji, vraća {@code null}. Inače vraća korisnika iz baze podataka.
     *
     * @param username Korisničko ime korisnika.
     * @return Korisnik ako postoji u bazi podataka, inače {@code null}.
     * @throws SQLException U slučaju greške prilikom rada s bazom podataka.
     */
    public User getUser(String username, String password) throws SQLException {
        return db.loadAndAuthUserByUsernameFromDB(username,password);
    }


    /**
     * Stvara novi korisnički profil u bazi podataka.
     * <p>
     * Provjerava postoji li korisničko ime u bazi podataka. Ako korisničko ime već postoji, vraća {@code false}.
     * U suprotnom, kreira novog korisnika s jedinstvenim ID-om i pohranjuje ga u bazu podataka.
     *
     * @param username Korisničko ime novog korisnika.
     * @param password Lozinka novog korisnika.
     * @param email    E-mail adresa novog korisnika.
     * @param address  Adresa novog korisnika.
     * @param admin    Status administratora.
     * @return {@code true} ako je profil uspješno kreiran, inače {@code false}.
     */
    public boolean createProfile(String username, String password, String email, String address, boolean admin) {
        try {
            if (db.addUser(username,password,email,address,Boolean.toString(admin))){
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}