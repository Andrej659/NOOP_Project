package views;

import controllers.LoginController;
import models.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

/**
 * Klasa {@code AppFrame} predstavlja glavni okvir aplikacije za online trgovinu.
 * <p>
 * Ova klasa omogućuje korisnicima prijavu u aplikaciju i
 * prelazak na odgovarajuće panele (npr. trgovina ili kreiranje profila).
 *
 * @author Andrej Markanjević
 */

public class AppFrame extends JFrame {

    private JPanel panel, buttonPanel;
    private JLabel usernameLabel, passwordLabel, boxErrorLabel;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton, createProfileButton;
    private LoginController lc;

    /**
     * Trenutno prijavljeni korisnik.
     */
    private User user;


    /**
     * Konstruktor klase {@code AppFrame}.
     * <p>
     * Inicijalizira prozor aplikacije, postavlja izgled, aktivira kontrole
     * i instancira kontroler za prijavu.
     */
    public AppFrame() {

        initAppFrame();
        layoutAppFrame();
        activateAppFrame();
        this.lc = new LoginController();

    }

    /**
     * Metoda koja inicijalizira komponente unutar okvira aplikacije.
     */
    public void initAppFrame() {

        panel = new JPanel();
        usernameLabel = new JLabel("Username:");
        usernameField = new JTextField(10);
        passwordLabel = new JLabel("Password:");
        passwordField = new JPasswordField(10);
        loginButton = new JButton("Login");
        createProfileButton = new JButton("Create Profile");
        buttonPanel = new JPanel(new GridLayout());
        boxErrorLabel = new JLabel("Fields cannot be empty or user already exists!");

    }

    /**
     * Postavlja raspored i izgled komponenti u prozoru aplikacije.
     * <p>
     * Postavlja naslove, dimenzije, te dodaje komponente poput gumba i polja
     * za unos korisničkih podataka u prozor aplikacije.
     */
    public void layoutAppFrame(){

        setTitle("Online shop");
        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setBounds(50, 50, 400, 300);
        setLocationRelativeTo(null);
        setContentPane(panel);
        panel.setLayout(null);

        boxErrorLabel.setVisible(false);
        boxErrorLabel.setForeground(Color.RED);

        buttonPanel.add(loginButton);
        buttonPanel.add(createProfileButton);

        panel.add(usernameLabel);
        panel.add(usernameField);
        panel.add(passwordLabel);
        panel.add(passwordField);
        panel.add(buttonPanel);
        panel.add(boxErrorLabel);

        usernameLabel.setBounds(20,30,150,25);
        usernameField.setBounds(150, 30, 150, 25);
        passwordLabel.setBounds(20, 100, 150, 25);
        passwordField.setBounds(150, 100, 150, 25);
        boxErrorLabel.setBounds(60,190,260,25);
        buttonPanel.setBounds(40, 220, 300, 30);

        setVisible(true);

    }

    /**
     * Aktivira funkcionalnosti unutar okvira aplikacije, dodajući akcije za gumbe.
     * <p>
     * Postavlja akcije za prijavu korisnika i prelazak na panel za kreiranje profila.
     */
    public void activateAppFrame(){
        loginButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                login();
            }

        });

       createProfileButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                openCreateProfilePanel();
            }

        });
    }

    /**
     * Metoda za prijavu korisnika na temelju unesenih podataka.
     * <p>
     * Provjerava korisničko ime i lozinku te prebacuje korisnika na panel za trgovinu ako su podaci ispravni.
     * Ako su podaci netočni, prikazuje poruku o pogrešci.
     */
    private void login(){

        try {
            String un = usernameField.getText();
            String pw = new String(passwordField.getPassword());

            User u = lc.getUser(un,pw);
            if (u != null ){
                setUser(u);
                openShopPanel(user);
            }else{
                boxErrorLabel.setVisible(true);
            }

        }catch (SQLException ex){
            boxErrorLabel.setVisible(true);
        }
    }

    /**
     * Prebacuje korisnika na frame za trgovinu.
     *
     * @param u Trenutno prijavljeni korisnik.
     */
    private void openShopPanel(User u){
        dispose();
        new ShopPanel(u);
    }

    /**
     * Prebacuje korisnika na frame za kreiranje profila.
     */
    private void openCreateProfilePanel(){
        dispose();
        new CreateProfilePanel();
    }

    public void setUser(User user) {
        this.user = user;
    }

}