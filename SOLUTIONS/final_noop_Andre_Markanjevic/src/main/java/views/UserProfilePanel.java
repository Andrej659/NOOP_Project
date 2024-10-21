package views;

import controllers.UserProfileController;
import models.Article;
import models.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Klasa {@code UserProfilePanel} predstavlja GUI prozor za prikaz i uređivanje korisničkog profila.
 * <p>
 * Ovaj panel omogućuje korisnicima pregled njihovih osnovnih informacija (korisničko ime, e-mail, adresa),
 * uređivanje tih informacija, te prijelaz natrag u trgovinu ili odjavu iz aplikacije.
 *
 * @author Andrej Markanjević
 */
public class UserProfilePanel extends JFrame {

    private UserProfileController upc;
    private JLabel usernameLabel, emailLabel, addressLabel, profilePicture;
    private JTextField usernameField, emailField, addressField;
    private JButton editButton, saveButton, adminButton, backToShopButton, logoutButton;
    private JPanel appBar, buttonPanel, profileContent, infoPanel, editPanel;
    private User user;


    /**
     * Konstruktor klase {@code UserProfilePanel}.
     * <p>
     * Inicijalizira panel za prikaz korisničkog profila te dodaje gumbe za povratak u trgovinu i odjavu.
     *
     * @param user Trenutno prijavljeni korisnik.
     */
    public UserProfilePanel(User user) {

        this.upc = new UserProfileController();
        this.user = user;

        initCartFrame();
        layoutProfileFrame();
        activateProfileFrame();
    }

    /**
     * Inicijalizira sve GUI komponente potrebne za prikaz korisničkog profila.
     */
    private void initCartFrame(){

        appBar = new JPanel(new BorderLayout());
        buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        adminButton = new JButton("All users");
        backToShopButton = new JButton("Back to Shop");
        logoutButton = new JButton("Logout");

        profileContent = new JPanel(new BorderLayout());
        infoPanel = new JPanel();
        profilePicture = new JLabel(new ImageIcon("src/main/resources/profile_pic.png"));

        usernameLabel = new JLabel("Username: " + user.getUsername());
        emailLabel = new JLabel("Email: " + user.getEmail());
        addressLabel = new JLabel("Address: " + user.getAddress());

        usernameField = new JTextField(user.getUsername());
        emailField = new JTextField(user.getEmail());
        addressField = new JTextField(user.getAddress());

        editPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        editButton = new JButton("Edit");
        saveButton = new JButton("Save");
    }

    /**
     * Postavlja raspored elemenata unutar prozora korisničkog profila.
     * <p>
     * Podesava veličinu prozora, raspored elemenata i inicijalizira njihove pozicije.
     */
    private void layoutProfileFrame(){

        setTitle("Online shop");
        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setBounds(100, 100, 350, 350);
        setLocationRelativeTo(null);

        setLayout(new BorderLayout());

        buttonPanel.add(adminButton);
        buttonPanel.add(backToShopButton);
        buttonPanel.add(logoutButton);
        appBar.add(buttonPanel, BorderLayout.NORTH);

        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));

        profileContent.add(profilePicture, BorderLayout.WEST);

        adminButton.setVisible(user.getAdminStatus());

        infoPanel.add(usernameLabel);
        infoPanel.add(emailLabel);
        infoPanel.add(addressLabel);

        profileContent.add(infoPanel, BorderLayout.CENTER);

        saveButton.setVisible(false);

        editPanel.add(editButton);
        editPanel.add(saveButton);
        profileContent.add(editPanel, BorderLayout.SOUTH);

        add(appBar, BorderLayout.NORTH);
        add(profileContent, BorderLayout.CENTER);

        setVisible(true);
    }

    /**
     * Aktivira funkcionalnosti unutar korisničkog profila, dodajući akcije za gumbe.
     * <p>
     * Postavlja akcije za uređivanje korisničkog profila, spremanje izmjena, pregled korisnika (admin),
     * povratak u trgovinu i odjavu.
     */
    private void activateProfileFrame(){
        editButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                switchToEditMode();
            }

        });
        saveButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                hasChanged();
            }

        });
        adminButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
               new AdminPanel(user);
            }

        });
        backToShopButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                new ShopPanel(user);
            }

        });
        logoutButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                user = null;
                dispose();
                new AppFrame();
            }

        });
    }

    /**
     * Provjerava jesu li korisnički podaci promijenjeni i poziva metodu za ažuriranje podataka.
     */
    private void hasChanged(){

        if (user.getUsername().equals(usernameField.getText()) && user.getEmail().equals(emailField.getText()) && user.getAddress().equals(addressField.getText())){
            returnToNormal();
        }else{
            saveUserData();
        }
    }

    /**
     * Sprema nove podatke u bazu podataka, radi provjeru da li username već postoji u bazi.
     * Također nakon spremanja podataka vraća stranicu u view mode.
     */
    private void saveUserData() {

        User newUser = new User(user.getUserId(), usernameField.getText(), user.getPassword(), emailField.getText(), addressField.getText(), user.getAdminStatus());

        if (usernameField.getText().isEmpty() || usernameField.getText().equals("Can't be empty!") || usernameField.getText().equals("Username is taken!")){
            usernameField.setText("Can't be empty!");
            return;
        }else if (emailField.getText().isEmpty() || emailField.getText().equals("Can't be empty!")) {
            emailField.setText("Can't be empty!");
            return;
        }else if (addressField.getText().isEmpty() || addressField.getText().equals("Can't be empty!")) {
            addressField.setText("Can't be empty!");
            return;
        }

        if (!upc.changeUserInfo(newUser)){
            usernameField.setText("Username is taken!");

        }else {
            user = newUser;
            returnToNormal();
        }
    }


    /**
     * Prebacuje sučelje u način uređivanja, omogućujući korisniku da mijenja svoje podatke.
     */
    private void switchToEditMode() {
        usernameLabel.setVisible(false);
        emailLabel.setVisible(false);
        addressLabel.setVisible(false);

        JPanel infoPanel = (JPanel) usernameLabel.getParent();
        infoPanel.removeAll();

        infoPanel.add(new JLabel("Username"));
        infoPanel.add(usernameField);
        infoPanel.add(new JLabel("Email"));
        infoPanel.add(emailField);
        infoPanel.add(new JLabel("Address"));
        infoPanel.add(addressField);

        editButton.setVisible(false);
        saveButton.setVisible(true);

        revalidate();
        repaint();
    }

    /**
     * Konkretnija metoda koja frame vraća u view mode, te ga pri tome osvježi.
     */
    private void returnToNormal(){

        usernameLabel.setText("Username: " + user.getUsername());
        emailLabel.setText("Email: " + user.getEmail());
        addressLabel.setText("Address: " + user.getAddress());

        JPanel infoPanel = (JPanel) usernameField.getParent();
        infoPanel.removeAll();

        infoPanel.add(usernameLabel);
        infoPanel.add(emailLabel);
        infoPanel.add(addressLabel);

        editButton.setVisible(true);
        saveButton.setVisible(false);

        usernameLabel.setVisible(true);
        emailLabel.setVisible(true);
        addressLabel.setVisible(true);

        revalidate();
        repaint();

    }
}