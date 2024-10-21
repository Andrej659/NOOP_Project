package controllers;

import models.Article;
import models.DataBase;
import models.User;

import java.util.List;
import java.sql.SQLException;
import java.util.Map;

/**
 * Klasa {@code ShopController} upravlja poslovanjem trgovine unutar aplikacije.
 * <p>
 * Omogućuje dohvaćanje artikala iz baze podataka te dodavanje artikala u korisničku košaricu.
 *
 * @author Andrej Markanjević
 */
public class ShopController {

    private DataBase db;

    /**
     * Konstruktor klase {@code ShopController}.
     * <p>
     * Inicijalizira kontroler trgovine i povezuje se s bazom podataka.
     * U slučaju pogreške prilikom povezivanja s bazom podataka, ispisuje stack trace.
     */
    public ShopController() {

        db = new DataBase();
        try {
            db.connect();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Dohvaća sve dostupne artikle iz baze podataka.
     * <p>
     * Učitava sve artikle iz baze podataka i vraća ih kao listu.
     * U slučaju pogreške prilikom rada s bazom podataka, ispisuje stack trace.
     *
     * @return Lista artikala iz baze podataka.
     */
    public List<Article> getArticles() {

        try {
            return db.loadArticlesFromDB();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Article getArticleById(int articleId){
        List<Article> art = getArticles();

        for (Article a : art){
            if (a.getArticleId() == articleId){
                return a;
            }
        }
        return null;
    }

    /**
     * Dodaje određenu količinu artikla u korisničku košaricu.
     * <p>
     * Na temelju ID-a korisnika i artikla, te količine, ažurira se korisnička košarica u bazi podataka.
     * U slučaju pogreške prilikom rada s bazom podataka, ispisuje stack trace.
     *
     * @param userID   Jedinstveni identifikator korisnika.
     */
    public void addOrder(int userID, Map<Article, Integer> articles) {

        User user = null;
        try {
            user = db.loadUserByIDFromDB(userID);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if(user == null){
            return;
        }

        try {
            db.addArticles2Order(articles,userID);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}