package models;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Klasa {@code User} predstavlja model korisnika u sustavu.
 * Sadrži osnovne informacije o korisniku kao što su ID korisnika, korisničko ime,
 * lozinka, e-mail, adresa te artikli koje korisnik posjeduje.
 *
 * @author Andrej Markanjević
 */
public class User {

    private int userId;
    private String username, password, email, address;
    private boolean admin;
    private List<Integer> orders;
    /**
     * Konstruktor klase {@code User}.
     * <p>
     * Inicijalizira korisnika s osnovnim podacima, uključujući ID korisnika, korisničko ime, lozinku, e-mail adresu, fizičku adresu i status administratora.
     *
     * @param userId   Jedinstveni identifikator korisnika.
     * @param username Korisničko ime korisnika.
     * @param password Lozinka korisnika.
     * @param email    E-mail adresa korisnika.
     * @param address  Fizička adresa korisnika.
     * @param admin    Status administratora korisnika.
     */
    public User(int userId, String username, String password, String email, String address, boolean admin) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.email = email;
        this.address = address;
        this.admin = admin;
        this.orders = new ArrayList<>();
    }

    public void setOrders(List<Integer> orders) {
        this.orders = orders;
    }

    public List<Integer> getOrders() {
        return orders;
    }

    public int getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public String getAddress() {
        return address;
    }

    public boolean getAdminStatus() {
        return admin;
    }

    public boolean isAdmin() {
        return admin;
    }

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", address='" + address + '\'' +
                ", admin=" + admin +
                '}';
    }
}