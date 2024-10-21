package controllers;

import models.Article;
import models.DataBase;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * Klasa {@code CartController} upravlja korisničkom košaricom u aplikaciji.
 * <p>
 * Omogućuje dohvaćanje artikala iz baze podataka te upravljanje artiklima u korisničkoj košarici.
 *
 * @author Andrej Markanjević
 */
public class CartController {

    private DataBase db = new DataBase();

    /**
     * Konstruktor klase {@code CartController}.
     * <p>
     * Pokušava uspostaviti vezu s bazom podataka prilikom inicijalizacije.
     * U slučaju pogreške prilikom povezivanja s bazom podataka, ispisuje stack trace.
     */
    public CartController() {
        try {
            db.connect();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Dohvaća artikle iz korisničke košarice na temelju korisničkog ID-a.
     * <p>
     * Učitava artikle koje je korisnik dodao u košaricu iz baze podataka.
     * U slučaju pogreške prilikom rada s bazom podataka, ispisuje stack trace.
     *
     * @param userID Jedinstveni identifikator korisnika.
     * @return Mapa artikala i njihovih količina za danog korisnika, ili {@code null} ako dođe do pogreške.
     */
    public Map<Integer, Map<Article, Integer>> getUserOrders(int userID) throws SQLException {
        List<Integer> orderIds = db.loadUserOrdersFromDB(userID);

        return db.loadArticlesFromUserOrders(orderIds);
    }
}