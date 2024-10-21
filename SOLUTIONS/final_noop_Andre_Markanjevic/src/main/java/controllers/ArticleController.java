package controllers;

import models.DataBase;

import java.sql.SQLException;

public class ArticleController {

    private DataBase db;

    public ArticleController() {

        db = new DataBase();
        try {
            db.connect();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addArticleToDB(String articleName, float articlePrice) throws SQLException {
        db.addArticleToDB(db.createNewID("Articles","articleId"), articleName, articlePrice);
    }

}
