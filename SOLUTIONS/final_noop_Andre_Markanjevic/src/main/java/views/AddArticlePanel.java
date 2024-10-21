package views;

import controllers.ArticleController;
import controllers.LoginController;
import models.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

/**
 * Klasa {@code AddArticlePanel} predstavlja GUI prozor za dodavanje novih artikla.
 * <p>
 * Ova klasa omogućuje korisniku dodavanje novog artikla u bazu podataka.
 *
 * @author Andrej Markanjević
 */
public class AddArticlePanel extends JFrame{

    private JTextField articleNameField, priceField;
    private JLabel lblArticleName, lblArticlePrice;
    private JPanel panel, buttonPanel;
    private JButton createButton, backToLoginButton;
    private ArticleController ac = new ArticleController();
    private User user;

    /**
     * Konstruktor klase {@code AddArticlePanel}.
     * <p>
     * Inicijalizira panel za dodavanje novih artikla u bazu podataka.
     *
     * @param user Trenutno prijavljeni korisnik.
     */

    public AddArticlePanel(User user) {

        initAddArticlePanel();
        layoutAddArticlePanel();
        activateAddArticlePanel();
        this.user = user;
    }


    /**
     * Inicijalizira sve GUI komponente potrebne za prikaz admin panela.
     */
    private void initAddArticlePanel(){

        panel = new JPanel();
        buttonPanel = new JPanel();

        lblArticleName = new JLabel("Article name:");
        articleNameField = new JTextField();

        lblArticlePrice = new JLabel("Article price:");
        priceField = new JTextField();

        createButton = new JButton("Create article");
        backToLoginButton = new JButton("Back to shop");
        buttonPanel = new JPanel(new GridLayout());


    }

    /**
     * Postavlja raspored elemenata unutar prozora za kreiranje profila.
     * <p>
     * Podesava veličinu prozora, raspored elemenata i inicijalizira njihove pozicije.
     */
    private void layoutAddArticlePanel(){

        setTitle("Online shop");
        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setBounds(100, 100, 450, 500);
        setLocationRelativeTo(null);
        setContentPane(panel);
        panel.setLayout(null);


        buttonPanel.add(createButton);
        buttonPanel.add(backToLoginButton);

        panel.add(lblArticleName);
        panel.add(articleNameField);
        panel.add(lblArticlePrice);
        panel.add(priceField);
        panel.add(buttonPanel);

        lblArticleName.setBounds(10, 30, 100, 25);
        lblArticlePrice.setBounds(10, 100, 100, 25);


        articleNameField.setBounds(150, 30, 150, 25);
        priceField.setBounds(150, 100, 150, 25);
        buttonPanel.setBounds(80, 410, 300, 30);

        setVisible(true);
    }

    /**
     * Aktivira akcije za gumbe, uključujući kreiranje korisničkog profila i povratak na ekran za prijavu.
     */
    private void activateAddArticlePanel(){
        createButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    ac.addArticleToDB(articleNameField.getText(), Float.parseFloat(priceField.getText()));
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
                dispose();
                new ShopPanel(user);
            }

        });
        backToLoginButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                new ShopPanel(user);
            }

        });
    }
}
