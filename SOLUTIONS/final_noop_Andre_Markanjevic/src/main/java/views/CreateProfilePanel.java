package views;

import controllers.LoginController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


/**
 * Klasa {@code CreateProfilePanel} predstavlja GUI prozor za kreiranje novog korisničkog profila.
 * <p>
 * Omogućuje korisnicima unos korisničkog imena, lozinke, e-mail adrese, fizičke adrese i ulogu
 * (admin ili obični korisnik), te kreiranje profila u aplikaciji.
 *
 * @author Andrej Markanjević
 */
public class CreateProfilePanel extends JFrame {

    private JTextField usernameField, emailField, addressField;
    private JPasswordField passwordField;
    private JLabel lblUsername, lblPassword, lblEmail, lblAddress, lblAdmin;
    private JPanel radioPanel, panel, buttonPanel;
    private ButtonGroup radioButtons;
    private JRadioButton adminButton, userButton;
    private JButton createButton, backToLoginButton;
    private JLabel boxErrorLabel;
    private LoginController lc = new LoginController();


    /**
     * Konstruktor klase {@code CreateProfilePanel}.
     * <p>
     * Inicijalizira korisničko sučelje za panel za kreiranje profila, uključujući polja za unos,
     * gumbe i oznaku za status. Također postavlja akcije za gumbe.
     */
    public CreateProfilePanel() {

        initCreateProfilePanel();
        layoutCreateProfilePanel();
        activateCreateProfilePanel();

    }

    /**
     * Inicijalizira sve GUI komponente potrebne za kreiranje korisničkog profila.
     */
    private void initCreateProfilePanel(){

        panel = new JPanel();
        buttonPanel = new JPanel();

        lblUsername = new JLabel("Username:");
        usernameField = new JTextField();

        lblPassword = new JLabel("Password:");
        passwordField = new JPasswordField();

        lblEmail = new JLabel("Email:");
        emailField = new JTextField();

        lblAddress = new JLabel("Address:");
        addressField = new JTextField();

        lblAdmin = new JLabel("Admin:");
        radioPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        userButton = new JRadioButton("No", true);
        adminButton = new JRadioButton("Yes");
        radioButtons = new ButtonGroup();

        createButton = new JButton("Create Profile");
        backToLoginButton = new JButton("Back to Login");
        buttonPanel = new JPanel(new GridLayout());

        boxErrorLabel = new JLabel("Fields are empty or user already exists!");

    }

    /**
     * Postavlja raspored elemenata unutar prozora za kreiranje profila.
     * <p>
     * Podesava veličinu prozora, raspored elemenata i inicijalizira njihove pozicije.
     */
    private void layoutCreateProfilePanel(){

        setTitle("Online shop");
        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setBounds(100, 100, 450, 500);
        setLocationRelativeTo(null);
        setContentPane(panel);
        panel.setLayout(null);

        radioButtons.add(userButton);
        radioButtons.add(adminButton);
        radioPanel.add(userButton);
        radioPanel.add(adminButton);
        userButton.setActionCommand("user");
        adminButton.setActionCommand("admin");

        buttonPanel.add(createButton);
        buttonPanel.add(backToLoginButton);

        panel.add(lblUsername);
        panel.add(usernameField);
        panel.add(lblPassword);
        panel.add(passwordField);
        panel.add(lblEmail);
        panel.add(emailField);
        panel.add(lblAddress);
        panel.add(addressField);
        panel.add(lblAdmin);
        panel.add(radioPanel);
        panel.add(buttonPanel);
        panel.add(boxErrorLabel);

        lblUsername.setBounds(10, 30, 100, 25);
        lblPassword.setBounds(10, 100, 100, 25);
        lblEmail.setBounds(10, 170, 100, 25);
        lblAddress.setBounds(10, 240, 100, 25);
        lblAdmin.setBounds(10, 310, 100, 25);

        usernameField.setBounds(150, 30, 150, 25);
        passwordField.setBounds(150, 100, 150, 25);
        emailField.setBounds(150, 170, 150, 25);
        addressField.setBounds(150, 240, 150, 25);
        radioPanel.setBounds(70, 310, 180, 25);
        boxErrorLabel.setBounds(130,380,230,25);
        buttonPanel.setBounds(80, 410, 300, 30);

        boxErrorLabel.setForeground(Color.RED);
        boxErrorLabel.setVisible(false);
        setVisible(true);
    }

    /**
     * Aktivira akcije za gumbe, uključujući kreiranje korisničkog profila i povratak na ekran za prijavu.
     */
    private void activateCreateProfilePanel(){
        createButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                createProfile();
            }

        });
        backToLoginButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
              openAppFrame();
            }

        });
    }

    private void openAppFrame(){
        dispose();
        new AppFrame();
    }

    /**
     * Metoda koja provjerava unesene podatke i kreira novi korisnički profil.
     * <p>
     * Ako su svi podaci ispravni, kreira korisnički profil koristeći kontroler {@code LoginController}.
     * U slučaju pogreške, prikazuje odgovarajuću poruku.
     */
    private void createProfile(){

        try {
            String un = usernameField.getText();
            String pw = new String(passwordField.getPassword());
            String em = emailField.getText();
            String ad = addressField.getText();
            boolean admin = radioButtons.getSelection().getActionCommand().equals("admin");

            if (un.equals("") || pw.equals("") || em.equals("") || ad.equals("")){

                boxErrorLabel.setVisible(true);

            }else{
                if (lc.createProfile(un, pw, em, ad, admin)){
                    openAppFrame();
                }else {
                    boxErrorLabel.setVisible(true);
                }
            }

        }catch (NullPointerException ex){
            boxErrorLabel.setVisible(true);

        }
    }
}